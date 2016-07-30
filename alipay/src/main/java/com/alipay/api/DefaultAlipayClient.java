/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.api;

import java.io.IOException;
import java.security.Security;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import com.alipay.api.internal.parser.json.ObjectJsonParser;
import com.alipay.api.internal.parser.xml.ObjectXmlParser;
import com.alipay.api.internal.util.AlipayHashMap;
import com.alipay.api.internal.util.AlipayLogger;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.internal.util.AlipayUtils;
import com.alipay.api.internal.util.RequestParametersHolder;
import com.alipay.api.internal.util.StringUtils;
import com.alipay.api.internal.util.WebUtils;

/**
 * 
 * @author runzhi
 * @version $Id: DefaultAlipayClient.java, v 0.1 2012-11-49:45:21 runzhi Exp $
 */
public class DefaultAlipayClient implements AlipayClient {

    private String serverUrl;
    private String appId;
    private String privateKey;
    private String prodCode;
    private String format         = AlipayConstants.FORMAT_JSON;
    private String sign_type      = AlipayConstants.SIGN_TYPE_RSA;

    private String alipayPublicKey;

    private String charset;

    private int    connectTimeout = 3000;
    private int    readTimeout    = 15000;

    static {
        //清除安全设置
        Security.setProperty("jdk.certpath.disabledAlgorithms", "");
    }

    public DefaultAlipayClient(String serverUrl, String appId, String privateKey) {
        this.serverUrl = serverUrl;
        this.appId = appId;
        this.privateKey = privateKey;
        this.alipayPublicKey = null;
    }

    public DefaultAlipayClient(String serverUrl, String appId, String privateKey, String format) {
        this(serverUrl, appId, privateKey);
        this.format = format;
    }

    public DefaultAlipayClient(String serverUrl, String appId, String privateKey, String format,
                               String charset) {
        this(serverUrl, appId, privateKey);
        this.format = format;
        this.charset = charset;
    }

    public DefaultAlipayClient(String serverUrl, String appId, String privateKey, String format,
                               String charset, String alipayPulicKey) {
        this(serverUrl, appId, privateKey);
        this.format = format;
        this.charset = charset;
        this.alipayPublicKey = alipayPulicKey;
    }

    public <T extends AlipayResponse> T execute(AlipayRequest<T> request) throws AlipayApiException {
        return execute(request, null);
    }

    public <T extends AlipayResponse> T execute(AlipayRequest<T> request, String accessToken)
                                                                                             throws AlipayApiException {

        AlipayParser<T> parser = null;
        if (AlipayConstants.FORMAT_XML.equals(this.format)) {
            parser = new ObjectXmlParser<T>(request.getResponseClass());
        } else {
            parser = new ObjectJsonParser<T>(request.getResponseClass());
        }
        System.out.println("AlipayRequest:"+request.toString());
        return _execute(request, parser, accessToken);
    }

    private <T extends AlipayResponse> T _execute(AlipayRequest<T> request, AlipayParser<T> parser,
                                                  String authToken) throws AlipayApiException {
        Map<String, Object> rt = doPost(request, authToken);
        if (rt == null) {
            return null;
        }
        T tRsp = null;
        try {
            System.out.println("_execute rsp:"+rt.get("rsp"));
            tRsp = parser.parse((String) rt.get("rsp"));
            tRsp.setBody((String) rt.get("rsp"));

            // 针对成功结果且有支付宝公钥的进行验签
            if (!StringUtils.isEmpty(this.alipayPublicKey)) {

                SignItem signItem = parser.getSignItem(request, tRsp);

                if (signItem == null) {
                    throw new AlipayApiException("sign check fail: Body is Empty!");
                }

                if (tRsp.isSuccess()
                    || (!tRsp.isSuccess() && !StringUtils.isEmpty(signItem.getSign()))) {

                    boolean rsaCheckContent = AlipaySignature.rsaCheckContent(
                        signItem.getSignSourceDate(), signItem.getSign(), this.alipayPublicKey,
                        this.charset);

                    if (!rsaCheckContent) {

                        // 针对JSON \/问题，替换/后再尝试做一次验证
                        if (!StringUtils.isEmpty(signItem.getSignSourceDate())
                            && signItem.getSignSourceDate().contains("\\/")) {

                            String srouceData = signItem.getSignSourceDate().replace("\\/", "/");

                            boolean jsonCheck = AlipaySignature.rsaCheckContent(srouceData,
                                signItem.getSign(), this.alipayPublicKey, this.charset);

                            if (!jsonCheck) {
                                throw new AlipayApiException(
                                    "sign check fail: check Sign and Data Fail！JSON also！");
                            }
                        } else {

                            throw new AlipayApiException(
                                "sign check fail: check Sign and Data Fail!");
                        }
                    }
                }
            }

        } catch (RuntimeException e) {
            AlipayLogger.logBizError((String) rt.get("rsp"));
            throw e;
        } catch (AlipayApiException e) {
            AlipayLogger.logBizError((String) rt.get("rsp"));
            throw new AlipayApiException(e);
        }
        tRsp.setParams((AlipayHashMap) rt.get("textParams"));
        if (!tRsp.isSuccess()) {
            AlipayLogger.logErrorScene(rt, tRsp, "");
        }
        return tRsp;
    }

