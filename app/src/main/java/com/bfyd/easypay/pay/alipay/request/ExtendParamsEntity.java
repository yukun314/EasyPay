package com.bfyd.easypay.pay.alipay.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zyk on 2016/6/6.
 * 业务扩展参数
 */
public class ExtendParamsEntity {

	//系统商编号 该参数作为系统商返佣数据提取的依据，请填写系统商签约协议的PID
	@SerializedName("sys_service_provider_id")
	public String sys_service_provider_id;

	//使用花呗分期要进行的分期数
	@SerializedName("hb_fq_num")
	public String hb_fq_num;

	//该笔订单允许的最晚付款时间，逾期将关闭交易。取值范围：1m～15d。m-分钟，h-小时，d-天，1c-当天（1c-当天的情况下，无论交易何时创建，都在0点关闭）。
	// 该参数数值不接受小数点， 如 1.5h，可转换为 90m。
	@SerializedName("timeout_express")
	public String timeout_express;
}
