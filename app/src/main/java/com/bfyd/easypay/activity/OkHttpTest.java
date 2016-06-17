package com.bfyd.easypay.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.bfyd.easypay.R;
import com.bfyd.easypay.app.BaseActivity;
import com.bfyd.easypay.okhttp.ClientCallback;
import com.bfyd.easypay.okhttp.OkHttpClientManager;
import com.bfyd.easypay.okhttp.request.LoginRequest;
import com.bfyd.easypay.okhttp.response.LoginResponse;

/**
 * Created by zyk on 2016/6/14.
 *
 */
public class OkHttpTest extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wxtest);

		Button button = (Button) findViewById(R.id.activity_wxtest_button1);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				LoginRequest login = new LoginRequest(OkHttpTest.this.getApplicationContext());
				login.setPassword("123456789");
				login.userName = "testLogin";
				OkHttpClientManager.request("user", login , new ClientCallback<LoginResponse>() {
					@Override
					public void onResponse(LoginResponse response) {
						System.out.println("response.body:"+response.returnCode);
						System.out.println("response.body:"+response.returnMsg);
						System.out.println("response.body:"+response.body);
					}
				});
			}
		});
	}
}
