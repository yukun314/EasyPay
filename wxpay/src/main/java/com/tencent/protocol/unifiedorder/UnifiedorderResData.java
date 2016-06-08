package com.tencent.protocol.unifiedorder;

/**
 * Created by zyk on 2016/6/6.
 */
public class UnifiedorderResData {
	/**返回结果*/

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

	//调用接口提交的终端设备号，
	public String device_info;//设备号

	//微信返回的随机字符串
	public String nonce_str;//随机字符串

	//微信返回的签名，详见签名算法
	public String sign;//签名

	//SUCCESS/FAIL
	public String result_code;//业务结果

	//
	public String err_code;//错误代码

	//错误返回的信息描述
	public String err_code_des;//错误代码描述

	/**以下字段在return_code 和result_code都为SUCCESS的时候有返回 */

	//调用接口提交的交易类型
	/*JSAPI--公众号支付、NATIVE--原生扫码支付、APP--app支付，统一下单接口trade_type的传参可参考这里
	MICROPAY--刷卡支付，刷卡支付有单独的支付接口，不调用统一下单接口*/
	public String trade_type;//交易类型

	//微信生成的预支付回话标识，用于后续接口调用中使用，该值有效期为2小时
	public String prepay_id;//预支付交易会话标识

	//trade_type为NATIVE是有返回，可将该参数值生成二维码展示出来进行扫码支付
	public String code_url;//二维码链接
}
