package com.bfyd.easypay.activity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.bfyd.easypay.R;
import com.bfyd.easypay.serial.Printer;
import com.bfyd.easypay.serial.PrinterDataEntity;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Created by zyk on 2016/8/11.
 */
public class BluetoothActivity extends Activity {

	private TextView mMessage, mReceive;
	private ListView mListView;
	private MyAdapter mAdapter;
	private List<BluetoothDevice> mList;

	private BluetoothDevice device;
	private BluetoothSocket socket = null;
	private InputStream inputStream;
	private OutputStream outputStream;
	private Handler _handler = new Handler();
	private ByteArrayOutputStream bufferOS = new ByteArrayOutputStream();

	private Printer mPrinter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bluetooth);
		init();
		mPrinter = new Printer(getApplicationContext(),Printer.USBPrinter);
		Button button = (Button) findViewById(R.id.activity_bluetooth_button);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				printText();
			}
		});
	}

	protected void onDestroy() {
		super.onDestroy();
		try {
			if (socket != null) {
				socket.close();
			}
		} catch (IOException e) {
		}
	}

	private void init() {
		mMessage = (TextView) findViewById(R.id.activity_bluetooth_message);
		mReceive = (TextView) findViewById(R.id.activity_bluetooth_receive);
		mListView = (ListView) findViewById(R.id.activity_bluetooth_list);
		mList = new ArrayList<>();
		mAdapter = new MyAdapter();
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				connect(position);
			}
		});

		initData();
	}

	private void connect(int position) {
		if (position >= 0 && position < mList.size()) {
			device = mList.get(position);
			mMessage.setText("正在链接" + device.getName());
			new Thread() {
				@Override
				public void run() {
					super.run();
					connect();
				}
			}.start();
		}
	}

	long lastTime = 0;

	protected void connect() {
		if (device == null) {
			return;
		}
		try {
			//Create a Socket connection: need the server's UUID number of registered
			socket = device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
			socket.connect();
			_handler.post(new Runnable() {
				public void run() {
					mMessage.setText("已链接:" + device.getName());
					mListView.setVisibility(View.GONE);
				}
			});
			inputStream = socket.getInputStream();
			outputStream = socket.getOutputStream();

			int read = -1;
			final byte[] bytes = new byte[2048];

			boolean a = true;
			while (true) {
				long time = System.currentTimeMillis();
				Thread.sleep(100);
				if (inputStream.available() > 0) {
					lastTime = time;
					read = inputStream.read(bytes);
					System.out.println("read:" + read);
					bufferOS.write(bytes, 0, read);
					a = true;
				}
				if (time - lastTime > 1000) {
					System.out.println("time-lastTime:" + (time - lastTime));
					if (a) {
						if (bufferOS.size() > 0) {
							final byte[] data = bufferOS.toByteArray();
							bufferOS.reset();
							_handler.post(new Runnable() {
								public void run() {
									mReceive.setText(mReceive.getText() + "\n\n<-- " + new PrinterDataEntity(data).result);
								}
							});
							print(data);
						}
						a = false;
					} else {
						read = inputStream.read(bytes);
						bufferOS.write(bytes, 0, read);
					}
				}
			}
		} catch (final IOException e) {
			_handler.post(new Runnable() {
				public void run() {
					mMessage.setText("  " + e);
					mListView.setVisibility(View.VISIBLE);
				}
			});
		} catch (InterruptedException e) {
//			e.printStackTrace();
		} finally {
			if (socket != null) {
				try {

					_handler.post(new Runnable() {
						public void run() {
							mMessage.setText("Client Socket Close");
							mListView.setVisibility(View.VISIBLE);
						}
					});
					socket.close();
					return;
				} catch (IOException e) {
				}
			}
		}
	}

	private void printText(){
		String str = "测试蓝牙打印";
		try {
			outputStream.write(str.getBytes());
			outputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void print(byte[] data){
		int offset = mPrinter.print(data);
		System.out.println("print offset:"+offset);
	}

	private void initData() {
		//得到BluetoothAdapter对象
		BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
		if (adapter != null) {
			//方法判断当前蓝牙设备是否可用
			if (adapter.isEnabled()) {
				//得到所有已经配对的蓝牙适配器对象
				Set<BluetoothDevice> devices = adapter.getBondedDevices();
				if (devices.size() > 0) {
					//用迭代
					for (Iterator iterator = devices.iterator(); iterator.hasNext(); ) {
						//得到BluetoothDevice对象,也就是说得到配对的蓝牙适配器
						BluetoothDevice device = (BluetoothDevice) iterator.next();
						//得到远程蓝牙设备的地址
						mList.add(device);
					}
					mAdapter.notifyDataSetChanged();
					mMessage.setText("请选择 要使用的蓝牙");
				} else {
					mMessage.setText("当前无配对的蓝牙");
				}
			} else {
				mMessage.setText("当前蓝牙设备不可用 请先开启蓝牙");
			}
		} else {
			mMessage.setText("当前设备无蓝牙");
		}
	}

	private class MyAdapter extends BaseAdapter {

		private ViewHolder mHolder;

		@Override
		public int getCount() {
			return mList.size();
		}

		@Override
		public Object getItem(int position) {
			return mList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				mHolder = new ViewHolder();
				convertView = LayoutInflater.from(BluetoothActivity.this.getApplicationContext()).inflate(R.layout.activity_bluetooth_item, null);
				mHolder.mTextView = (TextView) convertView.findViewById(R.id.activity_bluetooth_item_message);
				convertView.setTag(mHolder);
			} else {
				mHolder = (ViewHolder) convertView.getTag();
			}

			mHolder.mTextView.setText(mList.get(position).getName());

			return convertView;
		}


	}

	private class ViewHolder {
		public TextView mTextView;
	}
}
