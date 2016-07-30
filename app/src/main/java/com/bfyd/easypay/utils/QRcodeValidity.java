package com.bfyd.easypay.utils;

import android.os.Handler;
import com.bfyd.easypay.config.Configure;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by zyk on 2016/7/5.
 * 二维码有效期
 * 一个实例 只能同时有一个计时器在计时
 */
public class QRcodeValidity {

	public final int TimeOver = 100;
	private Timer mTimer;
	private Handler mHandler = null;


	public void starTiming(Handler handler) {
		endTiming();
		if(mHandler == null) {
			mHandler = handler;
			mTimer = new Timer();
			mTimer.schedule(new MyTimerTask(), Configure.validity*1000);
		}
	}

	public void endTiming(){
		if(mTimer != null) {
			mTimer.cancel();
			mTimer.purge();
			mTimer = null;
			mHandler = null;
		}
	}

	private class MyTimerTask extends TimerTask{
		@Override
		public void run() {
			if(mHandler != null) {
				mHandler.sendEmptyMessage(TimeOver);
				mHandler = null;
				mTimer = null;
			}
		}
	}

	public class ValidityException extends Exception{
		public ValidityException(String detailMessage) {
			super(detailMessage);
		}
	}

}
