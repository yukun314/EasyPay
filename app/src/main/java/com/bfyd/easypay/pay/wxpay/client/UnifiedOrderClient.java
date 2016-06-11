package com.bfyd.easypay.pay.wxpay.client;


import com.bfyd.easypay.pay.wxpay.WXConfigure;

/**
 * Created by zyk on 2016/6/8.
 */
public class UnifiedOrderClient extends DefaultWXpayClient {

	private static final String format = "json";
	private static final String charset = "UTF-8";

	public UnifiedOrderClient() {
		super(WXConfigure.UNIFIEDORDER_API, format, charset);
	}
}
