package com.bfyd.easypay.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.bfyd.easypay.serial.CustomerDisplayEntity;
import com.bfyd.easypay.serial.PrinterDataEntity;
import com.bfyd.easypay.serial.ProlificSerialDriver;
import com.bfyd.easypay.serial.SerialEntity;
import com.bfyd.easypay.utils.HexDump;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by zyk on 2016/7/28.
 * pl2303串口
 */
public class ProlificSerialService extends Service {

	private OnReceivedMessageListener onReceivedMessage;
	private ProlificSerialDriver mSerialDriver;
	private UsbManager mUsbManager;
	private Thread receiveThread;
	private boolean isStop = false;
	private boolean isCustomerDisplay = true;

	@Override
	public void onCreate() {
		super.onCreate();
		//第一次被创建的时候调用
		System.out.println("Service onCreate");
		mUsbManager = (UsbManager) getSystemService(Context.USB_SERVICE); // 获取
		enumerateDevice();
		if(mSerialDriver != null) {
			//开始监听接收数据
			receiveThread = new Thread(){
				@Override
				public void run() {
					super.run();
					while(!isStop){
						try {
							byte[] data = mSerialDriver.read();
							if(data != null && data.length >0) {
								processReceiveData(data);
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			};
			receiveThread.start();
		}else{
			sendMessage(1, "SerialDriver is null",null);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if(receiveThread != null){
			isStop = true;
			receiveThread.interrupt();
		}
	}

	private void processReceiveData(byte[] data){
		if(data.length > 0) {
			int len = data.length;
			String s = "";
			for(int i=0;i<len;i++){
				s += (data[i]& 0xFF)+"  ";
			}
			System.out.println("data:"+s);
			String str = HexDump.dumpHexString(data);
			sendMessage(0, str,data);
		}
	}

	private void sendMessage(int flag, String message ,byte []data){
		if(onReceivedMessage != null){
			System.out.println("收到的信息:"+message);
			onReceivedMessage.print(data);
			if(isCustomerDisplay) {
				onReceivedMessage.onReceivedMessage(flag, message, new CustomerDisplayEntity(data));
			}else{
				onReceivedMessage.onReceivedMessage(flag, message, new PrinterDataEntity(data));
			}
		}
	}

	private void enumerateDevice() {
		if (mUsbManager == null) {
			mUsbManager = (UsbManager) getSystemService(Context.USB_SERVICE); // 获取
		}
		HashMap<String, UsbDevice> deviceList = mUsbManager.getDeviceList();
		System.out.println("deviceList is null:" + deviceList.isEmpty());
		if (!deviceList.isEmpty()) {
			Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
//			Iterator<Map.Entry<String, UsbDevice>> entries = deviceList.entrySet().iterator();
//			//Name:/dev/bus/usb/001/002
//			while (entries.hasNext()){
//				Map.Entry<String,UsbDevice> entry = entries.next();
//				System.out.println("entry.Key:"+entry.getKey()+"   entry.values:"+entry.getValue());
//			}
			Context context = getApplicationContext();
			while (deviceIterator.hasNext()) {
				UsbDevice device = deviceIterator.next();
				// 保存设备VID和PID
				int VendorID = device.getVendorId();
				int ProductID = device.getProductId();
				System.out.println("device:"+device);
				System.out.println("生产商 vendorId:" + VendorID);
				System.out.println("产品 ProductID:" + ProductID);
				SharedPreferences sp = getApplicationContext().getSharedPreferences("serial", Context.MODE_PRIVATE);
				// 保存匹配到的设备
				if (VendorID == sp.getInt("VendorId",1659) && ProductID == sp.getInt("Product",8963)) {
					mSerialDriver = new ProlificSerialDriver(context, device);
					try {
						mSerialDriver.setup(sp.getInt("baudRate",2400));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}else if(VendorID == 26728 && ProductID == 1536){//打印机
//					mPrintSerialDriver = new ProlificSerialDriver(context, device, true);
//					try {
//						mPrintSerialDriver.setup(9600);
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
				}
			}
		}
	}

	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		return new MsgBinder();
	}


	public class MsgBinder extends Binder {
		public void setOnReceiced(OnReceivedMessageListener rml){
			onReceivedMessage = rml;
		}

		public void setCustomerDisplay(boolean iscd){
			isCustomerDisplay = iscd;
		}
	}

	public interface OnReceivedMessageListener{
		/**
		 * @param flag 0 正常信息 其他值 错误信息
		 * @param errorMsg 错误信息 flag为其他值时的提示信息
		 * @param se 解析出来的数据
		 */
		void onReceivedMessage(int flag, String errorMsg, SerialEntity se);

		/**
		 * 把接到的数据原样输出到打印机
		 * @param data
		 */
		void print(byte [] data);
	}
}
