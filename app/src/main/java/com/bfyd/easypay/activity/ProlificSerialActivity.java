package com.bfyd.easypay.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bfyd.easypay.R;
import com.bfyd.easypay.serial.CustomerDisplayEntity;
import com.bfyd.easypay.serial.ProlificSerialSettingActivity;
import com.bfyd.easypay.service.ProlificSerialService;

import org.w3c.dom.Text;

/**
 * Created by zyk on 2016/7/28.
 */
public class ProlificSerialActivity extends Activity implements ProlificSerialService.OnReceivedMessageListener{

	private TextView mMessage;
	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(msg.what == 0){
				String s = mMessage.getText().toString();
				mMessage.setText(s+"\n-->"+msg.obj);
			}
		}
	};

	private ServiceConnection conn = new ServiceConnection() {
		@Override
		public void onServiceDisconnected(ComponentName name) {

		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder binder) {
			((ProlificSerialService.MsgBinder)binder).setOnReceiced(ProlificSerialActivity.this);
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_prolic_serial);
		mMessage = (TextView) findViewById(R.id.activity_prolic_serial_message);

		Button button = (Button) findViewById(R.id.activity_prolic_serial_button);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(ProlificSerialActivity.this, ProlificSerialSettingActivity.class);
				ProlificSerialActivity.this.startActivity(intent);
			}
		});

		//绑定Service
		Intent intent = new Intent();
		intent.setClass(this,ProlificSerialService.class);
		bindService(intent, conn, Context.BIND_AUTO_CREATE);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unbindService(conn);
	}

	//收到信息
	@Override
	public void onReceivedMessage(int flag, String message, CustomerDisplayEntity cde) {
		Message m = new Message();
		m.what = 0;
		if(flag == 0){
			System.out.println("收到信息:"+message);
			m.obj = cde.toString();
		} else {
			System.out.println("错误:"+message);
			m.obj = message;
		}


		mHandler.sendMessage(m);
	}
}
