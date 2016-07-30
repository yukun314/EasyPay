package com.bfyd.easypay.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bfyd.easypay.R;
import com.bfyd.easypay.okhttp.BaseResponse;
import com.bfyd.easypay.okhttp.ClientCallback;
import com.bfyd.easypay.okhttp.OkHttpClientManager;
import com.bfyd.easypay.okhttp.request.OrderQueryRequest;
import com.bfyd.easypay.okhttp.request.RefundRequest;
import com.bfyd.easypay.okhttp.response.OrderQueryResponse;
import com.bfyd.easypay.okhttp.response.RefundResponse;
import com.bfyd.easypay.pay.alipay.response.QueryEntity;
import com.google.gson.Gson;
import com.google.zxing.client.android.CaptureActivity;

/**
 * Created by zyk on 2016/7/20.
 */
public class RefundActivity extends Activity {

	private String payType;
	private int totalFee;
	private String orderNo;

	private final String WX = "wx";
	private final String ZFB = "zfb";

	private TextView mMessageTextView;

	private final int QUERY_SUCCESS = 0;
	private final int QUERY_FAIL = 1;
	private final int REFUND_SUCCESS = 2;
	private final int REFUND_FAIL = 3;

	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			int what = msg.what;
			if(what == QUERY_SUCCESS) {
				String data = (String)msg.obj;
				validationResult(data);
			} else if(what == QUERY_FAIL){
				mMessageTextView.setText((String)msg.obj);
			}else if(what == REFUND_SUCCESS){//退款成功
				mMessageTextView.setText(msg.obj.toString());
			} else if(what == REFUND_FAIL){
				mMessageTextView.setText(msg.obj.toString());
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_refund);
		mMessageTextView = (TextView) findViewById(R.id.activity_refund_message);
		Button button = (Button) findViewById(R.id.activity_refund_button);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//FIXME 第一步 操作员 密码验证 通过后
				Intent intent = new Intent(RefundActivity.this, CaptureActivity.class);
				RefundActivity.this.startActivityForResult(intent,1);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == CaptureActivity.resultCode) {

			String type = data.getStringExtra(CaptureActivity.Type);
			String format = data.getStringExtra(CaptureActivity.Format);
			String contents = data.getStringExtra(CaptureActivity.Contents);
			orderNo = contents;
			System.out.println("type:"+type+"  format:"+format+"   contents:"+contents);
			mMessageTextView.setText("正在查询订单信息...");
			final OrderQueryRequest request = new OrderQueryRequest(RefundActivity.this.getApplicationContext());
			request.orderNo = orderNo;
			OkHttpClientManager.request("pay", request, new ClientCallback<OrderQueryResponse>() {
				@Override
				public void onResponse(OrderQueryResponse response) {
					Message message = new Message();
					if(response.returnCode == 0) {//正常返回
						payType = response.payType;
						String data = response.data;
						message.what = QUERY_SUCCESS;
						message.obj = data;
					}else{
						message.what = QUERY_FAIL;
						message.obj = response.returnMsg;
					}
					mHandler.sendMessage(message);
				}
			});

		}
	}

	private void validationResult(String data) {
		Gson json = new Gson();
		if(payType.equals(WX) | payType == WX){
			com.bfyd.easypay.pay.wxpay.response.OrderQueryResponse response = json.fromJson(data,com.bfyd.easypay.pay.wxpay.response.OrderQueryResponse.class);
			if(response.trade_state.equals("SUCCESS ") | response.trade_state == "SUCCESS"){
				//可退款
				totalFee = response.cash_fee;//现金金额
			}else {
				mMessageTextView.setText("订单状态为:"+response.trade_state+"! 不可退款");
				return ;
			}
		} else if(payType.equals(ZFB) | payType == ZFB) {
			QueryEntity query = json.fromJson(data, QueryEntity.class);
			if(query.trade_status.equals("TRADE_SUCCESS") | query.trade_status == "TRADE_SUCCESS") {
				float fee = Float.parseFloat(query.total_amount)*100;
				totalFee = (int)Math.floor(fee);
			}else {
				mMessageTextView.setText("订单状态为:"+query.sub_code+" , "+query.sub_msg+"! 不可退款");
				return ;
			}
		}

		refundDialog();
	}


	private void refundDialog(){
		final EditText et = new EditText(this);
		float total_fee = totalFee/100.0f;
		et.setText(total_fee+"");
		new AlertDialog.Builder(this).setTitle("请输入退款金额(元)    可退金额:"+total_fee)
				.setIcon(android.R.drawable.ic_dialog_info)
				.setView(et)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						float s = Float.parseFloat(et.getText().toString());
						int refundFee = (int)Math.floor(s*100);
						if(refundFee <= totalFee) {
							//开始退款
							refund(refundFee);
						}else {
							et.setText("");
							Toast.makeText(RefundActivity.this, "退款金额不能大于可退金额，请重新输入", Toast.LENGTH_SHORT).show();
						}
					}
				})
				.setNegativeButton("取消", null)
				.show();
	}

	private void refund(int refundFee){
		RefundRequest request = new RefundRequest(RefundActivity.this.getApplicationContext());
			request.orderNo = orderNo;
			request.payType = payType;
			request.totalFee = totalFee;
			request.refundFee = refundFee;
			System.out.println("refundrequest:"+request);
			OkHttpClientManager.request("pay", request, new ClientCallback<RefundResponse>() {
				@Override
				public void onResponse(RefundResponse response) {
					Message message = new Message();
					if(response.returnCode == 0){
						message.what = 0;
						message.obj = "成功:"+response.data;
					}else{
						message.what = 1;
						message.obj = "失败:returnCode="+response.returnCode+"  returnMsg="+response.returnMsg+"\n data:"+ response.data;
					}
					mHandler.sendMessage(message);
				}
			});
	}
}
