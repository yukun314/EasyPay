package com.bfyd.easypay.pay.alipay.request;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by zyk on 2016/6/6.
 * 请求参数
 */
public class QueryBizContentEntity {

	//订单支付时传入的商户订单号,和支付宝交易号不能同时为空。 trade_no,out_trade_no如果同时存在优先取trade_no
	public String out_trade_no;

	//支付宝交易号，和商户订单号不能同时为空
	public String trade_no;
}
