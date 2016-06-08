package com.bfyd.easypay.alipay;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by zyk on 2016/6/6.
 * 描述分账信息，json格式。
 */
public class RoyaltyInfoEntity {
	//分账类型 卖家的分账类型，目前只支持传入ROYALTY（普通分账类型）。
	@SerializedName("royalty_type")
	public String royalty_type;
	//分账明细的信息，可以描述多条分账指令，json数组
	@SerializedName("royalty_detail_infos")
	public List<RoyaltyDetailInfosEntity> royalty_detail_infos;

}
