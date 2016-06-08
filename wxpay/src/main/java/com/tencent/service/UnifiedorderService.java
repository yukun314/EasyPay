package com.tencent.service;

import com.tencent.common.Configure;
import com.tencent.protocol.pay_protocol.ScanPayReqData;
import com.tencent.protocol.unifiedorder.UnifiedorderReqData;

/**
 * Created by zyk on 2016/6/6.
 */
public class UnifiedorderService extends BaseService {
	public UnifiedorderService() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
		super(Configure.UNIFIEDORDER);
	}

	/**
	 * 统一下单
	 * @param unifiedorderReqData 这个数据对象里面包含了API要求提交的各种数据字段
	 * @return API返回的数据
	 * @throws Exception
	 */
	public String request(UnifiedorderReqData unifiedorderReqData) throws Exception {

		//--------------------------------------------------------------------
		//发送HTTPS的Post请求到API地址
		//--------------------------------------------------------------------
		String responseString = sendPost(unifiedorderReqData);

		return responseString;
	}
}
