package com.bfyd.easypay.okhttp;


import android.renderscript.Sampler;

import com.bfyd.easypay.config.Configure;
import com.bfyd.easypay.pay.wxpay.WXConfigure;
import com.bfyd.easypay.utils.MD5;
import com.bfyd.easypay.utils.NetworkUtils;
import com.google.gson.Gson;
import org.json.JSONObject;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;

/**
 * Created by zyk on 2016/5/19.
 */
public class OkHttpClientManager {
	/**
	 *
	 * @param domain 模块名
	 * @param param 参数
	 */
	public static <T extends BaseRequest> void request(final String domain,T param , final ClientCallback responseCallback) {
		//创建okHttpClient对象
		OkHttpClient mOkHttpClient = new OkHttpClient();
		String url = NetworkUtils.BaseURL+"/"+domain;
//		String url = NetworkUtils.ServerURL;
		System.out.println("url:"+url);

//		//创建一个Request
		final Request request = new Request.Builder()
				.url(url)
				.post(getParams(param))
				.build();
		Call call = mOkHttpClient.newCall(request);
		//请求加入调度

		OkHttpCallback okHttpCallbackcall = new OkHttpCallback(responseCallback,param.getResponseClass());
		call.enqueue(okHttpCallbackcall);
	}

	private static <T extends BaseRequest> RequestBody getParams(T param){
		genSign(param);
		Gson gson = new Gson();
		RequestBody formBody = new MyRequestBody(gson.toJson(param));
		return formBody;
	}

	private static <T extends BaseRequest> void genSign(T param){
		ArrayList<String> list = new ArrayList<String>();
		Field[] fields = param.getClass().getDeclaredFields();
		for (Field field : fields) {
			Object obj;
			try {
				obj = field.get(param);
				String name = field.getName();
				if (obj != null && !(name.equals("sign") | name == "sign")) {
					list.add(name + "=" + obj + "&");
				}

			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
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
		result += "key=" + Configure.key;
		result = MD5.md5(result).toUpperCase();
		param.setSign(result);
	}

}
