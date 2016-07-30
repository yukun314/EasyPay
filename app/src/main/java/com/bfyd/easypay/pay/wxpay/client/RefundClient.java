package com.bfyd.easypay.pay.wxpay.client;

import com.bfyd.easypay.pay.wxpay.WXConfigure;

/**
 * Created by zyk on 2016/7/19.
 */
public class RefundClient extends DefaultWXpayClient {

	public RefundClient() {
		super(WXConfigure.REFUND_API, WXConfigure.format, WXConfigure.charset);
	}

}
