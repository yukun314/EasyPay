package com.bfyd.easypay.pay.alipay.response;

/**
 * Created by zyk on 2016/6/13.
 * 扫码支付 异步通知
 */
public class PrecreateNotifyMessageEntity {

	//通知的发送时间。格式为yyyy-MM-dd HH:mm:ss
	public String notify_time;//通知时间

	//通知的类型
	public String notify_type;//通知类型

	//通知校验ID
	public String notify_id;//通知校验ID

	//签名算法类型，目前支持RSA
	public String sign_type;//签名类型

	//请参考异步返回结果的验签
	public String sign;//签名

	//支付宝交易凭证号
	public String trade_no;//支付宝交易号

	//支付宝分配给开发者的应用Id
	public String app_id;//开发者的app_id

	//原支付请求的商户订单号
	public String out_trade_no;//商户订单号

	//商户业务ID，主要是退款通知中返回退款申请的流水号
	public String out_biz_no;//商户业务号

	//买家支付宝账号对应的支付宝唯一用户号。以2088开头的纯16位数字
	public String buyer_id;//买家支付宝用户号

	//买家支付宝账号
	public String buyer_logon_id;//买家支付宝账号

	//卖家支付宝用户号
	public String seller_id;//卖家支付宝用户号

	//卖家支付宝账号
	public String seller_email;//卖家支付宝账号

	//交易目前所处的状态
	public String trade_status;//交易状态

	//本次交易支付的订单金额，单位为人民币（元）
	public float total_amount;//订单金额

	//商家在交易中实际收到的款项，单位为元
	public float receipt_amount;//实收金额

	//用户在交易中支付的可开发票的金额
	public float invoice_amount;//开票金额

	//用户在交易中支付的金额
	public float buyer_pay_amount;//付款金额

	//使用集分宝支付的金额
	public float point_amount;//集分宝金额

	//退款通知中，返回总退款金额，单位为元，支持两位小数
	public float refund_fee;//总退款金额

	//商户实际退款给用户的金额，单位为元，支持两位小数
	public float send_back_fee;//实际退款金额

	//商品的标题/交易标题/订单标题/订单关键字等，是请求时对应的参数，原样通知回来
	public String subject;//订单标题

	//该订单的备注、描述、明细等。对应请求时的body参数，原样通知回来
	public String body;//商品描述

	//该笔交易创建的时间。格式为yyyy-MM-dd HH:mm:ss
	public String gmt_create;//交易创建时间

	//该笔交易的买家付款时间。格式为yyyy-MM-dd HH:mm:ss
	public String gmt_payment;//交易付款时间

	//该笔交易的退款时间。格式为yyyy-MM-dd HH:mm:ss
	public String gmt_refund;//交易退款时间

	//该笔交易结束时间。格式为yyyy-MM-dd HH:mm:ss
	public String gmt_close;//交易结束时间

	//支付成功的各个渠道金额信息，详见资金明细信息说明
	public String fund_bill_list;//支付金额信息
}
