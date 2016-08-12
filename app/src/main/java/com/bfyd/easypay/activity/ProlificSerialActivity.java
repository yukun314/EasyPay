package com.bfyd.easypay.activity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bfyd.easypay.R;
import com.bfyd.easypay.serial.CustomerDisplayEntity;
import com.bfyd.easypay.serial.Printer;
import com.bfyd.easypay.serial.PrinterDataEntity;
import com.bfyd.easypay.serial.ProlificSerialSettingActivity;
import com.bfyd.easypay.serial.SerialEntity;
import com.bfyd.easypay.service.ProlificSerialService;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by zyk on 2016/7/28.
 */
public class ProlificSerialActivity extends Activity implements ProlificSerialService.OnReceivedMessageListener{

	private TextView mBluetoothMessage;
	private ListView mListView;
	private MyAdapter mAdapter;
	private List<BluetoothDevice> mList;
	private BluetoothDevice device;
	private Printer mPrinter;

	private boolean isCustomerDisplay = true;
	private CheckBox mCheckBox;
	private ImageView mImageView;
	private TextView mMessage;
	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(msg.what == 0){
				String s = mMessage.getText().toString();
				String result ;
				if(!isCustomerDisplay) {
					PrinterDataEntity pde = (PrinterDataEntity)msg.obj;
					mImageView.setImageBitmap(pde.mBitmap);
					result = pde.result;
				}else{
					result = (String)msg.obj;
				}
				System.out.println("result:"+result);
				mMessage.setText(s+"\n-->"+result);

			}
		}
	};

	private ServiceConnection conn = new ServiceConnection() {
		@Override
		public void onServiceDisconnected(ComponentName name) {

		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder binder) {
			ProlificSerialService.MsgBinder msbinder = (ProlificSerialService.MsgBinder)binder;
			msbinder.setOnReceiced(ProlificSerialActivity.this);
			if(mCheckBox!= null && mCheckBox.isChecked()) {
				msbinder.setCustomerDisplay(true);
				isCustomerDisplay = true;
			}else{
				msbinder.setCustomerDisplay(false);
				isCustomerDisplay = false;
			}
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_prolic_serial);
		mMessage = (TextView) findViewById(R.id.activity_prolic_serial_message);
		mCheckBox = (CheckBox) findViewById(R.id.activity_prolic_serial_cb);
		mImageView = (ImageView) findViewById(R.id.activity_prolic_serial_image);
		Button button = (Button) findViewById(R.id.activity_prolic_serial_button);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(ProlificSerialActivity.this, ProlificSerialSettingActivity.class);
				ProlificSerialActivity.this.startActivity(intent);
			}
		});

		Button buttons = (Button) findViewById(R.id.activity_prolic_serial_star);
		buttons.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//绑定Service
				Intent intent = new Intent();
				intent.setClass(ProlificSerialActivity.this,ProlificSerialService.class);
				bindService(intent, conn, Context.BIND_AUTO_CREATE);
			}
		});

		initBluetooth();
		mPrinter = new Printer(getApplicationContext(),Printer.BluetoothPrinter);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unbindService(conn);
	}

	//收到信息
	@Override
	public void onReceivedMessage(int flag, String message, SerialEntity se) {
		Message m = new Message();
		m.what = 0;
		if(flag == 0){
			System.out.println("收到信息:"+message);
			if(isCustomerDisplay) {
				m.obj = se.toString();
			}else{
				m.obj = se;
			}
		} else {
			System.out.println("错误:"+message);
			m.obj = message;
		}
		mHandler.sendMessage(m);
	}

	@Override
	public void print(byte[] data) {
		if(mPrinter != null){
			mPrinter.print(data);
		}
	}


	private void initBluetooth(){
		mBluetoothMessage = (TextView) findViewById(R.id.activity_prolic_serial_bluetooth_message);
		mListView = (ListView) findViewById(R.id.activity_prolic_serial_bluetooth_list);
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
			mPrinter.setDriver(device, mBluetoothMessage);
		}
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
					mBluetoothMessage.setText("请选择 要使用的蓝牙");
				} else {
					mBluetoothMessage.setText("当前无配对的蓝牙");
				}
			} else {
				mBluetoothMessage.setText("当前蓝牙设备不可用 请先开启蓝牙");
			}
		} else {
			mBluetoothMessage.setText("当前设备无蓝牙");
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
				convertView = LayoutInflater.from(ProlificSerialActivity.this.getApplicationContext()).inflate(R.layout.activity_bluetooth_item, null);
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
