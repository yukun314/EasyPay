package com.bfyd.easypay.utils;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

import com.bfyd.easypay.utils.options.IntegerOption;
import com.bfyd.easypay.utils.options.StringOption;

import java.io.File;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by zyk on 2016/3/15.
 */
public class Utils {

    private static int latitude = -1;
    private static int longitude = -1;

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
    public static String getClientId(Context context) {
        //不能为null并且长度小于65535
        return getDeviceId(context);
    }

    /**
     * 获取DeviceId
     */
    public static String getDeviceId(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String DEVICE_ID = telephonyManager.getDeviceId();
        if (DEVICE_ID == null || DEVICE_ID.length() < 2) {
            return getUUID(context);
        } else {
            return DEVICE_ID;
        }
    }


    /**
     * 得到全局唯一UUID
     */
    private static String getUUID(Context context) {
        String uuid = null;
        SharedPreferences mShare = context.getSharedPreferences("myUuid", Activity.MODE_PRIVATE);
        if (mShare != null) {
            uuid = mShare.getString("uuid", "");
        }

        if (uuid == null || uuid.length() < 2) {
            uuid = UUID.randomUUID().toString();
            SharedPreferences.Editor editor = mShare.edit();
            editor.putString("uuid", uuid);
            editor.commit();
        }
        return uuid;
    }


    public static int dp2px(int dp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics());
    }

    /**
     * 合并两个数组
     * @param first 要合并的第一个数组
     * @param second 要合并的第二个数组
     * @return
     */
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public static <T> T[] concat(T[] first, T[] second) {
        T[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }

    /**
     * 获取当前位置的经度
     * @return
     */
    public static String getLongitude(Activity context) {
        IntegerOption option = new IntegerOption("option","longitude",0);
        longitude = option.getValue();
        if(longitude <= 0) {
            getLatitudeAndLongitude(context);
            if (longitude < 0) {
                longitude = 0;//默认
            }
        }
        System.out.println("latitude:"+longitude);
        System.out.println("int latitude:"+Integer.toString(longitude));
        return String.format("%1$03d", Integer.toString(longitude));
    }

    /**
     * 获取当前位置的纬度
     * @return
     */
    public static String getLatitude(Activity context) {
        IntegerOption option = new IntegerOption("option","latitude",0);
        latitude = option.getValue();
        if(latitude <= 0) {
            getLatitudeAndLongitude(context);
            if (latitude < 0) {
                latitude = 0;//默认
            }
        }

        System.out.println("latitude:"+latitude);
        System.out.println("int latitude:"+Integer.toString(latitude));
        return String.format("%1$03d", Integer.toString(latitude));
    }

    public static void getLatitudeAndLongitude(final Activity context) {
        if(latitude > 0 || longitude > 0) {
            return;
        }
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        LocationProvider netProvider = locationManager.getProvider(LocationManager.NETWORK_PROVIDER);//通过网络定位
        System.out.println("netProvider is null");
        if (netProvider != null) {
            System.out.println("netProvider != null");
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                System.out.println("netProvider 有权限");
                Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if(location != null) {
                    System.out.println("location ！= null");
                    latitude = (int)Math.abs(location.getLatitude());
                    longitude = (int)Math.abs(location.getLongitude());
                    System.out.println("latitude:"+latitude);
                    System.out.println("longitude:"+longitude);
                    IntegerOption option1 = new IntegerOption("option","latitude",latitude);
                    option1.setValue(latitude);
                    IntegerOption option2 = new IntegerOption("option","longitude",longitude);
                    option2.setValue(longitude);
                }
            }

        } else {
            //无法定位：1、提示用户打开定位服务；2、跳转到设置界面
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, "无法定位，请打开定位服务", Toast.LENGTH_SHORT).show();
                }
            });
            Intent i = new Intent();
            i.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            context.startActivity(i);
        }
    }

    public static String getOutTradeNo(Activity context){
        Date date = new Date();
        DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String result = format.format(date);
        System.out.println("时间:"+result);
        result += getLongitude(context);
        System.out.println("+经度:"+result);
        result += StringUtils.getRandomNumberByLength(1);
        System.out.println("+1位随机数:"+result);
        result += StringUtils.getRandomNumberByLength(6);
        System.out.println("+6位随机数:"+result);
        result += getLatitude(context);
        System.out.println("+纬度:"+result);
        result += StringUtils.getRandomNumberByLength(1);
        System.out.println("+1位随机数:"+result);
        return result;
    }

}
