package com.bfyd.easypay.utils;

import com.bfyd.easypay.pay.wxpay.WXConfigure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

/**
 * Created by zyk on 2016/6/22.
 */
public class WXUtils {

	/**
	 * 生成微信的签名
	 */
	public static String genSign(Map<String, Object> params) {
		ArrayList<String> list = new ArrayList<String>();
		for(Map.Entry<String,Object> entry:params.entrySet()){
			if(entry.getValue()!=""){
				String name = entry.getKey();
				if(!(name.equals("sign") || name == "sign")) {//签名字段不参与签名
					list.add(name + "=" + entry.getValue() + "&");
				}
			}
		}
		int size = list.size();
		String [] arrayToSort = list.toArray(new String[size]);
		Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < size; i ++) {
			sb.append(arrayToSort[i]);
		}
		String result = sb.toString();
		result += "key=" + WXConfigure.key;
		//Util.log("Sign Before MD5:" + result);
		System.out.println("md5之前:"+result);
		result = MD5.md5(result).toUpperCase();
		//Util.log("Sign Result:" + result);
		return result;
	}
}
