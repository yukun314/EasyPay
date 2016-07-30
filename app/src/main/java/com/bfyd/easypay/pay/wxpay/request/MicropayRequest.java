package com.bfyd.easypay.pay.wxpay.request;

import android.content.Context;

import com.bfyd.easypay.pay.wxpay.WXConfigure;
import com.bfyd.easypay.utils.NetworkUtils;
import com.bfyd.easypay.utils.StringUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zyk on 2016/6/20.
 */
public class MicropayRequest extends WXPayRequest {

	//微信分配的公众账号ID（企业号corpid即为此appId）
	public String appid;//公众账号ID

	//微信支付分配的商户号
	public String mch_id;//商户号

	//随机字符串，不长于32位。推荐随机数生成算法
	public String nonce_str;//随机字符串

	//签名
	public String sign;

	//终端设备号(商户自定义，如门店编号)
	public String device_info;//设备号

	//商品或支付单简要描述
	public String body;//商品描述

	//商品名称明细列表
	public String detail;//商品详情

	//附加数据，在查询API和支付通知中原样返回，该字段主要用于商户携带订单的自定义数据
	public String attach;//附加数据

	//商户系统内部的订单号,32个字符内、可包含字母,其他说明见商户订单号
	public String out_trade_no;//商户订单号

	//订单总金额，单位为分，只能为整数，详见支付金额
	public int total_fee;//订单金额

	//符合ISO4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型
	public String fee_type;//货币类型

	//调用微信支付API的机器IP
	public String spbill_create_ip;//终端IP

	//商品标记，代金券或立减优惠功能的参数，说明详见代金券或立减优惠
	public String goods_tag;//商品标记

	//no_credit--指定不能使用信用卡支付
	public String limit_pay;//指定支付方式

	//扫码支付授权码，设备读取用户微信中的条码或者二维码信息
	public String auth_code;//授权码

	public MicropayRequest(Context context){
		//微信分配的公众号ID（开通公众号之后可以获取到）
		this.appid = WXConfigure.appID;
		//微信支付分配的商户号ID（开通公众号的微信支付功能之后可以获取到）
		this.mch_id = WXConfigure.mchID;
		//随机字符串，不长于32 位
		this.nonce_str = StringUtils.getRandomStringByLength(32);
		this.spbill_create_ip = NetworkUtils.GetIp(context);
	}

	@Override
	public void setSign(String sign) {
		this.sign = sign;
	}

	@Override
	public Map<String, Object> toMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		Field[] fields1 = this.getClass().getDeclaredFields();
		for (Field field : fields1) {
			Object obj;
			try {
				obj = field.get(this);
				if (obj != null) {
					map.put(field.getName(), obj);
				}

			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return map;
	}
}
