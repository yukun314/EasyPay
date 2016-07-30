package com.bfyd.easypay.pay.wxpay.response;

/**
 * Created by zyk on 2016/6/20.
 */
public class MicropayResponse extends WXPayResponse  {
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
	//用户在商户appid 下的唯一标识
	public String openid;//用户标识

	//用户是否关注公众账号，仅在公众账号类型支付有效，取值范围：Y或N;Y-关注;N-未关注
	public String is_subscribe;//是否关注公众账号

	//支付类型为MICROPAY(即扫码支付)
	public String trade_type;//交易类型

	//银行类型，采用字符串类型的银行标识，值列表详见银行类型
	public String bank_type;//付款银行

	//符合ISO 4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型
	public String fee_type;//货币类型

	//订单总金额，单位为分，只能为整数，详见支付金额
	public int total_fee;//订单金额

	//符合ISO 4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型
	public String cash_fee_type;//现金支付货币类型

	//订单现金支付金额，详见支付金额
	public int cash_fee;//现金支付金额

	//微信支付订单号
	public String transaction_id;//微信支付订单号

	//商户系统的订单号，与请求一致。
	public String out_trade_no;//商户订单号

	//商家数据包，原样返回
	public String attach;//商家数据包

	//订单生成时间，格式为yyyyMMddHHmmss，如2009年12月25日9点10分10秒表示为20091225091010。详见时间规则
	public String time_end;//支付完成时间

}
