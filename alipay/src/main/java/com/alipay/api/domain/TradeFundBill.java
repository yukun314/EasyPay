package com.alipay.api.domain;

import com.alipay.api.AlipayObject;
import com.alipay.api.internal.mapping.ApiField;

/**
 * 交易支付的渠道属性
 *
 * @author auto create
 * @since 1.0, 2015-05-13 12:12:24
 */
public class TradeFundBill extends AlipayObject {

	private static final long serialVersionUID = 6283572989577824143L;

	/**
	 * 该支付工具类型所使用的金额
	 */
	@ApiField("amount")
	private String amount;

	/**
	 * 支付所使用的渠道
	 */
	@ApiField("fund_channel")
	private String fundChannel;

	public String getAmount() {
		return this.amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getFundChannel() {
		return this.fundChannel;
	}
	public void setFundChannel(String fundChannel) {
		this.fundChannel = fundChannel;
	}

}
