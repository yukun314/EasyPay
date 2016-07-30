package com.bfyd.easypay.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bfyd.easypay.R;
import com.bfyd.easypay.app.BaseActivity;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by zyk on 2016/6/21.
 */
public class SocketTest extends BaseActivity {
	private TextView mTextView;
	private Button mButton;
	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			int what = msg.what;
			if(what == 1) {
				mTextView.setText((String)msg.obj);
			}
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_socket);
		mTextView = (TextView) findViewById(R.id.activity_socket_text);
		mButton = (Button) findViewById(R.id.activity_socket_button);
		mButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				socketText();
			}
		});
	}

	//一个读取输入流的方法
	public String readMsgFromSocket(InputStream in) {

		String msg = "";
		byte[] tempbuffer = new byte[1024];
		try {
			int numReadedBytes = in.read(tempbuffer, 0, tempbuffer.length);
			msg = new String(tempbuffer, 0, numReadedBytes, "utf-8");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return msg;
	}

	private void socketText(){
		new Thread(){
			@Override
			public void run() {
				super.run();
				try {
					final ServerSocket serverSocket = new ServerSocket(8888);
					while(true){
						Socket client = serverSocket.accept();
						BufferedOutputStream out = new BufferedOutputStream(client.getOutputStream());
						String str = readMsgFromSocket(client.getInputStream());
						if(str.length() > 0) {
							System.out.println("电脑发来消息:"+str);
							Message message = new Message();
							message.what = 1;
							message.obj = "收到新消息:"+str;
							mHandler.sendMessage(message);
							out.write("消息已经收到".getBytes());
							out.flush();
							out.close();
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}
}
