package com.bfyd.easypay.pay.wxpay.request;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * 微信 请求参数
 */
public abstract class WXPayRequest implements Serializable {

	public abstract void setSign(String sign);

	public abstract Map<String, Object> toMap();

}
