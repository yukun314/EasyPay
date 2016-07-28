package com.bfyd.easypay.okhttp.request;

import android.content.Context;

import com.bfyd.easypay.okhttp.BaseRequest;
import com.bfyd.easypay.okhttp.response.SaveOrderResponse;
import com.bfyd.easypay.utils.StringUtils;
import com.bfyd.easypay.utils.Utils;

/**
 * Created by zyk on 2016/7/19.
 */
public class SaveOrderRequest implements BaseRequest<SaveOrderResponse> {

	public String deviceNo;

	public String method;

	public String nonceStr;

	public String sign;

	public String payType;//微信:wx  支付宝:zfb

	public String orderNo;

	public String appId;

	public String mchId;

	public int totalFee;

	public long payTime;

	public long refundTime;

	public int orderStatus;

	public SaveOrderRequest(Context context){
			method = "saveOrder";
			deviceNo = Utils.getDeviceId(context);
			nonceStr = StringUtils.getRandomStringByLength(32);
	}

	@Override
	public Class<SaveOrderResponse> getResponseClass() {
		return SaveOrderResponse.class;
	}

	@Override
	public void setSign(String sign) {
		this.sign = sign;
	}

	@Override
	public String toString() {
		return "SaveOrderRequest{" +
				"deviceNo='" + deviceNo + '\'' +
				", method='" + method + '\'' +
				", nonceStr='" + nonceStr + '\'' +
				", sign='" + sign + '\'' +
				", payType='" + payType + '\'' +
				", orderNo='" + orderNo + '\'' +
				", appId='" + appId + '\'' +
				", mchId='" + mchId + '\'' +
				", totalFee=" + totalFee +
				", payTime=" + payTime +
				", refundTime=" + refundTime +
				", orderStatus=" + orderStatus +
				'}';
	}
}
