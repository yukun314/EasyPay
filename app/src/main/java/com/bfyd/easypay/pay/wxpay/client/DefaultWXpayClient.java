/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.bfyd.easypay.pay.wxpay.client;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayConstants;
import com.bfyd.easypay.pay.wxpay.WXConfigure;
import com.bfyd.easypay.pay.wxpay.request.WXPayRequest;
import com.bfyd.easypay.pay.wxpay.response.WXPayResponse;
import com.bfyd.easypay.pay.wxpay.XMLParser;
import com.bfyd.easypay.utils.MD5;
import com.alipay.api.internal.util.StringUtils;
import com.alipay.api.internal.util.WebUtils;
import com.bfyd.easypay.utils.WXUtils;

import org.json.JSONException;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Security;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

/**
 * 
 * @author runzhi
 * @version $Id: DefaultAlipayClient.java, v 0.1 2012-11-49:45:21 runzhi Exp $
 */
public class DefaultWXpayClient{

    private String serverUrl;
    private String appId;
    private String privateKey;
    private String prodCode;
    private String format         = AlipayConstants.FORMAT_XML;
    private String sign_type      = AlipayConstants.SIGN_TYPE_RSA;

    private String charset;

    private int    connectTimeout = 3000;
    private int    readTimeout    = 15000;

    static {
        //清除安全设置
        Security.setProperty("jdk.certpath.disabledAlgorithms", "");
    }

    public DefaultWXpayClient(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public DefaultWXpayClient(String serverUrl, String format) {
        this(serverUrl);
        this.format = format;
    }

    public DefaultWXpayClient(String serverUrl, String format, String charset) {
        this(serverUrl);
        this.format = format;
        this.charset = charset;
    }

    public <T extends WXPayResponse> T execute(WXPayRequest request, Class<T> clazz) throws AlipayApiException {
        return execute(request, null, clazz);
    }

    public <T extends WXPayResponse> T execute(WXPayRequest request, String accessToken, Class<T> clazz)
            throws AlipayApiException {
        return _execute(request, accessToken, clazz);
    }

    //原始返回结果 在这里处理(签名验证)后返回
    private <T extends WXPayResponse> T _execute(WXPayRequest request, String authToken, Class<T> clazz)
            throws AlipayApiException {
        String rsp = doPost(request, authToken);
//        System.out.println("rsp:"+rsp);
        T tRsp = null;
        try {
            tRsp = XMLParser.fromXML(rsp, clazz);

            if(tRsp != null && (tRsp.return_code.equals("SUCCESS") | tRsp.return_code == "SUCCESS")) {
                //FIXME 签名验证, 还没测试
                //微信接口可能增加字段，验证签名时必须支持增加的扩展字段
                //这里验证不使用T的字段
                Map<String, Object> map = XMLParser.getMapFromXML(rsp);
                String newSign = WXUtils.genSign(map);
                //微信返回的签名
                String oldSign = (String)map.get("sign");
                if(!(newSign.equals(oldSign) | newSign == oldSign)){
                    //签名不一致 (如数据被截获并做了修改等)
                    throw new AlipayApiException("sign check fail: check Sign and Data Fail!");
                }
            }
        } catch (ParserConfigurationException e) {
//            e.printStackTrace();
            throw new AlipayApiException("ParserConfigurationException错误:"+e);
        } catch (IOException e) {
//            e.printStackTrace();
            throw new AlipayApiException("IO错误:"+e);
        } catch (SAXException e) {
//            e.printStackTrace();
            throw new AlipayApiException("SAXException错误:"+e);
        } catch (JSONException e) {
//            e.printStackTrace();
            throw new AlipayApiException("JSONException错误:"+e);
        }
        return tRsp;
    }

    /**
     * 根据微信的签名规则 签名后请求
     */
    public <T extends WXPayResponse> String doPost(WXPayRequest request,
                                                                 String accessToken) throws AlipayApiException {
        if (StringUtils.isEmpty(charset)) {
            charset = AlipayConstants.CHARSET_UTF8;
        }

        Map<String, Object> map = request.toMap();
        String sign = WXUtils.genSign(map);
        request.setSign(sign);
        String rsp = null;
        String params = "";
        try {
            params = XMLParser.toXML(request);
            System.out.println("params:"+params);
        } catch (IllegalAccessException e) {
            throw new AlipayApiException("格式化参数错误:"+e);
        } catch (UnsupportedEncodingException e) {
            throw new AlipayApiException("格式化参数错误:"+e);
        }
        try {
//            System.out.println("请求参数:"+params);
            rsp = WebUtils.doPost(serverUrl, params, charset, connectTimeout,
                        readTimeout);
        } catch (IOException e) {
            throw new AlipayApiException("IO错误:"+e);
        }
        return rsp;
    }



}
