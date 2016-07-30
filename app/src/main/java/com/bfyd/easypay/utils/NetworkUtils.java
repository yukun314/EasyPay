package com.bfyd.easypay.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.Toast;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * Created by zyk on 2016/4/28.
 */
public class NetworkUtils {

//    public static final String BaseURL="http://192.168.1.102:8080";
//
//    public static final String ServerURL = BaseURL+"/AppInterface/update";

    public static final String BaseURL = "http://emo.mobi";

    public static final String ServerURL = BaseURL + "//app.php";

    public static final String UpdateURL = ServerURL + "/update";

//    public static final String PayURL = BaseURL+"/pay";
    public static final String PayURL = "http://emocc.applinzi.com/api.php";

    public static final String MessageServerURL = "tcp://192.168.1.104:1883";

    public static String GetIp(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = manager.getActiveNetworkInfo();
        if (activeNetworkInfo == null) {
            Toast.makeText(context, "请您打开网络", Toast.LENGTH_LONG).show();
        } else {
            NetworkInfo wifiInfoNetworkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (wifiInfoNetworkInfo != null && wifiInfoNetworkInfo.isConnected()) {
                return GetWifiIp(context);
            } else {
                return getLocalIpAddress(context);
            }
        }
        return "0";
    }

    public static String GetWifiIp(Context context) {
        WifiManager wifiManager = ((WifiManager) context.getSystemService(Context.WIFI_SERVICE));
        DhcpInfo dhcpInfo = wifiManager.getDhcpInfo();
        String ip = intToIp(dhcpInfo.ipAddress);
        return ip;
    }

    public static String intToIp(int paramInt) {
        return (paramInt & 0xFF) + "." + (0xFF & paramInt >> 8) + "." + (0xFF & paramInt >> 16) + "."
                + (0xFF & paramInt >> 24);
    }

    public static String getLocalIpAddress(Context context) {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            Toast.makeText(context, "网络错误"+ex.toString(), Toast.LENGTH_LONG).show();
        }
        return "0";
    }
}
