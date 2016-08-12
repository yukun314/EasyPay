package com.yarin.android.Examples_08_09;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ClientSocketActivity extends Activity {
	private static final String TAG = ClientSocketActivity.class.getSimpleName();
	private static final int REQUEST_DISCOVERY = 0x1;
	private Handler _handler = new Handler();
	private BluetoothAdapter _bluetooth = BluetoothAdapter.getDefaultAdapter();

	private BluetoothSocket socket = null;
	private TextView sTextView;
	private EditText sEditText;
	private String str;
	private OutputStream outputStream;
	private InputStream inputStream;
	private StringBuffer sbu;
	private ByteArrayOutputStream bufferOS = new ByteArrayOutputStream();

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
				WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
		setContentView(R.layout.client_socket);
		if (!_bluetooth.isEnabled()) {
			finish();
			return;
		}
		Intent intent = new Intent(this, DiscoveryActivity.class);
		
		/* Prompted to select a server to connect */
		Toast.makeText(this, "select device to connect", Toast.LENGTH_SHORT).show();
		
		/* Select device for list */
		startActivityForResult(intent, REQUEST_DISCOVERY);

		sTextView = (TextView) findViewById(R.id.sTextView);
		sEditText = (EditText) findViewById(R.id.sEditText);

		Log.d("EF-BTBee", ">>setOnKeyListener");
	}


	public void onButtonClicksend(View view) throws IOException {

		if (str != null) {
			Log.d("EF-BTBee", ">>second");
			sTextView.setText(str + "--> " + sEditText.getText());
			str += ("--> " + sEditText.getText().toString());
		} else {
			Log.d("EF-BTBee", ">>frist");
			sTextView.setText("--> " + sEditText.getText());
			str = "--> " + sEditText.getText().toString();
		}
		str += '\n';

		String tmpStr = sEditText.getText().toString();
		byte bytes[] = tmpStr.getBytes();
		outputStream.write(bytes);
	}

	/* after select, connect to device */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode != REQUEST_DISCOVERY) {
			finish();
			return;
		}
		if (resultCode != RESULT_OK) {
			finish();
			return;
		}
		final BluetoothDevice device = data.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
		new Thread() {
			public void run() {
				connect(device);
			}

			;
		}.start();
	}

	protected void onDestroy() {
		super.onDestroy();
		try {
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.e("EF-BTBee", ">>", e);
		}
	}
	long lastTime = 0;
	protected void connect(BluetoothDevice device) {
		//BluetoothSocket socket = null;
		try {
			//Create a Socket connection: need the server's UUID number of registered
			socket = device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));

			socket.connect();
			Log.d("EF-BTBee", ">>Client connectted");

			inputStream = socket.getInputStream();
			outputStream = socket.getOutputStream();
			int read = -1;
			final byte[] bytes = new byte[2048];
			ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = -1;

			System.out.println("正常应该不会到这里吧");
			boolean a = true;
while (true){
	System.out.println("开始循环");
	long time = System.currentTimeMillis();
	System.out.println("time:"+time);
	if(inputStream.available() >0){
		lastTime = time;
		System.out.println("inputStream.available():"+inputStream.available());
		read = inputStream.read(bytes);
		System.out.println("read:"+read);
		bufferOS.write(bytes, 0, read);
		a = true;
	}
	if(time - lastTime > 1000){
		System.out.println("time-lastTime:"+(time - lastTime));
		if(a) {
			_handler.post(new Runnable() {
				public void run() {
					System.out.println("一次接收完成");
					sTextView.setText(sTextView.getText() + "\n\n<-- " + HexDump.dumpHexString(bufferOS.toByteArray()));
					bufferOS.reset();
				}
			});
			a = false;
			System.out.println("if");
		}else {
			System.out.println("else");
			read = inputStream.read(bytes);
			bufferOS.write(bytes, 0, read);
			System.out.println("else 后面");
		}
	}
}
//			for (; (read = inputStream.read(bytes)) > -1; ) {
//				System.out.println("inputStream.available():"+inputStream.available());
//				final int count = read;
//				long time = System.currentTimeMillis();
//				if(time - lastTime > 1000 && bufferOS.size() > 0){
//					System.out.println("接收完毕");
//					_handler.post(new Runnable() {
//						public void run() {
//							System.out.println("一次接收完成");
//							sTextView.setText(sTextView.getText() + "\n\n<-- " + HexDump.dumpHexString(bufferOS.toByteArray()));
//							bufferOS.reset();
//						}
//					});
//				}
//				if(count > 0){
//					System.out.println("接收到新数据");
//					bufferOS.write(bytes, 0, count);
//					lastTime = time;
//				}
////				_handler.post(new Runnable() {
////					public void run() {
////						if(count > 0){
////							long time = System.currentTimeMillis();
////							if(time - lastTime > 1000){
////								bufferOS.reset();
////							}
////							bufferOS.write(bytes, 0, count);
////							lastTime = time;
////
////
////						}
////						StringBuilder b = new StringBuilder();
////						for (int i = 0; i < count; ++i) {
////							String s = Integer.toString(bytes[i]);
////							b.append(s);
////							b.append(",");
////						}
////						String s = b.toString();
////						String[] chars = s.split(",");
////						sbu = new StringBuffer();
////						for (int i = 0; i < chars.length; i++) {
////							sbu.append((char) Integer.parseInt(chars[i]));
////						}
////						Log.d("EF-BTBee", ">>inputStream");
////						if (str != null) {
////							sTextView.setText(str + "<-- " + sbu);
////							str += ("<-- " + sbu.toString());
////						} else {
////							sTextView.setText("<-- " + sbu);
////							str = "<-- " + sbu.toString();
////						}
////						str += '\n';
////					}
////				});
//			}

		} catch (IOException e) {
			Log.e("EF-BTBee", ">>", e);
			finish();
			return;
		} finally {
			if (socket != null) {
				try {
					Log.d("EF-BTBee", ">>Client Socket Close");
					socket.close();
					finish();
					return;
				} catch (IOException e) {
					Log.e("EF-BTBee", ">>", e);
				}
			}
		}
	}
}

