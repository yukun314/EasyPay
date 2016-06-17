package com.bfyd.easypay.okhttp;

/**
 * Created by zyk on 2016/6/14.
 */
public interface BaseRequest<T extends BaseResponse>{

	/**
	 * 得到当前API的响应结果类型
	 * @return 响应类型
	 */
	public Class<T> getResponseClass();

	public void setSign(String sign);

}
