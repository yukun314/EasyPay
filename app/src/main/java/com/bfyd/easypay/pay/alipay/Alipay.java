package com.bfyd.easypay.pay.alipay;


import com.alipay.api.DefaultAlipayClient;

/**
 * Created by zyk on 2016/6/8.
 */
public class Alipay extends DefaultAlipayClient {

	public Alipay() {
		super(AliConfigure.serverUrl, AliConfigure.appId, AliConfigure.privateKey, AliConfigure.format, AliConfigure.charset, AliConfigure.alipayPulicKey);
	}
}
