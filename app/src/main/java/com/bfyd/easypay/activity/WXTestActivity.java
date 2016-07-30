package com.bfyd.easypay.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.api.AlipayApiException;
import com.bfyd.easypay.R;
import com.bfyd.easypay.okhttp.ClientCallback;
import com.bfyd.easypay.okhttp.OkHttpClientManager;
import com.bfyd.easypay.okhttp.request.SaveOrderRequest;
import com.bfyd.easypay.okhttp.response.SaveOrderResponse;
import com.bfyd.easypay.pay.wxpay.WXConfigure;
import com.bfyd.easypay.pay.wxpay.XMLParser;
import com.bfyd.easypay.pay.wxpay.client.MicropayClient;
import com.bfyd.easypay.pay.wxpay.client.OrderQueryClient;
import com.bfyd.easypay.pay.wxpay.client.RefundClient;
import com.bfyd.easypay.pay.wxpay.request.MicropayRequest;
import com.bfyd.easypay.pay.wxpay.request.OrderQueryRequest;
import com.bfyd.easypay.pay.wxpay.request.RefundRequest;
import com.bfyd.easypay.pay.wxpay.request.UnifiedOrderRequest;
import com.bfyd.easypay.app.BaseActivity;
import com.bfyd.easypay.pay.wxpay.response.MicropayResponse;
import com.bfyd.easypay.pay.wxpay.response.OrderQueryResponse;
import com.bfyd.easypay.pay.wxpay.response.RefundResponse;
import com.bfyd.easypay.pay.wxpay.response.UnifiedOrderResponse;
import com.bfyd.easypay.pay.wxpay.client.UnifiedOrderClient;
import com.bfyd.easypay.utils.NetworkUtils;
import com.bfyd.easypay.utils.QRCodeUtil;
import com.bfyd.easypay.utils.QRcodeValidity;
import com.bfyd.easypay.utils.Utils;
import com.bfyd.easypay.utils.WXUtils;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.zxing.client.android.CaptureActivity;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by zyk on 2016/6/7.
 */
public class WXTestActivity extends BaseActivity {
	private TextView mMessageView;
	private ImageView mImageView;
	private Context mContext;
	private final int UNI_FAIL = 1;
	private final int UNI_SUCCESS = 2;
	private final int SEL_SUCCESS = 3;
	private final int SEL_PAYINF = 4;
	private final int SEL_FAIL = 5;
	private final int MICROPAY_SUCCESS = 6;
	private final int STARQUERY = 7;
	private final int REFUND_SUCCESS = 8;
	private final int REFUND_FAIL = 9;
	private boolean neetSelect = true;
	private String out_trade_no ;
	private Timer mTimer;

	private QRcodeValidity mQRcodeValidity;

	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			int what = msg.what;
			if(what == UNI_FAIL){
				mMessageView.setText((String)msg.obj);
//				Toast.makeText(mContext,
//						(String)msg.obj,Toast.LENGTH_SHORT).show();
			} else if(what == UNI_SUCCESS) {
				mImageView.setVisibility(View.VISIBLE);
				Bitmap bitmap = (Bitmap)msg.obj;
				mImageView.setImageBitmap(bitmap);
				//5s后开始查询支付结果
				if(mTimer == null) {
					mTimer = new Timer();
				}
				mTimer.schedule(new MyTimerTask(), 5*1000);
			} else if (what == SEL_SUCCESS) {
				mMessageView.setText((String)msg.obj);
				OrderQueryResponse response = (OrderQueryResponse)msg.obj;
				paySuccess(response.total_fee, response.time_end);
//				Toast.makeText(mContext,
//						(String)msg.obj,Toast.LENGTH_SHORT).show();
			} else if(what == SEL_PAYINF) {
				mImageView.setVisibility(View.INVISIBLE);
				mMessageView.setText((String)msg.obj);
//				Toast.makeText(mContext,
//						(String)msg.obj,Toast.LENGTH_SHORT).show();
			} else if(what == SEL_FAIL) {
				mMessageView.setText((String)msg.obj);
//				Toast.makeText(mContext,
//						(String)msg.obj,Toast.LENGTH_SHORT).show();
			} else if (what == MICROPAY_SUCCESS){
//				mMessageView.setText((String)msg.obj);
				MicropayResponse response = (MicropayResponse)msg.obj;
				paySuccess(response.total_fee, response.time_end);
			} else if(what == mQRcodeValidity.TimeOver){
				//二维码失效处理
				mImageView.setImageBitmap(null);
				mImageView.setVisibility(View.INVISIBLE);
				mMessageView.setText("此二维码已经失效");
			}else if (what == STARQUERY){
				queryOrder();
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wxtest);
		mQRcodeValidity = new QRcodeValidity();
		mContext = getApplicationContext();
		mMessageView = (TextView) findViewById(R.id.activity_wxtest_message);
		mImageView = (ImageView) findViewById(R.id.activity_wxtest_image);
		Button button = (Button) findViewById(R.id.activity_wxtest_button);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mQRcodeValidity.starTiming(mHandler);
				uniunifiedPayfiedPay();
//				a();
//				new Thread() {
//					@Override
//					public void run() {
//						super.run();
//						UnifiedOrderClient payClient = new UnifiedOrderClient();
//						UnifiedOrderRequest request = new UnifiedOrderRequest(WXTestActivity.this);
//						request.total_fee = 1;
//						request.body = "支付测试";
//						out_trade_no = Utils.getOutTradeNo();
//						request.out_trade_no = out_trade_no;
//						try {
//
//							UnifiedOrderResponse response = payClient.execute(request, UnifiedOrderResponse.class);
//							processResult(response);
//						} catch (AlipayApiException e) {
//							e.printStackTrace();
//						}
//					}
//				}.start();

			}
		});

		Button button1 = (Button) findViewById(R.id.activity_wxtest_button1);
		button1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
