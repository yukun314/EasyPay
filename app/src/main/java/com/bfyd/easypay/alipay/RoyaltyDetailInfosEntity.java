package com.bfyd.easypay.alipay;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zyk on 2016/6/6.
 */
public class RoyaltyDetailInfosEntity{
	//分账序列号，表示分账执行的顺序，必须为正整数
	@SerializedName("serial_no")
	public int serial_no;

	//接受分账金额的账户类型： 	userId：支付宝账号对应的支付宝唯一用户号。 	bankIndex：分账到银行账户的银行编号。目前暂时只支持分账到一个银行编号。
	// storeId：分账到门店对应的银行卡编号。 默认值为userId。
	@SerializedName("trans_in_typ")
	public String trans_in_typ;

	//分账批次号 分账批次号。 目前需要和转入账号类型为bankIndex配合使用
	@SerializedName("batch_no")
	public String batch_no; //必须

	//商户分账的外部关联号，用于关联到每一笔分账信息，商户需保证其唯一性。 如果为空，
	// 该值则默认为“商户网站唯一订单号+分账序列号”
	@SerializedName("out_relation_id")
	public String out_relation_id;

	//要分账的账户类型。 目前只支持userId：
	// 支付宝账号对应的支付宝唯一用户号。 默认值为userId。
	@SerializedName(" trans_out_type")
	public String  trans_out_type;//必须

	//如果转出账号类型为userId，本参数为要分账的支付宝账号对应的支付宝唯一用户号。以2088开头的纯16位数字。
	@SerializedName("trans_out")
	public String trans_out;//必须

	//如果转入账号类型为userId，本参数为接受分账金额的支付宝账号对应的支付宝唯一用户号。以2088开头的纯16位数字。 	如果转入账号类型为bankIndex，
	// 本参数为28位的银行编号（商户和支付宝签约时确定）。 如果转入账号类型为storeId，本参数为商户的门店ID。
	@SerializedName("trans_in")
	public String trans_in;//必须

	//分账的金额，单位为元
	@SerializedName("amount")
	public float amount;//必须

	//分账描述信息
	@SerializedName("desc")
	public String desc;
}