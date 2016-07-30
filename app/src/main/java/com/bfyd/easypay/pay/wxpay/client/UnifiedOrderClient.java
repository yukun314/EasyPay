package com.bfyd.easypay.pay.wxpay.client;


import com.bfyd.easypay.pay.wxpay.WXConfigure;

/**
 * Created by zyk on 2016/6/8.
 */
public class UnifiedOrderClient extends DefaultWXpayClient {


	public UnifiedOrderClient() {
		super(WXConfigure.UNIFIEDORDER_API,  WXConfigure.format, WXConfigure.charset);
	}
}
