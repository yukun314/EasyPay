package com.alipay.api.internal.parser.json;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayParser;
import com.alipay.api.AlipayRequest;
import com.alipay.api.AlipayResponse;
import com.alipay.api.SignItem;
import com.alipay.api.internal.mapping.Converter;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * 单个JSON对象解释器。
 * 
 * @author carver.gu
 * @since 1.0, Apr 11, 2010
 */
public class ObjectJsonParser<T extends AlipayResponse> implements AlipayParser<T> {

    private Class<T> clazz;

    public ObjectJsonParser(Class<T> clazz) {
        this.clazz = clazz;
    }

    public T parse(String rsp) throws AlipayApiException {
        Converter converter = new JsonConverter();
//        rsp = rsp.replace("\"","");
//        rsp = rsp.substring(rsp.indexOf("{",2), rsp.indexOf("}")+1);
        rsp = rsp.substring(rsp.indexOf("{",2), rsp.lastIndexOf("}"));
        rsp = rsp.substring(0,rsp.lastIndexOf("}")+1);
        System.out.println("截取之后rsp:"+rsp);
        Gson gson = new Gson();
        return gson.fromJson(rsp, clazz);
//        return converter.toResponse(rsp, clazz);
    }

    public Class<T> getResponseClass() {
        return clazz;
    }

    /** 
     * @see AlipayParser#getSignItem(AlipayRequest, AlipayResponse)
     */
    public SignItem getSignItem(AlipayRequest<?> request, AlipayResponse response)
                                                                                  throws AlipayApiException {

        Converter converter = new JsonConverter();

        return converter.getSignItem(request, response);
    }

}
