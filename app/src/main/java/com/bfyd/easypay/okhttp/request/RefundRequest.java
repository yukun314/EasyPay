package com.bfyd.easypay.okhttp.request;

import android.content.Context;

import com.bfyd.easypay.okhttp.BaseRequest;
import com.bfyd.easypay.okhttp.response.RefundResponse;
import com.bfyd.easypay.utils.StringUtils;
import com.bfyd.easypay.utils.Utils;

/**
 * Created by zyk on 2016/7/20.
 */
public class RefundRequest implements BaseRequest<RefundResponse> {

	public String deviceNo;

	public String method;

	public String nonceStr;

	public String sign;

	public String orderNo;

	public String payType;

	public int totalFee;

	public int refundFee;

	public String userId;

	public RefundRequest(Context context){
		method = "refund";
		deviceNo = Utils.getDeviceId(context);
		nonceStr = StringUtils.getRandomStringByLength(32);
	}

	@Override
	public Class<RefundResponse> getResponseClass() {
		return RefundResponse.class;
	}

	@Override
	public void setSign(String sign) {
		this.sign = sign;
	}

	@Override
	public String toString() {
		return "RefundRequest{" +
				"deviceNo='" + deviceNo + '\'' +
				", method='" + method + '\'' +
				", nonceStr='" + nonceStr + '\'' +
				", sign='" + sign + '\'' +
				", orderNo='" + orderNo + '\'' +
				", payType='" + payType + '\'' +
				", totalFee=" + totalFee +
				", refundFee=" + refundFee +
				'}';
	}
}
