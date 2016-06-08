package com.bfyd.easypay.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.bfyd.easypay.R;
import com.bfyd.easypay.app.BaseActivity;
//import com.tencent.protocol.unifiedorder.UnifiedorderReqData;
//import com.tencent.service.UnifiedorderService;

/**
 * Created by zyk on 2016/6/7.
 */
public class WXTestActivity extends BaseActivity {
	private TextView mMessageView;
	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(msg.what == 1){
				mMessageView.setText((String)msg.obj);
			}
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wxtest);
		mMessageView = (TextView) findViewById(R.id.activity_wxtest_message);
		Button button = (Button) findViewById(R.id.activity_wxtest_button);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				new Thread(){
					@Override
					public void run() {
						super.run();
						try {
//							UnifiedorderService unifiedorderService = new UnifiedorderService();
//							UnifiedorderReqData unifiedorderReq = new UnifiedorderReqData();
//							unifiedorderReq.body="Ipad mini  16G  白色";
//							unifiedorderReq.out_trade_no = "20150806125346";
//							unifiedorderReq.total_fee = 1;
//							unifiedorderReq.notify_url = "https://www.baidu.com";
//							unifiedorderReq.trade_type = "NATIVE";
//							unifiedorderReq.a();
//							String str = unifiedorderService.request(unifiedorderReq);
//							System.out.println("str:"+str);
//							Message message = new Message();
//							message.obj = str;
//							message.what = 1;
//							mHandler.sendMessage(message);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}.start();

			}
		});
	}
}
