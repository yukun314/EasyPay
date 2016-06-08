package com.bfyd.easypay.alipay;


import com.alipay.api.DefaultAlipayClient;

/**
 * Created by zyk on 2016/6/8.
 */
public class WXPay extends DefaultAlipayClient {

	private static final String serverUrl = "https://api.mch.weixin.qq.com/pay/unifiedorder";
	private static final String appId = "wx12da9dff1cc33e4e";
	//FIXME
	private static String mchID = "1268555101";
	private static String key = "ef796697130e28ac1820d98ac681fbb6";

	private static final String privateKey = "";

	private static final String format = "json";
	private static final String charset = "UTF-8";
	private static final String alipayPulicKey = null;

	public WXPay() {
		super(serverUrl, appId, privateKey, format, charset, alipayPulicKey);
	}
}
