package com.bfyd.easypay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.bfyd.easypay.activity.AliPayActivity;
import com.bfyd.easypay.activity.BluetoothActivity;
import com.bfyd.easypay.activity.OkHttpTest;
import com.bfyd.easypay.activity.ProlificSerialActivity;
import com.bfyd.easypay.activity.RefundActivity;
import com.bfyd.easypay.activity.SensorTest;
import com.bfyd.easypay.activity.SocketTest;
import com.bfyd.easypay.activity.WXTestActivity;
import com.bfyd.easypay.sqlite.SQLiteConfig;

public class SplashActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		new SQLiteConfig(this);
		Button button = (Button) findViewById(R.id.activity_splash_button1);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent  =  new Intent();
				intent.setClass(SplashActivity.this , WXTestActivity.class);
				SplashActivity.this.startActivity(intent);
			}
		});

		Button button1 = (Button) findViewById(R.id.activity_splash_button2);
		button1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent  =  new Intent();
				intent.setClass(SplashActivity.this , AliPayActivity.class);
				SplashActivity.this.startActivity(intent);
			}
		});

		Button button2 = (Button) findViewById(R.id.activity_splash_button3);
		button2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent  =  new Intent();
				intent.setClass(SplashActivity.this , OkHttpTest.class);
				SplashActivity.this.startActivity(intent);
			}
		});

		Button button3 = (Button) findViewById(R.id.activity_splash_button4);
		button3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent  =  new Intent();
				intent.setClass(SplashActivity.this , SocketTest.class);
				SplashActivity.this.startActivity(intent);
			}
		});

		Button button4 = (Button) findViewById(R.id.activity_splash_button5);
		button4.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent  =  new Intent();
				intent.setClass(SplashActivity.this , SensorTest.class);
				SplashActivity.this.startActivity(intent);
			}
		});

		Button button5 = (Button) findViewById(R.id.activity_splash_button6);
		button5.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent  =  new Intent();
				intent.setClass(SplashActivity.this , RefundActivity.class);
				SplashActivity.this.startActivity(intent);
			}
		});

		Button button6 = (Button) findViewById(R.id.activity_splash_button7);
		button6.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent  =  new Intent();
				intent.setClass(SplashActivity.this , ProlificSerialActivity.class);
				SplashActivity.this.startActivity(intent);
			}
		});

		Button button7 = (Button) findViewById(R.id.activity_splash_button8);
		button7.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent  =  new Intent();
				intent.setClass(SplashActivity.this , BluetoothActivity.class);
				SplashActivity.this.startActivity(intent);
			}
		});
	}
}
