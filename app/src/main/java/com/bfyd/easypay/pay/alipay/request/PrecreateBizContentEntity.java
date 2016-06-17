package com.bfyd.easypay.pay.alipay.request;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by zyk on 2016/6/6.
 * 请求参数
 */
public class PrecreateBizContentEntity {
	//商户订单号,64个字符以内、只能包含字母、数字、下划线；需保证在商户端不重复
	@SerializedName("out_trade_no")
	public String out_trade_no;//必须

	//卖家支付宝用户ID。 如果该值为空，则默认为商户签约账号对应的支付宝用户ID
	@SerializedName("seller_id")
	public String seller_id;

	//	订单总金额，单位为元，精确到小数点后两位，取值范围[0.01,100000000]
	// 如果同时传入了【打折金额】，【不可打折金额】，【订单总金额】三者，则必须满足如下条件：【订单总金额】=【打折金额】+【不可打折金额】
	@SerializedName("total_amount")
	public float total_amount;//必须

	//可打折金额. 参与优惠计算的金额，单位为元，精确到小数点后两位，取值范围[0.01,100000000] 如果该值未传入，但传入了【订单总金额】，
	// 【不可打折金额】则该值默认为【订单总金额】-【不可打折金额】
	@SerializedName("discountable_amount")
	public float discountable_amount;

	//不可打折金额. 不参与优惠计算的金额，单位为元，精确到小数点后两位，取值范围[0.01,100000000]
	//如果该值未传入，但传入了【订单总金额】,【打折金额】，则该值默认为【订单总金额】-【打折金额】
	@SerializedName("undiscountable_amount")
	public float undiscountable_amount;

	//买家支付宝账号
	@SerializedName("buyer_logon_id")
	public String buyer_logon_id;

	//订单标题
	@SerializedName("subject")
	public String subject;//必须

	//对交易或商品的描述
	@SerializedName("body")
	public String body;

	//订单包含的商品列表信息.Json格式. 其它说明详见：“商品明细说明”
	@SerializedName("goods_detail")
	public List<PrecreateGoodsDetailEntity> goods_detail;

	//商户操作员编号
	@SerializedName("operator_id")
	public String operator_id;

	//商户门店编号
	@SerializedName("store_id")
	public String store_id;

	//商户机具终端编号
	@SerializedName("terminal_id")
	public String terminal_id;

	//业务扩展参数 Json格式
	@SerializedName("extend_params")
	public PrecreateExtendParamsEntity extend_params;

	//该笔订单允许的最晚付款时间，逾期将关闭交易。取值范围：1m～15d。
	// m-分钟，h-小时，d-天，1c-当天（1c-当天的情况下，无论交易何时创建，都在0点关闭）。 该参数数值不接受小数点， 如 1.5h，可转换为 90m。
	@SerializedName("timeout_express")
	public String timeout_express;

	//描述分账信息，json格式。
	@SerializedName("royalty_info")
	public PrecreateRoyaltyInfoEntity royalty_info;
}
