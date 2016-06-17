package com.bfyd.easypay.pay.wxpay.response;

/**
 * Created by zyk on 2016/6/11.
 * 微信 查询订单 返回结果
 */
public class OrderQueryResponse extends WXPayResponse {

	//SUCCESS/FAIL
	public String result_code;//业务结果

	//错误码
	public String err_code;//错误代码

	//结果信息描述
	public String err_code_des;//错误代码描述

	/**以下字段在return_code 和result_code都为SUCCESS的时候有返回*/
	//微信支付分配的终端设备号，
	public String device_info;//设备号

	//用户在商户appid下的唯一标识
	public String openid;//用户标识

	//用户是否关注公众账号，Y-关注，N-未关注，仅在公众账号类型支付有效
	public String is_subscribe;//是否关注公众账号

	//调用接口提交的交易类型，取值如下：JSAPI，NATIVE，APP，MICROPAY，详细说明见参数规定
	public String trade_type;//交易类型

	//SUCCESS—支付成功  REFUND—转入退款 NOTPAY—未支付 CLOSED—已关闭
	// REVOKED—已撤销（刷卡支付） USERPAYING--用户支付中
	// PAYERROR--支付失败(其他原因，如银行返回失败)
	public String trade_state;//交易状态

	//银行类型，采用字符串类型的银行标识
	public String bank_type;//付款银行

	//订单总金额，单位为分
	public int total_fee;//订单金额

	//应结订单金额=订单金额-非充值代金券金额，应结订单金额<=订单金额。
	public int  settlement_total_fee;//应结订单金额

	//货币类型，符合ISO 4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型
	public String fee_type;//货币种类

	//现金支付金额订单现金支付金额，详见支付金额
	public int cash_fee;//现金支付金额

	//货币类型，符合ISO 4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型
	public String cash_fee_type;//现金支付货币类型

	//“代金券”金额<=订单金额，订单金额-“代金券”金额=现金支付金额，详见支付金额
	public int coupon_fee;//代金券金额

	//代金券批次ID ,$n为下标，从0开始编号
	public String coupon_batch_id_$n;//代金券批次ID

	//CASH--充值代金券


	//NO_CASH---非充值代金券
	//订单使用代金券时有返回（取值：CASH、NO_CASH）。$n为下标,从0开始编号，举例：coupon_type_$0
	public int coupon_type_$n;//代金券类型

	//代金券ID, $n为下标，从0开始编号
	public String coupon_id_$n;//代金券ID

	//单个代金券支付金额, $n为下标，从0开始编号
	public int coupon_fee_$n;//单个代金券支付金额

	//微信支付订单号
	public String transaction_id;//微信支付订单号

	//商户系统的订单号，与请求一致。
	public String out_trade_no;//商户订单号

	//附加数据，原样返回
	public String attach;//附加数据

	//订单支付时间，格式为yyyyMMddHHmmss，如2009年12月25日9点10分10秒表示为20091225091010。其他详见时间规则
	public String time_end;//支付完成时间

	//对当前查询订单状态的描述和下一步操作的指引
	public String trade_state_desc;//交易状态描述

	@Override
	public String toString() {
		return "OrderQueryResponse{" +
				"result_code='" + result_code + '\'' +
				", err_code='" + err_code + '\'' +
				", err_code_des='" + err_code_des + '\'' +
				", device_info='" + device_info + '\'' +
				", openid='" + openid + '\'' +
				", is_subscribe='" + is_subscribe + '\'' +
				", trade_type='" + trade_type + '\'' +
				", trade_state='" + trade_state + '\'' +
				", bank_type='" + bank_type + '\'' +
				", total_fee=" + total_fee +
				", settlement_total_fee=" + settlement_total_fee +
				", fee_type='" + fee_type + '\'' +
				", cash_fee=" + cash_fee +
				", cash_fee_type='" + cash_fee_type + '\'' +
				", coupon_fee=" + coupon_fee +
				", coupon_batch_id_$n='" + coupon_batch_id_$n + '\'' +
				", coupon_type_$n=" + coupon_type_$n +
				", coupon_id_$n='" + coupon_id_$n + '\'' +
				", coupon_fee_$n=" + coupon_fee_$n +
				", transaction_id='" + transaction_id + '\'' +
				", out_trade_no='" + out_trade_no + '\'' +
				", attach='" + attach + '\'' +
				", time_end='" + time_end + '\'' +
				", trade_state_desc='" + trade_state_desc + '\'' +
				'}';
	}
}
