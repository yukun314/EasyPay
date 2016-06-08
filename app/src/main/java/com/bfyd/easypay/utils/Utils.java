package com.bfyd.easypay.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Build;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.File;
import java.util.List;
import java.util.UUID;

/**
 * Created by zyk on 2016/3/15.
 */
public class Utils {

    /**
     * Returns the screen/display size
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public static Point getDisplaySize(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    /**
     * Shows a (long) toast
     */
    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    /**
     * Shows a (long) toast.
     */
    public static void showToast(Context context, int resourceId) {
        Toast.makeText(context, context.getString(resourceId), Toast.LENGTH_LONG).show();
    }

    /**
     * 根据手机的分辨率 dp转成px(像素)
     */
    public static float dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率px(像素)转成dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 判断App是否在运行
     */
    public static boolean isAppInForeground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        //activityManager.getRunningServices() 也可以判断service是否正在运行
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                return appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND;
            }
        }
        return false;
    }

	/**
	 * 唯一标示该设备的字符串
     * @return
     */
    public static String getClientId(Context context){
        //不能为null并且长度小于65535
        return getDeviceId(context);
    }

    /**
     * 获取DeviceId
     */
    public static String getDeviceId(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String DEVICE_ID = telephonyManager.getDeviceId();
        if(DEVICE_ID==null || DEVICE_ID.length()<2){
            return getUUID(context);
        } else {
            return DEVICE_ID;
        }
    }


    /**
     * 得到全局唯一UUID
     */
    private static String getUUID(Context context){
        String uuid = null;
        SharedPreferences mShare = context.getSharedPreferences("myUuid",Activity.MODE_PRIVATE);
        if(mShare != null){
            uuid = mShare.getString("uuid", "");
        }

        if(uuid == null || uuid.length() <2){
            uuid = UUID.randomUUID().toString();
            SharedPreferences.Editor editor = mShare.edit();
            editor.putString("uuid",uuid);
            editor.commit();
        }
        return uuid;
    }


    public static int dp2px(int dp,Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics());
    }
}
