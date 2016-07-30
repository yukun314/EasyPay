package com.bfyd.easypay.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.bfyd.easypay.R;
import com.bfyd.easypay.app.BaseActivity;

/**
 * Created by zyk on 2016/6/24.
 */
public class SensorTest extends BaseActivity implements SensorEventListener {
	private TextView mText;
	private SensorManager mManager = null;
	private Sensor mSensor = null;
	private Sensor mSensor1 = null;

	private float oldLightValue = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sensor);
		mText = (TextView) findViewById(R.id.activity_sensor_text);
		Button button = (Button) findViewById(R.id.activity_sensor_button);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				distance();
			}
		});

		Button button1 = (Button) findViewById(R.id.activity_sensor_button1);
		button1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				light();
			}
		});
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	private void distance() {
		System.out.println("distance 开始");
		//获取系统服务SENSOR_SERVICE，返回一个SensorManager对象
		if(mManager == null) {
			mManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		}
		//获取距离感应器对象
		mSensor = mManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
		mManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
//Intent intent = new Intent(this, DistanceService.class);
//		startService(intent);
	}

	private void light(){
		if(mManager == null) {
			mManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		}
		mSensor1 = mManager.getDefaultSensor(Sensor.TYPE_LIGHT);
		mManager.registerListener(this, mSensor1, SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	protected void onDestroy() {
		//取消监听
		if(mManager != null) {
			mManager.unregisterListener(this);
		}
		super.onDestroy();
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		System.out.println("onSensorChanged");
		float[] its = event.values;
		if (its != null && event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
			if(its[0] <= 3){
				mText.setText("接近:its[0]="+its[0]);
			} else {
				mText.setText("远离:its[0]="+its[0]);
			}
		} else if (its != null && event.sensor.getType() == Sensor.TYPE_LIGHT){
			if(oldLightValue - its[0] > 0){
				mText.setText("光感应器 光线变暗 its[0]="+its[0]+"   差值为:"+(oldLightValue - its[0]));
				oldLightValue = its[0];
			} else {
				mText.setText("光感应器 光线变亮 its[0]="+its[0]+"   差值为:"+(its[0] - oldLightValue));
				oldLightValue = its[0];
			}
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		System.out.println("onAccuracyChanged:" + sensor + "  accuracy:" + accuracy);
	}
}
