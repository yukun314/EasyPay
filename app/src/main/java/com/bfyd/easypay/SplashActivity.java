package com.bfyd.easypay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.bfyd.easypay.activity.AliPayActivity;
import com.bfyd.easypay.activity.WXTestActivity;

public class SplashActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

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
	}
}
