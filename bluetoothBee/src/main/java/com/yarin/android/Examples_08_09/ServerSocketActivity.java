package com.yarin.android.Examples_08_09;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ServerSocketActivity extends ListActivity
{
	/*Some constants, representing the name of the server */
	public static final String PROTOCOL_SCHEME_L2CAP = "btl2cap";
	public static final String PROTOCOL_SCHEME_RFCOMM = "btspp";
	public static final String PROTOCOL_SCHEME_BT_OBEX = "btgoep";
	public static final String PROTOCOL_SCHEME_TCP_OBEX = "tcpobex";
	
	private static final String TAG = ServerSocketActivity.class.getSimpleName();
	private Handler _handler = new Handler();
	
	
	/* GetDefaultAdapter */
	private BluetoothAdapter _bluetooth = BluetoothAdapter.getDefaultAdapter();
	
	/* Server */
	private BluetoothServerSocket _serverSocket;
	
	/* Thread-Listen */
	private Thread _serverWorker = new Thread() {
		public void run() {
			listen();
		};
	};
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
				WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
		setContentView(R.layout.server_socket);
		if (!_bluetooth.isEnabled()) {
			finish();
			return;
		}
		/* Start listen */
		_serverWorker.start();
	}
	protected void onDestroy() {
		super.onDestroy();
		shutdownServer();
	}
	protected void finalize() throws Throwable {
		super.finalize();
		shutdownServer();
	}
	/* Stop server */
	private void shutdownServer() {
		new Thread() {
			public void run() {
				_serverWorker.interrupt();
				if (_serverSocket != null) {
					try {
						/* close sever */
						_serverSocket.close();
					} catch (IOException e) {
						Log.e("EF-BTBee", ">>", e);
					}
					_serverSocket = null;
					finish();
					return;
				}
			};
		}.start();
	}
	public void onButtonClicked(View view) {
		shutdownServer();
	}
	protected void listen() {
		try {
			/* Create BT service
			 * param:server name、UUID
			 */
			_serverSocket = _bluetooth.listenUsingRfcommWithServiceRecord(PROTOCOL_SCHEME_RFCOMM,
					UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
			
			/* Client devices*/
			final List<String> lines = new ArrayList<String>();
			_handler.post(new Runnable() {
				public void run() {
					lines.add("Rfcomm server started...");
					ArrayAdapter<String> adapter = new ArrayAdapter<String>(
							ServerSocketActivity.this,
							android.R.layout.simple_list_item_1, lines);
					setListAdapter(adapter);
				}
			});
			
			
			/* accept client request */
			BluetoothSocket socket = _serverSocket.accept();
			Log.d("EF-BTBee", ">>Accept Client Request");
			
			/* Processing the request content*/
			if (socket != null) {
				InputStream inputStream = socket.getInputStream();
				OutputStream outputStream = socket.getOutputStream();
				int read = -1;
				final byte[] bytes = new byte[2048];
				for (; (read = inputStream.read(bytes)) > -1;) {
					outputStream.write(bytes);
					final int count = read;
					_handler.post(new Runnable() {
						public void run() {
							StringBuilder b = new StringBuilder();
							for (int i = 0; i < count; ++i) {
								String s = Integer.toString(bytes[i]);
								b.append(s);
								b.append(",");
							}
							String s = b.toString();
							String[] chars = s.split(",");
							StringBuffer sbu = new StringBuffer();  
							 for (int i = 0; i < chars.length; i++) {  
							        sbu.append((char) Integer.parseInt(chars[i]));  
							    }
							 
							lines.add(sbu.toString());
							ArrayAdapter<String> adapter = new ArrayAdapter<String>(
									ServerSocketActivity.this,
									android.R.layout.simple_list_item_1, lines);
							setListAdapter(adapter);
						}
					}); 
				}
				Log.d("EF-BTBee", "Server Listen Over!!");
			}
			} catch (IOException e) {
				Log.e("EF-BTBee", ">>", e);
			} finally {
		}
	}
}

