package com.bfyd.easypay.okhttp;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by zyk on 2016/6/14.
 */
public class OkHttpCallback <T extends BaseResponse> implements Callback {

	private final ClientCallback<T> mCallback;
	private final Class<T> clazz;

	public OkHttpCallback(ClientCallback<T> callback, Class<T> responseClass){
		this.mCallback = callback;
		clazz = responseClass;

	}
	@Override
	public void onFailure(Call call, IOException e) {
		error(e.toString(),"");
	}

	@Override
	public void onResponse(Call call, Response response) throws IOException,JsonSyntaxException {
		if(response.isSuccessful()){
			String str = response.body().string();
			if(str == null || str.length() == 0){
				error("服务器没有任何返回","");
			}else {
				try {
					Gson gson = new Gson();
					T r = gson.fromJson(str, clazz);
					r.body = str;
					mCallback.onResponse(r);
				} catch (JsonSyntaxException e) {
					error(e.toString(), str);
				}
			}
		} else {
			String message = response.message();
			if(message == null) {
				message = "unknown";
			}
			error(message,"");
		}
	}

	private void error(String msg, String body){
		T t = getAClass();
		if(t != null){
			t.returnCode = 999;
			t.returnMsg = msg;
			t.body = body;
		}
		mCallback.onResponse(t);
	}

	private T getAClass(){
		try {
			return  clazz.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
}
