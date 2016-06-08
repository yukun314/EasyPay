package com.alipay.api.internal.parser.xml;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayParser;
import com.alipay.api.AlipayRequest;
import com.alipay.api.AlipayResponse;
import com.alipay.api.SignItem;
import com.alipay.api.internal.mapping.Converter;

/**
 * 单个JSON对象解释器。
 * 
 * @author carver.gu
 * @since 1.0, Apr 11, 2010
 */
public class ObjectXmlParser<T extends AlipayResponse> implements AlipayParser<T> {

    private Class<T> clazz;

    public ObjectXmlParser(Class<T> clazz) {
        this.clazz = clazz;
    }

    public T parse(String rsp) throws AlipayApiException {
        Converter converter = new XmlConverter();
        return converter.toResponse(rsp, clazz);
    }

    public Class<T> getResponseClass() {
        return clazz;
    }

    /** 
     * @see AlipayParser#getSignItem(AlipayRequest, AlipayResponse)
     */
    public SignItem getSignItem(AlipayRequest<?> request, AlipayResponse response)
                                                                                  throws AlipayApiException {

        Converter converter = new XmlConverter();

        return converter.getSignItem(request, response);
    }

}
