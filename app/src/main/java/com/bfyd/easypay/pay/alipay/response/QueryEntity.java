package com.bfyd.easypay.pay.alipay.response;

import com.alipay.api.domain.TradeFundBill;

/**
 * Created by zyk on 2016/6/13.
 */
public class QueryEntity {

	//支付宝交易号
	public String trade_no;

	//商家订单号
	public String out_trade_no;

	//买家支付宝用户号，该字段将废弃，不要使用
	public String open_id;

	//买家支付宝账号
	public String buyer_logon_id;

	//交易状态：WAIT_BUYER_PAY（交易创建，等待买家付款）、TRADE_CLOSED（未付款交易超时关闭，或支付完成后全额退款）、
	// TRADE_SUCCESS（交易支付成功）、TRADE_FINISHED（交易结束，不可退款）
	public String trade_status;

	//交易的订单金额，单位为元，两位小数。
	public String total_amount;

	//实收金额，单位为元，两位小数。
	public String receipt_amount;

	//买家实付金额，单位为元，两位小数。
	public float buyer_pay_amount;

	//积分支付的金额，单位为元，两位小数。
	public float point_amount;

	//交易中用户支付的可开具发票的金额，单位为元，两位小数。
	public float invoice_amount;

	//本次交易打款给卖家的时间	如 2014-11-27 15:45:57
	public String send_pay_date;

	//支付宝店铺编号
	public String alipay_store_id;

	//商户门店编号
	public String store_id;

	//商户机具终端编号
	public String terminal_id;

	//交易支付使用的资金渠道
	public TradeFundBill[] fund_bill_list;

	//请求交易支付中的商户店铺的名称
	public String store_name;

	//买家在支付宝的用户id
	public String buyer_user_id;

	//本次交易支付所使用的单品券优惠的商品优惠信息
	//[{"goods_id":"STANDARD1026181538","goods_name":"雪碧","discount_amount":"100.00","voucher_id":"2015102600073002039000002D5O"}]
	public String discount_goods_detail;

	//行业特殊信息（例如在医保卡支付业务中，向用户返回医疗信息）。
	//{"registration_order_pay":{"brlx":"1","cblx":"1"}}
	public String industry_sepc_detail;


}
