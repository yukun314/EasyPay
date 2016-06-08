package com.tencent.protocol.unifiedorder;

import com.tencent.common.Configure;
import com.tencent.common.RandomStringGenerator;
import com.tencent.common.Signature;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zyk on 2016/6/6.
 * 统一下单请求参数
 */
public class UnifiedorderReqData {
	//微信分配的公众账号ID（企业号corpid即为此appId）
	public String appid;//公众账号ID

	//微信支付分配的商户号
	public String mch_id;//商户号

	//终端设备号(门店号或收银设备ID)，注意：PC网页或公众号内支付请传"WEB"
	public String device_info;//设备号

	//随机字符串，不长于32位。推荐随机数生成算法
	public String nonce_str;//随机字符串

	//签名，详见签名生成算法
	public String sign;//签名

	//商品或支付单简要描述
	public String body;//商品描述

	//商品名称明细列表
	public String detail;//商品详情

	//附加数据，在查询API和支付通知中原样返回，该字段主要用于商户携带订单的自定义数据
	public String attach;//附加数据

	//商户系统内部的订单号,32个字符内、可包含字母, 其他说明见商户订单号
	public String out_trade_no;//商户订单号

	//符合ISO 4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型
	public String fee_type;//货币类型

	//订单总金额，单位为分，详见支付金额
	public int total_fee;//总金额

	//APP和网页支付提交用户端ip，Native支付填调用微信支付API的机器IP
	public String spbill_create_ip;//终端IP

	//订单生成时间，格式为yyyyMMddHHmmss，如2009年12月25日9点10分10秒表示为20091225091010。其他详见时间规则
	public String time_start;//交易起始时间

	//订单失效时间，格式为yyyyMMddHHmmss，如2009年12月27日9点10分10秒表示为20091227091010。其他详见时间规则
	// 注意：最短失效时间间隔必须大于5分钟
	public String time_expire;//交易结束时间

	//商品标记，代金券或立减优惠功能的参数，说明详见代金券或立减优惠
	public String goods_tag;//商品标记

	//接收微信支付异步通知回调地址，通知url必须为直接可访问的url，不能携带参数。
	public String notify_url;//通知地址

	//取值如下：JSAPI，NATIVE，APP，详细说明见参数规定
	public String trade_type;//交易类型

	//trade_type=NATIVE，此参数必传。此id为二维码中包含的商品ID，商户自行定义。
	public String product_id;//商品ID

	//no_credit--指定不能使用信用卡支付
	public String limit_pay;//指定支付方式

	//trade_type=JSAPI，此参数必传，用户在商户appid下的唯一标识。openid如何获取，
	// 可参考【获取openid】。企业号请使用【企业号OAuth2.0接口】获取企业号内成员userid，再调用【企业号userid转openid接口】进行转换
	public String openid;//用户标识

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getMch_id() {
		return mch_id;
	}

	public void setMch_id(String mch_id) {
		this.mch_id = mch_id;
	}

	public String getDevice_info() {
		return device_info;
	}

	public void setDevice_info(String device_info) {
		this.device_info = device_info;
	}

	public String getNonce_str() {
		return nonce_str;
	}

	public void setNonce_str(String nonce_str) {
		this.nonce_str = nonce_str;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getAttach() {
		return attach;
	}

	public void setAttach(String attach) {
		this.attach = attach;
	}

	public String getOut_trade_no() {
		return out_trade_no;
	}

	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}

	public String getFee_type() {
		return fee_type;
	}

	public void setFee_type(String fee_type) {
		this.fee_type = fee_type;
	}

	public int getTotal_fee() {
		return total_fee;
	}

	public void setTotal_fee(int total_fee) {
		this.total_fee = total_fee;
	}

	public String getSpbill_create_ip() {
		return spbill_create_ip;
	}

	public void setSpbill_create_ip(String spbill_create_ip) {
		this.spbill_create_ip = spbill_create_ip;
	}

	public String getTime_start() {
		return time_start;
	}

	public void setTime_start(String time_start) {
		this.time_start = time_start;
	}

	public String getTime_expire() {
		return time_expire;
	}

	public void setTime_expire(String time_expire) {
		this.time_expire = time_expire;
	}

	public String getGoods_tag() {
		return goods_tag;
	}

	public void setGoods_tag(String goods_tag) {
		this.goods_tag = goods_tag;
	}

	public String getNotify_url() {
		return notify_url;
	}

	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}

	public String getTrade_type() {
		return trade_type;
	}

	public void setTrade_type(String trade_type) {
		this.trade_type = trade_type;
	}

	public String getProduct_id() {
		return product_id;
	}

	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}

	public String getLimit_pay() {
		return limit_pay;
	}

	public void setLimit_pay(String limit_pay) {
		this.limit_pay = limit_pay;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	//FIXME 必须最后调用此方法
	public void a(){
		//微信分配的公众号ID（开通公众号之后可以获取到）
		setAppid(Configure.getAppid());

		//微信支付分配的商户号ID（开通公众号的微信支付功能之后可以获取到）
		setMch_id(Configure.getMchid());

		//随机字符串，不长于32 位
		setNonce_str(RandomStringGenerator.getRandomStringByLength(32));

		setSpbill_create_ip(Configure.getIP());
		//根据API给的签名规则进行签名
		String sign = Signature.getSign(toMap());
		setSign(sign);//把签名数据设置到Sign这个属性中
	}

	public Map<String,Object> toMap(){
		Map<String,Object> map = new HashMap<String, Object>();
		Field[] fields = this.getClass().getDeclaredFields();
		for (Field field : fields) {
			Object obj;
			try {
				obj = field.get(this);
				if(obj!=null){
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
