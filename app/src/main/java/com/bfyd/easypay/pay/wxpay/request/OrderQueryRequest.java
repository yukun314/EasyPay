package com.bfyd.easypay.pay.wxpay.request;

import android.content.Context;

import com.bfyd.easypay.pay.wxpay.WXConfigure;
import com.bfyd.easypay.utils.StringUtils;
import com.bfyd.easypay.utils.Utils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zyk on 2016/6/11.
 * 微信 查询订单 请求参数
 */
public class OrderQueryRequest extends WXPayRequest {

    //微信分配的公众账号ID（企业号corpid即为此appId）
    public String appid;//公众账号ID

    //微信支付分配的商户号
    public String mch_id;//商户号
    //终端设备号
    public String device_info;//设备号

    //随机字符串，不长于32位。推荐随机数生成算法
    public String nonce_str;//随机字符串

    //微信生成的订单号，在支付通知中有返回
    public String transaction_id;//微信订单号

    //商户侧传给微信的订单号
    public String out_trade_no;//商户订单号

    //商户系统内部的退款单号，商户系统内部唯一，同一退款单号多次请求只退一笔
    public String out_refund_no;//商户退款单号

    //订单总金额，单位为分，只能为整数，详见支付金额
    public int total_fee;//总金额

    //退款总金额，订单总金额，单位为分，只能为整数，详见支付金额
    public int refund_fee;//退款金额

    //货币类型，符合ISO 4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型
    public String refund_fee_type;//货币种类

    //操作员帐号, 默认为商户号
    public String op_user_id;//操作员

    public OrderQueryRequest(Context context) {
        //微信分配的公众号ID（开通公众号之后可以获取到）
        this.appid = WXConfigure.appID;
        //微信支付分配的商户号ID（开通公众号的微信支付功能之后可以获取到）
        this.mch_id = WXConfigure.mchID;
        //随机字符串，不长于32 位
        this.nonce_str = StringUtils.getRandomStringByLength(32);
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<String, Object>();
        Field[] fields1 = this.getClass().getDeclaredFields();
        Field[] fields2 = super.getClass().getDeclaredFields();
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
