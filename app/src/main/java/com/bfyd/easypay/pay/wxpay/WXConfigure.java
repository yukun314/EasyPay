package com.bfyd.easypay.pay.wxpay;

/**
 * 这里放置各种配置数据
 */
public class WXConfigure {
//这个就是自己要保管好的私有Key了（切记只能放在自己的后台代码里，不能放在任何可能被看到源代码的客户端程序中）
	// 每次自己Post数据给API的时候都要用这个key来对所有字段进行签名，生成的签名会放在Sign这个字段，API收到Post数据的时候也会用同样的签名算法对Post过来的数据进行签名和验证
	// 收到API的返回的时候也要用这个key来对返回的数据算下签名，跟API的Sign数据进行比较，如果值不一致，有可能数据被第三方给篡改

	public static String key = "qwertyuiopasdfghjklzxcvbnmqwerty";//qwertyuiopasdfghjklzxcvbnm123456   qwertyuiopasdfghjklzxcvbnmqwerty

	//微信分配的公众号ID（开通公众号之后可以获取到）
	//FIXME
	public static String appID = "wx12da9dff1cc33e4e";//wxed196eb6578bf4f2   wx12da9dff1cc33e4e

	//微信支付分配的商户号ID（开通公众号的微信支付功能之后可以获取到）
	//FIXME
	public static String mchID = "1268555101";//1292746201   1268555101


	//受理模式下给子商户分配的子商户号
	public static String subMchID;

	//异步通知回调地址
	public static String notifyUrl = "http://illus.org/w/2016/wxpay/notify.php";

	//HTTPS证书的本地路径
	public static String certLocalPath;

	//HTTPS证书密码，默认密码等于商户号MCHID
	public static String certPassword;

	//是否使用异步线程的方式来上报API测速，默认为异步模式
	public static boolean useThreadToDoReport = true;

	//以下是几个API的路径：
	//1）被扫支付API
	public static String PAY_API = "https://api.mch.weixin.qq.com/pay/micropay";

	//2）被扫支付查询API
	public static String PAY_QUERY_API = "https://api.mch.weixin.qq.com/pay/orderquery";

	//3）退款API
	public static String REFUND_API = "https://api.mch.weixin.qq.com/secapi/pay/refund";

	//4）退款查询API
	public static String REFUND_QUERY_API = "https://api.mch.weixin.qq.com/pay/refundquery";

	//5）撤销API
	public static String REVERSE_API = "https://api.mch.weixin.qq.com/secapi/pay/reverse";

	//6）下载对账单API
	public static String DOWNLOAD_BILL_API = "https://api.mch.weixin.qq.com/pay/downloadbill";

	//7) 统计上报API
	public static String REPORT_API = "https://api.mch.weixin.qq.com/payitil/report";

	//8)统一下单
	public static String UNIFIEDORDER_API = "https://api.mch.weixin.qq.com/pay/unifiedorder";

	//9)查询订单
	public static String ORDERQUERY_API = "https://api.mch.weixin.qq.com/pay/orderquery";

	public static String HttpsRequestClassName = "com.tencent.common.HttpsRequest";

}