    /**
     * 
     * 
     * @param request
     * @param accessToken
     * @return
     * @throws AlipayApiException
     */
    public <T extends AlipayResponse> Map<String, Object> doPost(AlipayRequest<T> request,
                                                                 String accessToken)
                                                                                    throws AlipayApiException {
        Map<String, Object> result = new HashMap<String, Object>();
        RequestParametersHolder requestHolder = new RequestParametersHolder();
        AlipayHashMap appParams = new AlipayHashMap(request.getTextParams());
        requestHolder.setApplicationParams(appParams);

        if (StringUtils.isEmpty(charset)) {
            charset = AlipayConstants.CHARSET_UTF8;
        }

        AlipayHashMap protocalMustParams = new AlipayHashMap();
        protocalMustParams.put(AlipayConstants.METHOD, request.getApiMethodName());
        protocalMustParams.put(AlipayConstants.VERSION, request.getApiVersion());
        protocalMustParams.put(AlipayConstants.APP_ID, this.appId);
        protocalMustParams.put(AlipayConstants.SIGN_TYPE, this.sign_type);
        protocalMustParams.put(AlipayConstants.TERMINAL_TYPE, request.getTerminalType());
        protocalMustParams.put(AlipayConstants.TERMINAL_INFO, request.getTerminalInfo());
        protocalMustParams.put(AlipayConstants.NOTIFY_URL, request.getNotifyUrl());
        protocalMustParams.put(AlipayConstants.CHARSET, charset);

        Long timestamp = System.currentTimeMillis();
        DateFormat df = new SimpleDateFormat(AlipayConstants.DATE_TIME_FORMAT);
        df.setTimeZone(TimeZone.getTimeZone(AlipayConstants.DATE_TIMEZONE));
        protocalMustParams.put(AlipayConstants.TIMESTAMP, df.format(new Date(timestamp)));
        requestHolder.setProtocalMustParams(protocalMustParams);

        AlipayHashMap protocalOptParams = new AlipayHashMap();
        protocalOptParams.put(AlipayConstants.FORMAT, format);
        protocalOptParams.put(AlipayConstants.ACCESS_TOKEN, accessToken);
        protocalOptParams.put(AlipayConstants.ALIPAY_SDK, AlipayConstants.SDK_VERSION);
        protocalOptParams.put(AlipayConstants.PROD_CODE, request.getProdCode());
        requestHolder.setProtocalOptParams(protocalOptParams);

        if (AlipayConstants.SIGN_TYPE_RSA.equals(this.sign_type)) {

            String signContent = AlipaySignature.getSignatureContent(requestHolder);

            protocalMustParams.put(AlipayConstants.SIGN,
                AlipaySignature.rsaSign(signContent, privateKey, charset));

        } else {
            protocalMustParams.put(AlipayConstants.SIGN, "");
        }

        StringBuffer urlSb = new StringBuffer(serverUrl);
        try {
            String sysMustQuery = WebUtils.buildQuery(requestHolder.getProtocalMustParams(),
                charset);
            String sysOptQuery = WebUtils.buildQuery(requestHolder.getProtocalOptParams(), charset);

            urlSb.append("?");
            urlSb.append(sysMustQuery);
            if (sysOptQuery != null & sysOptQuery.length() > 0) {
                urlSb.append("&");
                urlSb.append(sysOptQuery);
            }
        } catch (IOException e) {
            throw new AlipayApiException(e);
        }

        String rsp = null;
        try {
            if (request instanceof AlipayUploadRequest) {
                AlipayUploadRequest<T> uRequest = (AlipayUploadRequest<T>) request;
                Map<String, FileItem> fileParams = AlipayUtils.cleanupMap(uRequest.getFileParams());
                rsp = WebUtils.doPost(urlSb.toString(), appParams, fileParams, charset,
                    connectTimeout, readTimeout);
            } else {
                rsp = WebUtils.doPost(urlSb.toString(), appParams, charset, connectTimeout,
                    readTimeout);
            }
        } catch (IOException e) {
            throw new AlipayApiException(e);
        }
        System.out.println("doPost rsp:"+rsp);
        result.put("rsp", rsp);
        result.put("textParams", appParams);
        result.put("protocalMustParams", protocalMustParams);
        result.put("protocalOptParams", protocalOptParams);
        result.put("url", urlSb.toString());
        return result;
    }
}
