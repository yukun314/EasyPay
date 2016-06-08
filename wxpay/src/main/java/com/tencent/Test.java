package com.tencent;

import java.util.HashMap;

/**
 * Created by zyk on 2016/6/6.
 */
public class Test {
	public static void main(String []args){
		HashMap<String, String> paramMap = new HashMap();
//		paramMap.put("trade_type", "NATIVE"); //交易类型
//		paramMap.put("spbill_create_ip",localIp()); //本机的Ip
//		paramMap.put("product_id", payOrderIdsStr); // 商户根据自己业务传递的参数 必填
//		paramMap.put("body", orderSubject);         //描述
//		paramMap.put("out_trade_no", payOrderIdsStr); //商户 后台的贸易单号
//		paramMap.put("total_fee", "" + totalCount); //金额必须为整数  单位为分
//		paramMap.put("notify_url", "http://" + getAccessDomain() + "/wx_pay_notify"); //支付成功后，回调地址
//		paramMap.put("appid", siteConfig.getWxPayAppId()); //appid
//		paramMap.put("mch_id", siteConfig.getWxPayMchId()); //商户号
//		paramMap.put("nonce_str", CommonUtilPub.createNoncestr(32));  //随机数
//		paramMap.put("sign",CommonUtilPub.getSign(paramMap,siteConfig.getWxPayPartnerKey()));//根据微信签名规则，生成签名
//		String xmlData = CommonUtilPub.mapToXml(paramMap);//把参数转换成XML数据格式
	}

//	/**
//	 2      * 获取本机Ip
//	 3      *
//	 4      *  通过 获取系统所有的networkInterface网络接口 然后遍历 每个网络下的InterfaceAddress组。
//	 5      *  获得符合 <code>InetAddress instanceof Inet4Address</code> 条件的一个IpV4地址
//	 6      * @return
//	 7      */
//	8     @SuppressWarnings("rawtypes")
//	9     private String localIp(){
//		10         String ip = null;
//		11         Enumeration allNetInterfaces;
//		12         try {
//			13             allNetInterfaces = NetworkInterface.getNetworkInterfaces();
//			14             while (allNetInterfaces.hasMoreElements()) {
//				15                 NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
//				16                 List<InterfaceAddress> InterfaceAddress = netInterface.getInterfaceAddresses();
//				17                 for (InterfaceAddress add : InterfaceAddress) {
//					18                     InetAddress Ip = add.getAddress();
//					19                     if (Ip != null && Ip instanceof Inet4Address) {
//						20                         ip = Ip.getHostAddress();
//						21                     }
//					22                 }
//				23             }
//			24         } catch (SocketException e) {
//			25             // TODO Auto-generated catch block
//			26             logger.warn("获取本机Ip失败:异常信息:"+e.getMessage());
//			27         }
//		28         return ip;
//		29     }
}
