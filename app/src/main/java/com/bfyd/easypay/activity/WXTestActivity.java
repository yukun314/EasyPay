package com.bfyd.easypay.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.api.AlipayApiException;
import com.bfyd.easypay.R;
import com.bfyd.easypay.pay.wxpay.request.UnifiedOrderRequest;
import com.bfyd.easypay.app.BaseActivity;
import com.bfyd.easypay.pay.wxpay.response.UnifiedOrderResponse;
import com.bfyd.easypay.pay.wxpay.client.UnifiedOrderClient;
import com.bfyd.easypay.utils.QRCodeUtil;
import com.bfyd.easypay.utils.Utils;

/**
 * Created by zyk on 2016/6/7.
 */
public class WXTestActivity extends BaseActivity {
	private TextView mMessageView;
	private ImageView mImageView;
	private Context mContext;
	private final int UNI_FAIL = 1;
	private final int UNI_SUCCESS = 2;

	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			int what = msg.what;
			if(what == UNI_FAIL){
				mMessageView.setText((String)msg.obj);
				Toast.makeText(mContext,
						(String)msg.obj,Toast.LENGTH_SHORT).show();
			} else if(what == UNI_SUCCESS) {
				Bitmap bitmap = (Bitmap)msg.obj;
				mImageView.setImageBitmap(bitmap);
				//FIXME 关于支付结果查询 什么时候发起？
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wxtest);
		mContext = getApplicationContext();
		mMessageView = (TextView) findViewById(R.id.activity_wxtest_message);
		mImageView = (ImageView) findViewById(R.id.activity_wxtest_image);
		Button button = (Button) findViewById(R.id.activity_wxtest_button);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
//				a();
				new Thread() {
					@Override
					public void run() {
						super.run();
						UnifiedOrderClient payClient = new UnifiedOrderClient();
						UnifiedOrderRequest request = new UnifiedOrderRequest(WXTestActivity.this);
						request.total_fee = 1;
						request.body = "JSAPI支付测试";
						request.out_trade_no = "9879885439";
						try {
							UnifiedOrderResponse response = payClient.execute(request, UnifiedOrderResponse.class);
							processResult(response);
						} catch (AlipayApiException e) {
							e.printStackTrace();
						}
					}
				}.start();

			}
		});
	}

	private void processResult(UnifiedOrderResponse response){
		String returnCode = response.return_code;
		Message message = new Message();
		if(returnCode.equals("SUCCESS") | returnCode == "SUCCESS") {//返回状态码
			String resultCode = response.result_code;
			if(resultCode.equals("SUCCESS") | resultCode == "SUCCESS") {//业务结果码
				String tradeType = response.trade_type;//交易类型
				if(tradeType.equals("NATIVE") | tradeType == "NATIVE") {//原生扫码支付
					String codeUrl = response.code_url;//用于生成二维码的url
					Point point = Utils.getDisplaySize(mContext);
					int width = Math.min(point.x, point.y)*2/3;
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
}
