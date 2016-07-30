package com.bfyd.easypay.serial;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.bfyd.easypay.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by zyk on 2016/7/30.
 */
public class ProlificSerialSettingActivity extends Activity {

	public static final String ACTION_USB_PERMISSION = "com.bfyd.easypay.USB_PERMISSION";
	private Spinner mShebei,mBotelv;
	private int baudRate;
	private ArrayAdapter<String> mShebeiAdapter;
	private List<String> mShebeiList;
	private List<UsbDevice> mShebeiListUD;
	private int mShebeiPosition;
	private UsbManager mUsbManager ;
	private PendingIntent mPermissionIntent;

	private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (ACTION_USB_PERMISSION.equals(action)) {
				synchronized (this) {
					if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
						save();
					} else {
//						System.out.println("permission denied for device " + mShebeiPosition);
					}
				}
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_prolific_setting);
		init();

		IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
		registerReceiver(mUsbReceiver, filter);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mUsbReceiver);
	}

	private void init(){
		mShebei = (Spinner) findViewById(R.id.activity_prolific_setting_shebei);
		mShebeiList = new ArrayList<>();
		mShebeiListUD = new ArrayList<>();
		mShebeiAdapter = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_item,mShebeiList);
		mShebeiAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mShebei.setAdapter(mShebeiAdapter);
		mShebei.setSelection(0);
		mShebeiPosition = 0;
		mShebei.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				mShebeiPosition = position;
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
		getDevice();

		mBotelv = (Spinner) findViewById(R.id.activity_prolific_setting_botelv);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.BaudRate_Var, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mBotelv.setAdapter(adapter);
		mBotelv.setSelection(2);
		mBotelv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				String newBaudRate = parent.getItemAtPosition(position).toString();
				try	{
					baudRate= Integer.parseInt(newBaudRate);
				}catch (NumberFormatException e) {}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		Button button = (Button) findViewById(R.id.activity_prolific_setting_save);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				save();
			}
		});
	}

	private void getDevice(){
		mUsbManager = (UsbManager) getSystemService(Context.USB_SERVICE); // 获取
		HashMap<String, UsbDevice> deviceList = mUsbManager.getDeviceList();
		if (!deviceList.isEmpty()) {
			Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
			while (deviceIterator.hasNext()) {
				UsbDevice device = deviceIterator.next();
				// 保存设备VID和PID
				int VendorID = device.getVendorId();
				int ProductID = device.getProductId();
				mShebeiListUD.add(device);
				if(Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
					mShebeiList.add(device.getProductName());
				}else{
					mShebeiList.add(device.getDeviceName());
				}
				System.out.println("device:"+device);
				System.out.println("生产商 vendorId:" + VendorID);
				System.out.println("产品 ProductID:" + ProductID);
				// 保存匹配到的设备

			}
			mShebeiAdapter.notifyDataSetChanged();
		}
	}

	private void save(){
		if(mShebeiListUD.size() > mShebeiPosition && mShebeiPosition > -1) {
			UsbDevice device = mShebeiListUD.get(mShebeiPosition);
			if(mUsbManager.hasPermission(device)) {
				saveParams(device);
			}else{
				mPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);
				mUsbManager.requestPermission(device,mPermissionIntent);
			}
		}
	}

	private void saveParams(UsbDevice device){
		SharedPreferences sp = getApplicationContext().getSharedPreferences("serial", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putInt("baudRate", baudRate);
		editor.putInt("VendorId", device.getVendorId());
		editor.putInt("Product", device.getProductId());
		//FIXME 这样 不能区分 两条相同的线
		editor.commit();
	}
}
