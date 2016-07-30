package com.bfyd.easypay.okhttp.response;

import com.bfyd.easypay.okhttp.BaseResponse;

/**
 * Created by zyk on 2016/7/20.
 */
public class RefundResponse extends BaseResponse {

	public String payType;

	//微信或支付宝返回结果的json格式
	public String data;
}
