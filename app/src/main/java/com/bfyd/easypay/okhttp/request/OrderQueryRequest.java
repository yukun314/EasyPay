package com.bfyd.easypay.okhttp.request;

import android.content.Context;

import com.bfyd.easypay.okhttp.BaseRequest;
import com.bfyd.easypay.okhttp.response.OrderQueryResponse;
import com.bfyd.easypay.utils.StringUtils;
import com.bfyd.easypay.utils.Utils;

/**
 * Created by zyk on 2016/7/20.
 */
public class OrderQueryRequest  implements BaseRequest<OrderQueryResponse> {

	public String deviceNo;

	public String method;

	public String nonceStr;

	public String sign;

	public String orderNo;

	public OrderQueryRequest(Context context){
		method = "query";
		deviceNo = Utils.getDeviceId(context);
		nonceStr = StringUtils.getRandomStringByLength(32);
	}

	@Override
	public Class<OrderQueryResponse> getResponseClass() {
		return OrderQueryResponse.class;
	}

	@Override
	public void setSign(String sign) {
		this.sign = sign;
	}
}
