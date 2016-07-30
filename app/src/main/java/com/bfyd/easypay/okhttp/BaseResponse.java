package com.bfyd.easypay.okhttp;

/**
 * Created by zyk on 2016/6/14.
 */
public class BaseResponse {

	//返回状态码
	public int returnCode;

	//返回信息
	public String returnMsg;

	//随机字符串
	public String nonceStr;

	//签名
	public String sign;

	//服务器返回的原数据
	public String body;
}
