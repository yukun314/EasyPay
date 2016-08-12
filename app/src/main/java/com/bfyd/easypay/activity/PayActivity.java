package com.bfyd.easypay.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bfyd.easypay.R;
import com.bfyd.easypay.app.BaseActivity;
import com.bfyd.easypay.utils.NetworkUtils;
import com.bfyd.easypay.utils.QRCodeUtil;
import com.bfyd.easypay.utils.Utils;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by zyk on 2016/8/11.
 */
public class PayActivity extends BaseActivity {

	private TextView mMessage;
	private ImageView mQRCode;
	private boolean isPassive = false;
	private Context mContext = getApplicationContext();
	private float totalFee;
	private String out_trade_no ;
	private Timer mTimer;

	private final int UNI_FAIL = 1;
	private final int UNI_SUCCESS = 2;
	private final int SEL_SUCCESS = 3;
	private final int SEL_PAYINF = 4;
	private final int SEL_FAIL = 5;
	private final int MICROPAY_SUCCESS = 6;
	private final int STARQUERY = 7;
	private final int REFUND_SUCCESS = 8;
	private final int REFUND_FAIL = 9;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pay);
		mMessage = (TextView) findViewById(R.id.activity_pay_message);
		mQRCode = (ImageView) findViewById(R.id.activity_pay_qrcode);
		Button button = (Button) findViewById(R.id.activity_pay_button);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				payPassive();
			}
		});

		payInitiative();
	}

	//被扫支付
	private void payPassive(){
		isPassive = true;
	}

	//扫码支付
	private void payInitiative(){
		isPassive = false;
		totalFee = getIntent().getFloatExtra("totalFee",0.0f);
		out_trade_no = Utils.getOutTradeNo();
		String url = NetworkUtils.PayURL+"?total_fee="+1+"&out_trade_no="+out_trade_no;
		Point point = Utils.getDisplaySize(mContext);
		int width = Math.min(point.x, point.y)*2/3;
		Bitmap bitmap = QRCodeUtil.createQRImage(url/*codeUrl*/,width,width,
				BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.ic_launcher));
		mQRCode.setVisibility(View.VISIBLE);
		mQRCode.setImageBitmap(bitmap);
		//5s后开始查询支付结果
		if(mTimer == null) {
			mTimer = new Timer();
		}
		mTimer.schedule(new MyTimerTask(), 5*1000);
	}


	private class MyTimerTask extends TimerTask {
		@Override
		public void run() {
//			mHandler.sendEmptyMessage(STARQUERY);
		}
	}
}
