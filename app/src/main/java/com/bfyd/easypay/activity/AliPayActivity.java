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

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.bfyd.easypay.R;
import com.bfyd.easypay.pay.alipay.Alipay;
import com.bfyd.easypay.pay.alipay.BizContentEntity;
import com.bfyd.easypay.pay.alipay.ExtendParamsEntity;
import com.bfyd.easypay.pay.alipay.GoodsDetailEntity;
import com.bfyd.easypay.pay.alipay.RoyaltyDetailInfosEntity;
import com.bfyd.easypay.pay.alipay.RoyaltyInfoEntity;
import com.bfyd.easypay.app.BaseActivity;
import com.bfyd.easypay.utils.QRCodeUtil;
import com.bfyd.easypay.utils.Utils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zyk on 2016/6/7.
 */
public class AliPayActivity extends BaseActivity {

	private final int FAIL = 1;
	private final int SUCCESS = 2;

	private TextView mMessageView;
	private ImageView mImageView;
	private Context mContext;

	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(msg.what == FAIL){
				mMessageView.setText((String)msg.obj);
			} else if(msg.what == SUCCESS) {
				mImageView.setImageBitmap((Bitmap)msg.obj);
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
				a();
			}
		});
	}

	private void a() {
		new Thread() {
			@Override
			public void run() {
				super.run();
				AlipayClient alipayClient = new Alipay();
				AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
				BizContentEntity bce = new BizContentEntity();
				bce.out_trade_no = "20150320010101001";
				bce.seller_id = "2088102146225135";
				bce.total_amount = 88.88f;
				bce.discountable_amount = 8.88f;
				bce.undiscountable_amount = 80;
				bce.buyer_logon_id = "15901825620";
				bce.subject = "Iphone6 16G";
				bce.body = "Iphone6 16G";

				List<GoodsDetailEntity> listgde = new ArrayList<>();
				GoodsDetailEntity gde = new GoodsDetailEntity();
				gde.alipay_goods_id = "apple-01";
				gde.goods_name = "ipad";
				gde.alipay_goods_id = "20010001";
				gde.quantity = 1;
				gde.price = 2000;
				gde.goods_category = "34543238";
				gde.body = "特价手机";
				listgde.add(gde);

				bce.goods_detail = listgde;
				bce.operator_id = "yx_001";
				bce.store_id = "NJ_001";
				bce.terminal_id = "NJ_T_001";

				ExtendParamsEntity epe = new ExtendParamsEntity();
				epe.sys_service_provider_id = "2088511833207846";
				epe.hb_fq_num = "3";
				epe.timeout_express = "100";

				bce.extend_params = epe;

				bce.timeout_express = "90";
				RoyaltyInfoEntity rie = new RoyaltyInfoEntity();
				rie.royalty_type = "ROYALTY";
				List<RoyaltyDetailInfosEntity> listrdie = new ArrayList<>();
				RoyaltyDetailInfosEntity rdie = new RoyaltyDetailInfosEntity();
				rdie.serial_no = 1;
				rdie.trans_in_typ = "userId";
				rdie.batch_no = "123";
				rdie.out_relation_id = "20131124001";
				rdie.trans_out_type = "userId";
				rdie.trans_out = "2088101126765726";
				rdie.trans_in = "2088101126708402";
				rdie.amount = 0.1f;
				rdie.desc = "分账测试1";
				listrdie.add(rdie);
				rie.royalty_detail_infos = listrdie;
				bce.royalty_info = rie;

				Gson json = new Gson();
				String str = json.toJson(bce);
				System.out.println("str:" + str);
				request.setBizContent(str);
				try {
					AlipayTradePrecreateResponse response = alipayClient.execute(request, null);
					String s = response.getBody();
					Message message = new Message();
					message.obj = s;
					message.what = 1;
					mHandler.sendMessage(message);
				} catch (AlipayApiException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	private void processResult(AlipayTradePrecreateResponse response){
		Message message = new Message();
		if(response.getCode().equals("1000") | response.getCode() =="1000"){
			//success
			String qrCode = response.getQrCode();
			Point point = Utils.getDisplaySize(mContext);
			int width = Math.min(point.x, point.y)*2/3;
			Bitmap bitmap = QRCodeUtil.createQRImage(qrCode, width, width,
					BitmapFactory.decodeResource(AliPayActivity.this.getResources(),R.mipmap.ic_launcher));
			message.what = SUCCESS;
			message.obj = bitmap;
			mHandler.sendMessage(message);
		}else {
			message.what = FAIL;
			message.obj = response.getSubMsg();
			mHandler.sendMessage(message);
		}
	}
}
