package com.alipay.api.response;

import java.util.Date;
import java.util.List;
import com.alipay.api.internal.mapping.ApiField;
import com.alipay.api.internal.mapping.ApiListField;
import com.alipay.api.domain.TradeFundBill;

import com.alipay.api.AlipayResponse;

/**
 * ALIPAY API: alipay.trade.pay response.
 * 
 * @author auto create
 * @since 1.0, 2015-12-04 21:45:56
 */
public class AlipayTradePayResponse extends AlipayResponse {

	private static final long serialVersionUID = 8689391567652825129L;

	/** 
	 * 买家支付宝账号
	 */
	@ApiField("buyer_logon_id")
	public String buyerLogonId;

	/** 
	 * 买家付款的金额
	 */
	@ApiField("buyer_pay_amount")
	public String buyerPayAmount;

	/** 
	 * 买家在支付宝的用户id
	 */
	@ApiField("buyer_user_id")
	public String buyerUserId;

	/** 
	 * 支付宝卡余额
	 */
	@ApiField("card_balance")
	public String cardBalance;

	/** 
	 * 本次交易支付所使用的单品券优惠的商品优惠信息
	 */
	@ApiField("discount_goods_detail")
	public String discountGoodsDetail;

	/** 
	 * 交易支付使用的资金单据列表
	 */
	@ApiListField("fund_bill_list")
	@ApiField("trade_fund_bill")
	public List<TradeFundBill> fundBillList;

	/** 
	 * 交易支付时间
	 */
	@ApiField("gmt_payment")
	public Date gmtPayment;

	/** 
	 * 交易中可给用户开具发票的金额
	 */
	@ApiField("invoice_amount")
	public String invoiceAmount;

	/** 
	 * 买家支付宝用户号
	 */
	@ApiField("open_id")
	public String openId;

	/** 
	 * 商户订单号
	 */
	@ApiField("out_trade_no")
	public String outTradeNo;

	/** 
	 * 使用积分宝付款的金额
	 */
	@ApiField("point_amount")
	public String pointAmount;

	/** 
	 * 88.88
	 */
	@ApiField("receipt_amount")
	public String receiptAmount;

	/** 
	 * 发生支付交易的商户门店名称
	 */
	@ApiField("store_name")
	public String storeName;

	/** 
	 * 交易金额
	 */
	@ApiField("total_amount")
	public String totalAmount;

	/** 
	 * 支付宝交易号
	 */
	@ApiField("trade_no")
	public String tradeNo;

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

	public void setCardBalance(String cardBalance) {
		this.cardBalance = cardBalance;
	}
	public String getCardBalance( ) {
		return this.cardBalance;
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

	public void setGmtPayment(Date gmtPayment) {
		this.gmtPayment = gmtPayment;
	}
	public Date getGmtPayment( ) {
		return this.gmtPayment;
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

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	public String getStoreName( ) {
		return this.storeName;
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

	@Override
	public String toString() {
		return "AlipayTradePayResponse{" +
				"buyerLogonId='" + buyerLogonId + '\'' +
				", buyerPayAmount='" + buyerPayAmount + '\'' +
				", buyerUserId='" + buyerUserId + '\'' +
				", cardBalance='" + cardBalance + '\'' +
				", discountGoodsDetail='" + discountGoodsDetail + '\'' +
				", fundBillList=" + fundBillList +
				", gmtPayment=" + gmtPayment +
				", invoiceAmount='" + invoiceAmount + '\'' +
				", openId='" + openId + '\'' +
				", outTradeNo='" + outTradeNo + '\'' +
				", pointAmount='" + pointAmount + '\'' +
				", receiptAmount='" + receiptAmount + '\'' +
				", storeName='" + storeName + '\'' +
				", totalAmount='" + totalAmount + '\'' +
				", tradeNo='" + tradeNo + '\'' +
				'}';
	}
}
