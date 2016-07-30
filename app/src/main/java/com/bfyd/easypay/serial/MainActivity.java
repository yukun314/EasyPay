//package com.bfyd.easypay.serial;
//
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Iterator;
//
//import android.app.Activity;
//import android.content.Context;
//import android.hardware.usb.UsbConstants;
//import android.hardware.usb.UsbDevice;
//import android.hardware.usb.UsbDeviceConnection;
//import android.hardware.usb.UsbEndpoint;
//import android.hardware.usb.UsbInterface;
//import android.hardware.usb.UsbManager;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.util.Log;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.bfyd.easypay.serial.ProlificSerialDriver;
//
//public class MainActivity extends Activity {
//
//	private final String TAG = "mainActivity";
//
//	private ProlificSerialDriver psd;
//	private UsbManager mUsbManager;
//	private UsbDevice mUsbDevice;
//	private  TextView receiveText;
//	private Handler mHandler = new Handler(){
//
//		@Override
//		public void handleMessage(Message msg) {
//			super.handleMessage(msg);
//			if(msg.what == 0){
//				receiveText.setText((String)msg.obj);
//			}
//		}
//
//	};
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_main);
//		mUsbManager = (UsbManager) getSystemService(Context.USB_SERVICE); // 获取
//		enumerateDevice(mUsbManager);
//		if(psd != null){
//			try {
//				psd.setup(2400);
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}else{
//			System.out.println("psd is null");
//		}
//
//		final EditText sendText = (EditText) findViewById(R.id.editText1);
//
//		Button send = (Button) findViewById(R.id.button1);
//		send.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				if(psd!= null){
//					String str = sendText.getText().toString();
//					byte [] a =  str.getBytes();
//					psd.write(a, a.length);
//				}
//			}
//		});
//
//		receiveText = (TextView) findViewById(R.id.textView);
//		Button receive = (Button) findViewById(R.id.button2);
//		receive.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				if (psd != null) {
//					try {
//						byte[] a = psd.read();
//						String s = bytesToHexString(a);
////						Message message = new Message();
////						message.what = 0;
////						message.obj = s;
////						mHandler.sendMessage(message);
//						receiveText.setText(s);
//						System.out.println("接收到的数据为:" + s);
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//				}
//			}
//
//		});
//	}
//
//	/**
//	 * Convert byte[] to hex string. 把字节数组转化为字符串
//	 * 这里我们可以将byte转换成int，然后利用Integer.toHexString(int)来转换成16进制字符串。
//	 * @param src byte[] data
//	 * @return hex string
//	 */
//	private String bytesToHexString(byte[] src){
//		StringBuilder stringBuilder = new StringBuilder("");
//		if (src == null || src.length <= 0) {
//			return null;
//		}
//		for (int i = 0; i < src.length; i++) {
//			int v = src[i] & 0xFF;
//			String hv = Integer.toHexString(v);
//			if (hv.length() < 2) {
//				stringBuilder.append(0);
//			}
//			stringBuilder.append(hv+" ");
//		}
//		return stringBuilder.toString();
//	}
//
//	// 枚举设备函数
//	private void enumerateDevice(UsbManager mUsbManager) {
//		System.out.println("开始进行枚举设备!");
//		if (mUsbManager == null) {
//			System.out.println("创建UsbManager失败，请重新启动应用！");
//			// info.setText("创建UsbManager失败，请重新启动应用！");
//			return;
//		} else {
//			HashMap<String, UsbDevice> deviceList = mUsbManager.getDeviceList();
//			if (!(deviceList.isEmpty())) {
//				// deviceList不为空
//				System.out.println("deviceList is not null!");
//				Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
//				while (deviceIterator.hasNext()) {
//					UsbDevice device = deviceIterator.next();
//					// 输出设备信息
//					Log.i(TAG, "DeviceInfo: " + device.getVendorId() + " , " + device.getProductId());
//					System.out.println("DeviceInfo:" + device.getVendorId() + " , " + device.getProductId());
//					// 保存设备VID和PID
//					int VendorID = device.getVendorId();
//					int ProductID = device.getProductId();
//
//					System.out.println("vendorId:" + VendorID);
//					System.out.println("ProductID:" + ProductID);
//					// 保存匹配到的设备
//					if (VendorID == 1659 && ProductID == 8963) {
//						mUsbDevice = device; // 获取USBDevice
//						System.out.println("发现待匹配设备:" + device.getVendorId() + "," + device.getProductId());
//						Context context = getApplicationContext();
//						Toast.makeText(context, "发现待匹配设备", Toast.LENGTH_SHORT).show();
//						psd = new ProlificSerialDriver(context, mUsbDevice);
//					}
//				}
//			} else {
//				// info.setText("请连接USB设备至PAD！");
//				System.out.println("请连接USB设备至PAD！");
//				Context context = getApplicationContext();
//				Toast.makeText(context, "请连接USB设备至PAD！", Toast.LENGTH_SHORT).show();
//			}
//		}
//	}
//
//	// 寻找设备接口
//	private void getDeviceInterface() {
//		if (mUsbDevice != null) {
//			Log.d(TAG, "interfaceCounts : " + mUsbDevice.getInterfaceCount());
//			for (int i = 0; i < mUsbDevice.getInterfaceCount(); i++) {
//				UsbInterface intf = mUsbDevice.getInterface(i);
//				System.out.println("i:" + i + "  intf:" + intf.toString());
//				if (i == 0) {
//					// 保存设备接口
//					System.out.println("成功获得设备接口:" + intf.getId());
//				}
//				if (i == 1) {
//					System.out.println("成功获得设备接口:" + intf.getId());
//				}
//			}
//		} else {
//			System.out.println("设备为空！");
//		}
//
//	}
//
//	private UsbEndpoint epBulkOut;
//	private UsbEndpoint epBulkIn;
//	private UsbEndpoint epControl;
//	private UsbEndpoint epIntEndpointOut;
//	private UsbEndpoint epIntEndpointIn;
//
//	// 分配端点，IN | OUT，即输入输出；可以通过判断
//	private UsbEndpoint assignEndpoint(UsbInterface mInterface) {
//
//		for (int i = 0; i < mInterface.getEndpointCount(); i++) {
//			UsbEndpoint ep = mInterface.getEndpoint(i);
//			// look for bulk endpoint
//			if (ep.getType() == UsbConstants.USB_ENDPOINT_XFER_BULK) {
//				if (ep.getDirection() == UsbConstants.USB_DIR_OUT) {
//					epBulkOut = ep;
//					System.out.println("Find the BulkEndpointOut," + "index:" + i + "," + "使用端点号："
//							+ epBulkOut.getEndpointNumber());
//				} else {
//					epBulkIn = ep;
//					System.out.println(
//							"Find the BulkEndpointIn:" + "index:" + i + "," + "使用端点号：" + epBulkIn.getEndpointNumber());
//				}
//			}
//			// look for contorl endpoint
//			if (ep.getType() == UsbConstants.USB_ENDPOINT_XFER_CONTROL) {
//				epControl = ep;
//				System.out.println("find the ControlEndPoint:" + "index:" + i + "," + epControl.getEndpointNumber());
//			}
//			// look for interrupte endpoint
//			if (ep.getType() == UsbConstants.USB_ENDPOINT_XFER_INT) {
//				if (ep.getDirection() == UsbConstants.USB_DIR_OUT) {
//					epIntEndpointOut = ep;
//					System.out.println("find the InterruptEndpointOut:" + "index:" + i + ","
//							+ epIntEndpointOut.getEndpointNumber());
//				}
//				if (ep.getDirection() == UsbConstants.USB_DIR_IN) {
//					epIntEndpointIn = ep;
//					System.out.println(
//							"find the InterruptEndpointIn:" + "index:" + i + "," + epIntEndpointIn.getEndpointNumber());
//				}
//			}
//		}
//		if (epBulkOut == null && epBulkIn == null && epControl == null && epIntEndpointOut == null
//				&& epIntEndpointIn == null) {
//			throw new IllegalArgumentException("not endpoint is founded!");
//		}
//		return epIntEndpointIn;
//	}
//
//	private UsbDeviceConnection mDeviceConnection;
//
//	// 打开设备
//	public void openDevice(UsbInterface mInterface) {
//		if (mInterface != null) {
//			UsbDeviceConnection conn = null;
//			// 在open前判断是否有连接权限；对于连接权限可以静态分配，也可以动态分配权限
//			if (mUsbManager.hasPermission(mUsbDevice)) {
//				conn = mUsbManager.openDevice(mUsbDevice);
//			}
//
//			if (conn == null) {
//				return;
//			}
//
//			if (conn.claimInterface(mInterface, true)) {
//				mDeviceConnection = conn;
//				if (mDeviceConnection != null)// 到此你的android设备已经连上zigbee设备
//					System.out.println("open设备成功！");
//				final String mySerial = mDeviceConnection.getSerial();
//				System.out.println("设备serial number：" + mySerial);
//			} else {
//				System.out.println("无法打开连接通道。");
//				conn.close();
//			}
//		}
//	}
//
//	// 发送数据
//	private void sendMessageToPoint(byte[] buffer) {
//		// bulkOut传输
//		if (mDeviceConnection.bulkTransfer(epBulkOut, buffer, buffer.length, 0) < 0)
//			System.out.println("bulkOut返回输出为  负数");
//		else {
//			System.out.println("Send Message Succese！");
//		}
//	}
//
//	// 从设备接收数据bulkIn
//	private byte[] receiveMessageFromPoint() {
//		byte[] buffer = new byte[15];
//		if (mDeviceConnection.bulkTransfer(epBulkIn, buffer, buffer.length, 2000) < 0)
//			System.out.println("bulkIn返回输出为  负数");
//		else {
//			System.out.println("Receive Message Succese！"
//					// + "数据返回"
//					// + myDeviceConnection.bulkTransfer(epBulkIn, buffer,
//					// buffer.length, 3000)
//			);
//		}
//		return buffer;
//	}
//}
