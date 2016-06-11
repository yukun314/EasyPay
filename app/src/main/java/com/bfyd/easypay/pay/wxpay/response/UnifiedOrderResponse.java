package com.bfyd.easypay.pay.wxpay.response;

/**
 * Created by zyk on 2016/6/6.
 * 微信 统一下单 返回结果
 */
public class UnifiedOrderResponse extends WXPayResponse {

	/**以下字段在return_code为SUCCESS的时候有返回*/
	//调用接口提交的终端设备号，
	public String device_info;//设备号


	//SUCCESS/FAIL
	public String result_code;//业务结果

	//
	public String err_code;//错误代码

	//错误返回的信息描述
	public String err_code_des;//错误代码描述

	/**以下字段在return_code 和result_code都为SUCCESS的时候有返回 */

	//调用接口提交的交易类型
	/*JSAPI--公众号支付、NATIVE--原生扫码支付、APP--app支付，统一下单接口trade_type的传参可参考这里
	MICROPAY--刷卡支付，刷卡支付有单独的支付接口，不调用统一下单接口*/
	public String trade_type;//交易类型

	//微信生成的预支付回话标识，用于后续接口调用中使用，该值有效期为2小时
	public String prepay_id;//预支付交易会话标识

	//trade_type为NATIVE是有返回，可将该参数值生成二维码展示出来进行扫码支付
	//code_url有效期为2小时
	public String code_url;//二维码链接

	@Override
	public String toString() {
		String str = super.toString()+"\n";
		str+="{" +
				"appid='" + appid + '\'' +
				", mch_id='" + mch_id + '\'' +
				", device_info='" + device_info + '\'' +
				", nonce_str='" + nonce_str + '\'' +
				", sign='" + sign + '\'' +
				", result_code='" + result_code + '\'' +
				", err_code='" + err_code + '\'' +
				", err_code_des='" + err_code_des + '\'' +
				", trade_type='" + trade_type + '\'' +
				", prepay_id='" + prepay_id + '\'' +
				", code_url='" + code_url + '\'' +
				'}';
		return str;
	}
}