//				a();
//				new Thread() {
//					@Override
//					public void run() {
//						super.run();
//						queryOrder();
						micropay();
//					}
//				}.start();

			}
		});

		Button button2 = (Button) findViewById(R.id.activity_wxtest_button2);
		button2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				refundDialog();
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(mQRcodeValidity != null){
			mQRcodeValidity.endTiming();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == CaptureActivity.resultCode) {
			new Thread() {
				@Override
				public void run() {
					super.run();
					String type = data.getStringExtra(CaptureActivity.Type);
					String format = data.getStringExtra(CaptureActivity.Format);
					String contents = data.getStringExtra(CaptureActivity.Contents);
					System.out.println("type:"+type+"  format:"+format+"   contents:"+contents);
					MicropayRequest micropay = new MicropayRequest(WXTestActivity.this);
					micropay.body = "商品描述";
					out_trade_no = Utils.getOutTradeNo();
					micropay.out_trade_no = out_trade_no;
					micropay.total_fee = 3;
					micropay.auth_code = contents;
					MicropayClient client = new MicropayClient();
					try {
						MicropayResponse response = client.execute(micropay, MicropayResponse.class);
						processMicropayResult(response);
					} catch (AlipayApiException e) {
						e.printStackTrace();
					}
				}
			}.start();

		}
	}

	private void uniunifiedPayfiedPay(){
//		UnifiedOrderRequest request = new UnifiedOrderRequest(WXTestActivity.this);
//		request.total_fee = 1;
//		request.body = "支付测试";
		out_trade_no = Utils.getOutTradeNo();
//		request.out_trade_no = out_trade_no;
//		Map<String, Object> map = request.toMap();
//		String sign = WXUtils.genSign(map);
//		request.setSign(sign);
//		try {
//			String params = XMLParser.toXML(request);
//			String url = NetworkUtils.PayURL+"?parameter="+params;
			String url = NetworkUtils.PayURL+"?total_fee="+1+"&out_trade_no="+out_trade_no;
			Point point = Utils.getDisplaySize(mContext);
			int width = Math.min(point.x, point.y)*2/3;
			Bitmap bitmap = QRCodeUtil.createQRImage(url/*codeUrl*/,width,width,
					BitmapFactory.decodeResource(WXTestActivity.this.getResources(),R.mipmap.ic_launcher));
			mImageView.setVisibility(View.VISIBLE);
			mImageView.setImageBitmap(bitmap);
//		} catch (IllegalAccessException e) {
//			e.printStackTrace();
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
	}

	private void micropay(){
		Intent intent = new Intent(WXTestActivity.this, CaptureActivity.class);
		this.startActivityForResult(intent,1);
	}

	private void paySuccess(int totalFee, String endTime){
		System.out.println("paySuccess 开始方后台发送数据");
		//FIXME 支付成功后的相关操作·
		//把支付信息保存到服务器
		SaveOrderRequest request = new SaveOrderRequest(getApplicationContext());
		request.payType = "wx";
		request.orderNo = out_trade_no;
		request.appId = WXConfigure.appID;;
		request.mchId = WXConfigure.mchID;
		request.totalFee = totalFee;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date = new Date();
		try {
			date = formatter.parse(endTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		request.payTime = date.getTime();

		System.out.println("request:"+request);
		OkHttpClientManager.request("pay", request, new ClientCallback<SaveOrderResponse>(){
			@Override
			public void onResponse(SaveOrderResponse response) {
				System.out.println("服务器返回了结果");
				if(response.returnCode == 0) {//成功
					//这里没有业务处理就不在验证签名
					System.out.println("插入后台数据成功");
				} else {
					System.out.println("插入后台数据失败:"+response.returnMsg);
				}
			}
		});

	}

	private void processMicropayResult(MicropayResponse response){
		String returnCode = response.return_code;
		System.out.println("returnCode:"+returnCode+"   "+response);
		Message message = new Message();
		if(returnCode.equals("SUCCESS") | returnCode == "SUCCESS") {//返回状态码
			String resultCode = response.result_code;
			if(resultCode.equals("SUCCESS") | resultCode == "SUCCESS") {//业务结果码
					message.what = MICROPAY_SUCCESS;
				String str = (response.is_subscribe.equals("Y"))?"是":"否";
					message.obj = response;
					mHandler.sendMessage(message);
			} else {
				message.what = UNI_FAIL;
				String errCodeDes = response.err_code_des;
				String errCode = response.err_code;
				if (errCode.equals("SYSTEMERROR") | errCode == "SYSTEMERROR") {
					errCodeDes += "\n原因:系统超时";
					errCodeDes += "\n解决方案:请立即调用被扫订单结果查询API，查询当前订单状态，并根据订单的状态决定下一步的操作";
				} else if (errCode.equals("PARAM_ERROR") | errCode == "PARAM_ERROR") {
					errCodeDes += "\n原因:请求参数未按指引进行填写";
					errCodeDes += "\n解决方案:请根据接口返回的详细信息检查您的程序";
				} else if (errCode.equals("ORDERPAID") | errCode == "ORDERPAID") {
					errCodeDes += "\n原因:订单号重复";
					errCodeDes += "\n解决方案:请确认该订单号是否重复支付，如果是新单，请使用新订单号提交";
				} else if (errCode.equals("NOAUTH") | errCode == "NOAUTH") {
					errCodeDes += "\n原因:商户没有开通被扫支付权限";
					errCodeDes += "\n解决方案:请开通商户号权限。请联系产品或商务申请";
				} else if (errCode.equals("AUTHCODEEXPIRE") | errCode == "AUTHCODEEXPIRE") {
					errCodeDes += "\n原因:用户的条码已经过期";
					errCodeDes += "\n解决方案:请收银员提示用户，请用户在微信上刷新条码，然后请收银员重新扫码。 直接将错误展示给收银员";
				} else if (errCode.equals("NOTENOUGH") | errCode == "NOTENOUGH") {
					errCodeDes += "\n原因:用户的零钱余额不足";
					errCodeDes += "\n解决方案:请收银员提示用户更换当前支付的卡，然后请收银员重新扫码。建议：商户系统返回给收银台的提示为“用户余额不足.提示用户换卡支付”";
				} else if (errCode.equals("NOTSUPORTCARD") | errCode == "NOTSUPORTCARD") {
					errCodeDes += "\n原因:用户使用卡种不支持当前支付形式";
					errCodeDes += "\n解决方案:请用户重新选择卡种 建议：商户系统返回给收银台的提示为“该卡不支持当前支付，提示用户换卡支付或绑新卡支付”";
				} else if (errCode.equals("ORDERCLOSED") | errCode == "ORDERCLOSED") {
					errCodeDes += "\n原因:该订单已关";
					errCodeDes += "\n解决方案:商户订单号异常，请重新下单支付";
				} else if (errCode.equals("ORDERREVERSED") | errCode == "ORDERREVERSED") {
					errCodeDes += "\n原因:当前订单已经被撤销";
					errCodeDes += "\n解决方案:当前订单状态为“订单已撤销”，请提示用户重新支付";
				} else if (errCode.equals("BANKERROR") | errCode == "BANKERROR") {
					errCodeDes += "\n原因:银行端超时";
					errCodeDes += "\n解决方案:请立即调用被扫订单结果查询API，查询当前订单的不同状态，决定下一步的操作。";
				} else if (errCode.equals("USERPAYING") | errCode == "USERPAYING") {
					errCodeDes += "\n原因:该笔交易因为业务规则要求，需要用户输入支付密码。";
					errCodeDes += "\n解决方案:等待5秒，然后调用被扫订单结果查询API，查询当前订单的不同状态，决定下一步的操作。";
				} else if (errCode.equals("AUTH_CODE_ERROR") | errCode == "AUTH_CODE_ERROR") {
					errCodeDes += "\n原因:请求参数未按指引进行填写";
					errCodeDes += "\n解决方案:每个二维码仅限使用一次，请刷新再试";
				} else if (errCode.equals("AUTH_CODE_INVALID") | errCode == "AUTH_CODE_INVALID") {
					errCodeDes += "\n原因:收银员扫描的不是微信支付的条码 ";
					errCodeDes += "\n解决方案:请扫描微信支付被扫条码/二维码";
				} else if (errCode.equals("XML_FORMAT_ERROR") | errCode == "XML_FORMAT_ERROR") {
					errCodeDes += "\n原因:XML格式错误";
					errCodeDes += "\n解决方案:请检查XML参数格式是否正确";
				} else if (errCode.equals("REQUIRE_POST_METHOD") | errCode == "REQUIRE_POST_METHOD") {
					errCodeDes += "\n原因:未使用post传递参数";
					errCodeDes += "\n解决方案:请检查请求参数是否通过post方法提交";
				} else if (errCode.equals("SIGNERROR") | errCode == "SIGNERROR") {
					errCodeDes += "\n原因:参数签名结果不正确";
					errCodeDes += "\n解决方案:请检查签名参数和方法是否都符合签名算法要求";
				} else if (errCode.equals("LACK_PARAMS") | errCode == "LACK_PARAMS") {
					errCodeDes += "\n原因:缺少必要的请求参数";
					errCodeDes += "\n解决方案:请检查参数是否齐全";
				} else if (errCode.equals("NOT_UTF8") | errCode == "NOT_UTF8") {
					errCodeDes += "\n原因:未使用指定编码格式";
					errCodeDes += "\n解决方案:请使用UTF-8编码格式";
				} else if (errCode.equals("BUYER_MISMATCH") | errCode == "BUYER_MISMATCH") {
					errCodeDes += "\n原因:暂不支持同一笔订单更换支付方";
					errCodeDes += "\n解决方案:请确认支付方是否相同";
				} else if (errCode.equals("APPID_NOT_EXIST") | errCode == "APPID_NOT_EXIST") {
					errCodeDes += "\n原因:参数中缺少APPID";
					errCodeDes += "\n解决方案:请检查APPID是否正确";
				} else if (errCode.equals("MCHID_NOT_EXIST") | errCode == "MCHID_NOT_EXIST") {
					errCodeDes += "\n原因:参数中缺少MCHID";
					errCodeDes += "\n解决方案:请检查MCHID是否正确";
				} else if (errCode.equals("OUT_TRADE_NO_USED") | errCode == "OUT_TRADE_NO_USED") {
					errCodeDes += "\n原因:同一笔交易不能多次提交";
					errCodeDes += "\n解决方案:请核实商户订单号是否重复提交";
				} else if (errCode.equals("APPID_MCHID_NOT_MATCH") | errCode == "APPID_MCHID_NOT_MATCH") {
					errCodeDes += "\n原因:appid和mch_id不匹配";
					errCodeDes += "\n解决方案:请确认appid和mch_id是否匹配";
				}
				message.obj = errCodeDes;
				mHandler.sendMessage(message);
			}
		} else {
			message.what = UNI_FAIL;
			message.obj = response.return_msg;
			mHandler.sendMessage(message);
		}
	}

	private void processResult(UnifiedOrderResponse response){
		String returnCode = response.return_code;
		System.out.println("returnCode:"+returnCode+"   "+response);
		Message message = new Message();
		if(returnCode.equals("SUCCESS") | returnCode == "SUCCESS") {//返回状态码
			String resultCode = response.result_code;
			if(resultCode.equals("SUCCESS") | resultCode == "SUCCESS") {//业务结果码
				String tradeType = response.trade_type;//交易类型
				if(tradeType.equals("NATIVE") | tradeType == "NATIVE") {//原生扫码支付
					String codeUrl = response.code_url;//用于生成二维码的url
					Point point = Utils.getDisplaySize(mContext);
					int width = Math.min(point.x, point.y)*2/3;
//					String url = "http://illus.org/w/2016/s.php?codeUrl="+codeUrl;
					Bitmap bitmap = QRCodeUtil.createQRImage(codeUrl,width,width,
							BitmapFactory.decodeResource(WXTestActivity.this.getResources(),R.mipmap.ic_launcher));
					message.what = UNI_SUCCESS;
					message.obj = bitmap;
					mHandler.sendMessage(message);
				}else {
					//除非代码被修改，该应用的交易类型被定为 NATIVE
					message.what = UNI_FAIL;
					message.obj = "交易类型非 原生扫码支付 返回结果无二维码数据";
					mHandler.sendMessage(message);
				}
			} else {
				message.what = UNI_FAIL;
				String errCodeDes   = response.err_code_des;
				String errCode = response.err_code;
				if(errCode.equals("NOAUTH") | errCode == "NOAUTH") {
					errCodeDes+="\n原因:商户未开通此接口权限";
					errCodeDes+="\n解决方案:请商户前往申请此接口权限";
				} else if(errCode.equals("NOTENOUGH") | errCode == "NOTENOUGH") {
					errCodeDes+="\n原因:用户帐号余额不足";
					errCodeDes+="\n解决方案:用户帐号余额不足，请用户充值或更换支付卡后再支付";
				}else if(errCode.equals("ORDERPAID") | errCode == "ORDERPAID") {
					errCodeDes+="\n原因:商户订单已支付，无需重复操作";
					errCodeDes+="\n解决方案:商户订单已支付，无需更多操作";
				}else if(errCode.equals("ORDERCLOSED") | errCode == "ORDERCLOSED") {
					errCodeDes+="\n原因:当前订单已关闭，无法支付";
					errCodeDes+="\n解决方案:当前订单已关闭，请重新下单";
				}else if(errCode.equals("SYSTEMERROR") | errCode == "SYSTEMERROR") {
					errCodeDes+="\n原因:系统超时";
					errCodeDes+="\n解决方案:系统异常，请用相同参数重新调用";
				}else if(errCode.equals("APPID_NOT_EXIST") | errCode == "APPID_NOT_EXIST") {
					errCodeDes+="\n原因:参数中缺少APPID";
					errCodeDes+="\n解决方案:请检查APPID是否正确";
				}else if(errCode.equals("MCHID_NOT_EXIST") | errCode == "MCHID_NOT_EXIST") {
					errCodeDes+="\n原因:参数中缺少MCHID";
					errCodeDes+="\n解决方案:请检查MCHID是否正确";
				}else if(errCode.equals("APPID_MCHID_NOT_MATCH") | errCode == "APPID_MCHID_NOT_MATCH") {
					errCodeDes+="\n原因:appid和mch_id不匹配";
					errCodeDes+="\n解决方案:请确认appid和mch_id是否匹配";
				}else if(errCode.equals("LACK_PARAMS") | errCode == "LACK_PARAMS") {
					errCodeDes+="\n原因:缺少必要的请求参数";
					errCodeDes+="\n解决方案:请检查参数是否齐全";
				}else if(errCode.equals("OUT_TRADE_NO_USED") | errCode == "OUT_TRADE_NO_USED") {
					errCodeDes+="\n原因:同一笔交易不能多次提交";
					errCodeDes+="\n解决方案:请核实商户订单号是否重复提交";
				}else if(errCode.equals("SIGNERROR") | errCode == "SIGNERROR") {
					errCodeDes+="\n原因:参数签名结果不正确";
					errCodeDes+="\n解决方案:请检查签名参数和方法是否都符合签名算法要求";
				}else if(errCode.equals("XML_FORMAT_ERROR") | errCode == "XML_FORMAT_ERROR") {
					errCodeDes+="\n原因:XML格式错误";
					errCodeDes+="\n解决方案:请检查XML参数格式是否正确";
				}else if(errCode.equals("REQUIRE_POST_METHOD") | errCode == "REQUIRE_POST_METHOD") {
					errCodeDes+="\n原因:未使用post传递参数 ";
					errCodeDes+="\n解决方案:请检查请求参数是否通过post方法提交";
				}else if(errCode.equals("POST_DATA_EMPTY") | errCode == "POST_DATA_EMPTY") {
					errCodeDes+="\n原因:post数据不能为空";
					errCodeDes+="\n解决方案:请检查post数据是否为空";
				}else if(errCode.equals("NOT_UTF8") | errCode == "NOT_UTF8") {
					errCodeDes+="\n原因:未使用指定编码格式";
					errCodeDes+="\n解决方案:请使用NOT_UTF8编码格式";
				}
				message.obj = errCodeDes;
				mHandler.sendMessage(message);
			}
		} else {
			message.what = UNI_FAIL;
			message.obj = response.return_msg;
			mHandler.sendMessage(message);
		}
	}

	private void queryOrder(){
		neetSelect = true;
		final OrderQueryClient client = new OrderQueryClient();
		final OrderQueryRequest request = new OrderQueryRequest();
		new Thread(){
			@Override
			public void run() {
				super.run();
				//查到支付成功或外部终止时停止循环
				while(neetSelect) {
					try {
						Message message = new Message();
						request.out_trade_no = out_trade_no;
						OrderQueryResponse response = client.execute(request, OrderQueryResponse.class);
						System.out.println("查询支付结果response:"+response);
						if(response.return_code.equals("SUCCESS") | response.return_code == "SUCCESS") {
							if(response.result_code.equals("SUCCESS") | response.result_code == "SUCCESS") {
								String tradeState = response.trade_state;
								if(tradeState.equals("SUCCESS") | tradeState == "SUCCESS") {//支付成功
									neetSelect = false;
									message.what = SEL_SUCCESS;
									message.obj = response;
									mHandler.sendMessage(message);
								} else if(tradeState.equals("USERPAYING") | tradeState == "USERPAYING") {//用户支付中
									//界面处理，如 不再显示二维码 显示正在支付
									message.what = SEL_PAYINF;
									message.obj = "正在支付:"+response.trade_state_desc;
									mHandler.sendMessage(message);
								} else if(tradeState.equals("CLOSED") | tradeState == "CLOSED") {//已关闭
									//FIXME 是不是不能再支付
									neetSelect = false;
									message.what = SEL_FAIL;
									message.obj = "支付失败:"+response.trade_state_desc;
									mHandler.sendMessage(message);
								} else {
//									REFUND—转入退款
//									NOTPAY—未支付
//									REVOKED—已撤销（刷卡支付）
//									USERPAYING--用户支付中
//									PAYERROR--支付失败(其他原因，如银行返回失败)
//									neetSelect = false;
									message.what = SEL_FAIL;
									message.obj = "支付失败:"+response.trade_state_desc;
									mHandler.sendMessage(message);
									System.out.println("response:"+response);
								}

							}
						}
					} catch (AlipayApiException e) {
						e.printStackTrace();
					}
					//每秒查询一次
					try {
						this.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();

	}

	private void refundDialog(){
		final EditText et = new EditText(this);
		et.setText("2016071911474595234032319521");
		new AlertDialog.Builder(this).setTitle("请输入订单号")
				.setIcon(android.R.drawable.ic_dialog_info)
				.setView(et)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						String s = et.getText().toString();
						refund(s);
					}
				})
				.setNegativeButton("取消", null)
				.show();
	}

	private void refund(String trade_no){
		final RefundClient client = new RefundClient();
		final RefundRequest request = new RefundRequest();
		request.out_trade_no = trade_no;
		request.out_refund_no = Utils.getOutTradeNo();
		request.total_fee = 1;
		request.refund_fee = 1;
		new Thread(){
			@Override
			public void run() {
				super.run();
				Message message = new Message();
				try {
					RefundResponse response = client.execute(request, RefundResponse.class);
					if(response.return_code.equals("SUCCESS") | response.return_code == "SUCCESS") {
						if (response.result_code.equals("SUCCESS") | response.result_code == "SUCCESS") {
							message.what = REFUND_SUCCESS;
							message.obj = "退款成功:"+response.err_code_des;
							mHandler.sendMessage(message);
						}else{
							message.what = REFUND_FAIL;
							message.obj = "退款失败:"+response.err_code_des;
							mHandler.sendMessage(message);
						}
					}else {
						message.what = REFUND_FAIL;
						message.obj = "退款失败:"+response.return_msg;
						mHandler.sendMessage(message);
					}
				} catch (AlipayApiException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	private class MyTimerTask extends TimerTask{
		@Override
		public void run() {
			mHandler.sendEmptyMessage(STARQUERY);
		}
	}
}
