package com.bfyd.easypay.serial;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.widget.TextView;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

/**
 * Created by zyk on 2016/8/11.
 */
public class Printer {

	private ProlificSerialDriver mPrintSerialDriver;
	private UsbManager mUsbManager;
	private BluetoothDevice device;
	private BluetoothSocket socket;
	private OutputStream mOutputStream;
	private int mType = 0;

	public static int USBPrinter = 0;
	public static int BluetoothPrinter = 1;

	public Printer(Context context,int type){
		this.mType = type;
		if(type == USBPrinter){
			usbPrinter(context);
		}else if(type == BluetoothPrinter){
//			bluetoothPrinter(context);
		}else{
			usbPrinter(context);
		}
	}

	public void setDriver(BluetoothDevice device, TextView message){
		this.device = device;
		bluetoothPrinter(message);
	}

	private void usbPrinter(Context context){
		mUsbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE); // 获取
		HashMap<String, UsbDevice> deviceList = mUsbManager.getDeviceList();
		System.out.println("deviceList is null:" + deviceList.isEmpty());
		if (!deviceList.isEmpty()) {
			Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
			while (deviceIterator.hasNext()) {
				UsbDevice device = deviceIterator.next();
				// 保存设备VID和PID
				int VendorID = device.getVendorId();
				int ProductID = device.getProductId();
				System.out.println("device:"+device);
				System.out.println("生产商 vendorId:" + VendorID);
				System.out.println("产品 ProductID:" + ProductID);
				SharedPreferences sp = context.getSharedPreferences("serial", Context.MODE_PRIVATE);
				// 保存匹配到的设备
				if((VendorID == 26728 && ProductID == 1536) | (VendorID == 1155 && ProductID == 1803)){//打印机
					mPrintSerialDriver = new ProlificSerialDriver(context, device, true);
					try {
						mPrintSerialDriver.setup(9600);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	private void bluetoothPrinter(TextView message){
		if(device == null){
			new IOException("请先setDriver");
			return ;
		}
		new Thread(){
			@Override
			public void run() {
				super.run();
				try {
					if(mOutputStream != null){
						mOutputStream.close();
					}
					if(socket != null){
						if(socket.isConnected()){
							socket.close();
						}
						socket = null;
					}
					socket = device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
					socket.connect();
					mOutputStream = socket.getOutputStream();
//			message.setText("已链接:"+device.getName());
					System.out.println("已链接:"+device.getName());
					byte [] data = {(byte)0XB4, (byte)0XF2, (byte)0XD3, (byte)0XA1, (byte)0XBB, (byte)0XFA, (byte)0XB2, (byte)0XE2, (byte)0XCA, (byte)0XD4, (byte)0XD2, (byte)0XB3};
					mOutputStream.write(data);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}.start();

//		message.setText("正在链接:"+device.getName());


	}

	/**
	 * @param data
	 * @return 当tpye 为BluetoothPrinter 时 成功打印返回 1，否则返回0
	 */
	public int print(byte [] data){
		if(mType == USBPrinter) {
			return mPrintSerialDriver.write(data, data.length);
		}else if(mType == BluetoothPrinter){
			try {
				System.out.println("调用蓝牙打印机打印");
				mOutputStream.write(data, 0, data.length);
				mOutputStream.flush();
				return 1;
			} catch (IOException e) {
//				e.printStackTrace();
				return 0;
			}
		}else{
			return mPrintSerialDriver.write(data, data.length);
		}
	}
}
