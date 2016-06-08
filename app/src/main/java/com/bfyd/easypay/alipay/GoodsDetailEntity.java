package com.bfyd.easypay.alipay;


import com.google.gson.annotations.SerializedName;

/**
 * Created by zyk on 2016/6/6.
 * 订单包含的商品列表信息(商品明细)
 */
public class GoodsDetailEntity {
    // 商品编号(国标)
    @SerializedName("goods_id")
    public String goods_id;//必须

    //支付宝定义的统一商品编号
    @SerializedName("alipay_goods_id")
    public String alipay_goods_id;

    // 商品名称
    @SerializedName("goods_name")
    public String goods_name;//必须

    // 商品数量
    @SerializedName("quantity")
    public int quantity;//必须

    // 商品价格，此处单位为元，精确到小数点后2位
    @SerializedName("price")
    public float price;//必须

    // 商品类别
    @SerializedName("goods_category")
    public String  goods_category;

    // 商品详情
    @SerializedName("body")
    public String body;

}
