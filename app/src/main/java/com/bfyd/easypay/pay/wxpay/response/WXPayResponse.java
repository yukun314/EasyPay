package com.bfyd.easypay.pay.wxpay.response;

import java.io.Serializable;

/**
 * 微信 返回结果
 */
public abstract class WXPayResponse implements Serializable {

	private static final long serialVersionUID = 4822394695918885225L;

	//SUCCESS/FAIL
	// 此字段是通信标识，非交易标识，交易是否成功需要查看result_code来判断
	public String return_code;//返回状态码

	//返回信息，如非空，为错误原因
	// 签名失败 参数格式校验错误
	public String return_msg;//返回信息

	/**以下字段在return_code为SUCCESS的时候有返回*/

	//调用接口提交的公众账号ID
	public String appid;//公众账号ID

	//调用接口提交的商户号
	public String mch_id;//商户号

	//微信返回的随机字符串
	public String nonce_str;//随机字符串

	//微信返回的签名，详见签名算法
	public String sign;//签名


	@Override
	public String toString() {
		return "WXPayResponse{" +
				"return_code='" + return_code + '\'' +
				", return_msg='" + return_msg + '\'' +
				", appid='" + appid + '\'' +
				", mch_id='" + mch_id + '\'' +
				", nonce_str='" + nonce_str + '\'' +
				", sign='" + sign + '\'' +
				'}';
	}
}
