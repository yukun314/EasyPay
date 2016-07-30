package com.bfyd.easypay.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;

/**
 * Created by zyk on 2016/6/24.
 */
public class DistanceService extends Service {
	private SensorManager mManager;

	private Sensor mSensor = null;

	private SensorEventListener mListener = null;

	@Override
	public void onCreate() {
		super.onCreate();
		System.out.println("DistanceService onCreate");
		//获取系统服务SENSOR_SERVICE，返回一个SensorManager对象
		mManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		//获取距离感应器对象
		mSensor = mManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
		//注册感应器事件
		mListener = new SensorEventListener() {
			@Override
			public void onSensorChanged(SensorEvent event) {
				System.out.println("onSensorChanged");
				float[] its = event.values;
				if (its != null && event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
					System.out.println("its.length:"+its.length);
					System.out.println("its[0]:"+its[0]);
				}
			}

			@Override
			public void onAccuracyChanged(Sensor sensor, int accuracy) {

			}
		};
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		System.out.println("DistanceService onStartCommand");
		//注册监听
		mManager.registerListener(mListener, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		//取消监听
		mManager.unregisterListener(mListener);
		super.onDestroy();
	}

	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}
