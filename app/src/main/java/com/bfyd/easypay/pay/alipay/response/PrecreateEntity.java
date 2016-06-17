package com.bfyd.easypay.pay.alipay.response;

/**
 * Created by zyk on 2016/6/13.
 */
public class PrecreateEntity {

	public String code;
	public String msg;
	public String sub_code;
	public String sub_msg;

	/**
	 * 商户的订单号
	 */
	public String out_trade_no;

	/**
	 * 当前预下单请求生成的二维码码串，可以用二维码生成工具根据该码串值生成对应的二维码
	 */
	public String qr_code;
}
