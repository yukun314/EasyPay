package com.bfyd.easypay.okhttp.request;

import android.content.Context;

import com.bfyd.easypay.okhttp.BaseRequest;
import com.bfyd.easypay.okhttp.response.LoginResponse;
import com.bfyd.easypay.utils.MD5;
import com.bfyd.easypay.utils.StringUtils;
import com.bfyd.easypay.utils.Utils;

/**
 * Created by zyk on 2016/6/15.
 */
public class LoginRequest implements BaseRequest<LoginResponse> {

	public String deviceNo;

	public String method;

	public String nonceStr;

	public String sign;

	public String userName;

	private String password;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = MD5.md5(password);
	}

	public LoginRequest(Context context){
		method = "login";
		deviceNo = Utils.getDeviceId(context);
		nonceStr = StringUtils.getRandomStringByLength(32);
	}

	@Override
	public Class getResponseClass() {
		return LoginResponse.class;
	}

	@Override
	public void setSign(String sign) {
		this.sign = sign;
	}

	@Override
	public String toString() {
		return "LoginRequest{" +
				"deviceNo='" + deviceNo + '\'' +
				", method='" + method + '\'' +
				", nonceStr='" + nonceStr + '\'' +
				", sign='" + sign + '\'' +
				", userName='" + userName + '\'' +
				", password='" + password + '\'' +
				'}';
	}
}
