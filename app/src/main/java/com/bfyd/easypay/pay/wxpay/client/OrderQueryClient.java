package com.bfyd.easypay.pay.wxpay.client;

import com.bfyd.easypay.pay.wxpay.WXConfigure;

/**
 * Created by zyk on 2016/6/11.
 */
public class OrderQueryClient extends DefaultWXpayClient {

    public OrderQueryClient() {
        super(WXConfigure.ORDERQUERY_API,  WXConfigure.format, WXConfigure.charset);
    }
}
