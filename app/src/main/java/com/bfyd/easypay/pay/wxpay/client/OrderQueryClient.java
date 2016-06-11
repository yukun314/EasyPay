package com.bfyd.easypay.pay.wxpay.client;

import com.bfyd.easypay.pay.wxpay.WXConfigure;

/**
 * Created by zyk on 2016/6/11.
 */
public class OrderQueryClient extends DefaultWXpayClient {
    private static final String format = "json";
    private static final String charset = "UTF-8";
    private static final String alipayPulicKey = null;

    public OrderQueryClient() {
        super(WXConfigure.ORDERQUERY_API, format, charset);
    }
}
