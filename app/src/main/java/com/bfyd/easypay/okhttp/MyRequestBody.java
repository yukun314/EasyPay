package com.bfyd.easypay.okhttp;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;

/**
 * Created by zyk on 2016/6/15.
 */
public class MyRequestBody extends RequestBody {

	private final String parameter;

	public MyRequestBody(String parameter){
		this.parameter = parameter;
	}
	@Override
	public MediaType contentType() {
		return MediaType.parse("application/x-www-form-urlencoded");
	}

	@Override
	public void writeTo(BufferedSink sink) throws IOException {
		Buffer buffer = sink.buffer();
		buffer.writeUtf8("parameter");
		buffer.writeByte('=');
		buffer.writeUtf8(parameter);
	}
}
