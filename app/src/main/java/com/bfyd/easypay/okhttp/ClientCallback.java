package com.bfyd.easypay.okhttp;

/**
 * Created by zyk on 2016/6/14.
 */
public interface ClientCallback<T extends BaseResponse> {
	public void onResponse(T response);
}
