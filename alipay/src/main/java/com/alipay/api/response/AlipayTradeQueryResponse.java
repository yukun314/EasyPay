package com.alipay.api.response;

import java.util.Date;
import java.util.List;
import com.alipay.api.internal.mapping.ApiField;
import com.alipay.api.internal.mapping.ApiListField;
import com.alipay.api.domain.TradeFundBill;

import com.alipay.api.AlipayResponse;

/**
 * ALIPAY API: alipay.trade.query response.
 * 
 * @author auto create
 * @since 1.0, 2015-12-04 21:45:51
 */
public class AlipayTradeQueryResponse extends AlipayResponse {

	private static final long serialVersionUID = 2225387452523525464L;

	/** 
	 * 支付宝店铺编号
	 */
	@ApiField("alipay_store_id")
	private String alipayStoreId;

	/** 
	 * 买家支付宝账号
	 */
	@ApiField("buyer_logon_id")
	private String buyerLogonId;

	/** 
	 * 买家实付金额
	 */
	@ApiField("buyer_pay_amount")
	private String buyerPayAmount;

	/** 
	 * 买家在支付宝的用户id
	 */
	@ApiField("buyer_user_id")
	private String buyerUserId;

	/** 
	 * 本次交易支付所使用的单品券优惠的商品优惠信息
	 */
	@ApiField("discount_goods_detail")
	private String discountGoodsDetail;

	/** 
	 * 资金单据信息的集合
	 */
	@ApiListField("fund_bill_list")
	@ApiField("trade_fund_bill")
	private List<TradeFundBill> fundBillList;

	/** 
	 * 交易中用户支付的可开具发票的金额
	 */
	@ApiField("invoice_amount")
	private String invoiceAmount;

	/** 
	 * 买家支付宝用户号
	 */
	@ApiField("open_id")
	private String openId;

	/** 
	 * 商家订单号
	 */
	@ApiField("out_trade_no")
	private String outTradeNo;

	/** 
	 * 积分支付的金额
	 */
	@ApiField("point_amount")
	private String pointAmount;

	/** 
	 * 实收金额
	 */
	@ApiField("receipt_amount")
	private String receiptAmount;

	/** 
	 * 本次交易打款给卖家的时间
	 */
	@ApiField("send_pay_date")
	private Date sendPayDate;

	/** 
	 * 商户门店编号
	 */
	@ApiField("store_id")
	private String storeId;

	/** 
	 * 请求交易支付中的商户店铺的名称
	 */
	@ApiField("store_name")
	private String storeName;

	/** 
	 * 商户机具终端编号
	 */
	@ApiField("terminal_id")
	private String terminalId;

	/** 
	 * 交易的订单金额
	 */
	@ApiField("total_amount")
	private String totalAmount;

	/** 
	 * 支付宝交易号
	 */
	@ApiField("trade_no")
	private String tradeNo;

	/** 
	 * 交易状态
	 */
	@ApiField("trade_status")
	private String tradeStatus;

	public void setAlipayStoreId(String alipayStoreId) {
		this.alipayStoreId = alipayStoreId;
	}
	public String getAlipayStoreId( ) {
		return this.alipayStoreId;
	}

	public void setBuyerLogonId(String buyerLogonId) {
		this.buyerLogonId = buyerLogonId;
	}
	public String getBuyerLogonId( ) {
		return this.buyerLogonId;
	}

	public void setBuyerPayAmount(String buyerPayAmount) {
		this.buyerPayAmount = buyerPayAmount;
	}
	public String getBuyerPayAmount( ) {
		return this.buyerPayAmount;
	}

	public void setBuyerUserId(String buyerUserId) {
		this.buyerUserId = buyerUserId;
	}
	public String getBuyerUserId( ) {
		return this.buyerUserId;
	}

	public void setDiscountGoodsDetail(String discountGoodsDetail) {
		this.discountGoodsDetail = discountGoodsDetail;
	}
	public String getDiscountGoodsDetail( ) {
		return this.discountGoodsDetail;
	}

	public void setFundBillList(List<TradeFundBill> fundBillList) {
		this.fundBillList = fundBillList;
	}
	public List<TradeFundBill> getFundBillList( ) {
		return this.fundBillList;
	}

	public void setInvoiceAmount(String invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
	}
	public String getInvoiceAmount( ) {
		return this.invoiceAmount;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public String getOpenId( ) {
		return this.openId;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}
	public String getOutTradeNo( ) {
		return this.outTradeNo;
	}

	public void setPointAmount(String pointAmount) {
		this.pointAmount = pointAmount;
	}
	public String getPointAmount( ) {
		return this.pointAmount;
	}

	public void setReceiptAmount(String receiptAmount) {
		this.receiptAmount = receiptAmount;
	}
	public String getReceiptAmount( ) {
		return this.receiptAmount;
	}

	public void setSendPayDate(Date sendPayDate) {
		this.sendPayDate = sendPayDate;
	}
	public Date getSendPayDate( ) {
		return this.sendPayDate;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}
	public String getStoreId( ) {
		return this.storeId;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	public String getStoreName( ) {
		return this.storeName;
	}

	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}
	public String getTerminalId( ) {
		return this.terminalId;
	}

	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}
	public String getTotalAmount( ) {
		return this.totalAmount;
	}

	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}
	public String getTradeNo( ) {
		return this.tradeNo;
	}

	public void setTradeStatus(String tradeStatus) {
		this.tradeStatus = tradeStatus;
	}
	public String getTradeStatus( ) {
		return this.tradeStatus;
	}

}
