package com.bfyd.easypay.pay.wxpay.response;

/**
 * Created by zyk on 2016/7/19.
 */
public class RefundResponse extends WXPayResponse {

	//SUCCESS/FAIL  SUCCESS退款申请接收成功，结果通过退款查询接口查询 FAIL 提交业务失败
	public String result_code;//业务结果

	//列表详见错误码列表
	public String err_code;//错误代码

	//结果信息描述
	public String err_code_des;//错误代码描述

	//商户系统内部的订单号
	public String transaction_id;//微信订单号

	//商户系统内部的订单号
	public String out_trade_no;//商户订单号

	//商户退款单号
	public String out_refund_no;//商户退款单号

	//微信退款单号
	public String refund_id;//微信退款单号

	//ORIGINAL—原路退款  BALANCE—退回到余额
	public String refund_channel;//退款渠道

	//退款总金额,单位为分,可以做部分退款
	public int refund_fee;//申请退款金额

	//退款金额=申请退款金额-非充值代金券退款金额，退款金额<=申请退款金额
	public int settlement_refund_fee_$n;//退款金额

	//订单总金额，单位为分，只能为整数，详见支付金额
	public int total_fee;//订单金额

	//应结订单金额=订单金额-非充值代金券金额，应结订单金额<=订单金额。
	public int settlement_total_fee;//应结订单金额

	//订单金额货币类型，符合ISO 4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型
	public String fee_type;//订单金额货币种类

	//现金支付金额，单位为分，只能为整数，详见支付金额
	public int cash_fee;//现金支付金额

	//现金退款金额，单位为分，只能为整数，详见支付金额
	public int cash_refund_fee;//现金退款金额

	//FIXME 应该不能转换成GSON  $n 下面的也是如此
	//CASH--充值代金券  NO_CASH---非充值代金券 订单使用代金券时有返回（取值：CASH、NO_CASH）。$n为下标,从0开始编号，举例：coupon_type_$0
	public String coupon_type_$n;//代金券类型

	//代金券退款金额<=退款金额，退款金额-代金券或立减优惠退款金额为现金，说明详见代金券或立减优惠
	public int coupon_refund_fee_$n;//代金券退款金额

	//退款代金券使用数量 ,$n为下标,从0开始编号
	public int coupon_refund_count_$n;//退款代金券使用数量

	//退款代金券批次ID ,$n为下标，$m为下标，从0开始编号
	public String coupon_refund_batch_id_$n_$m;//退款代金券批次ID

	//退款代金券ID, $n为下标，$m为下标，从0开始编号
	public String 退款代金券ID;//退款代金券ID

	//单个退款代金券支付金额, $n为下标，$m为下标，从0开始编号
	public int coupon_refund_fee_$n_$m;//单个退款代金券支付金额

}
