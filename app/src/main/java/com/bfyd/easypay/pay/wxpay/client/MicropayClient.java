package com.bfyd.easypay.pay.wxpay.client;

import com.bfyd.easypay.pay.wxpay.WXConfigure;

/**
 * Created by zyk on 2016/6/20.
 *
 */
public class MicropayClient extends DefaultWXpayClient {

	public MicropayClient() {
		super(WXConfigure.PAY_API,  WXConfigure.format, WXConfigure.charset);
	}
}
