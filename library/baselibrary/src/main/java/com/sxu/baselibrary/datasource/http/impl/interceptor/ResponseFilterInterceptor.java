package com.sxu.baselibrary.datasource.http.impl.interceptor;

import java.io.IOException;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import okhttp3.Interceptor;
import okhttp3.Response;
import okhttp3.ResponseBody;

/*******************************************************************************
 * 过滤应答数据拦截器
 *
 * @author: Freeman
 *
 * @date: 2020/6/4
 *
 * Copyright: all rights reserved by Freeman.
 *******************************************************************************/
public class ResponseFilterInterceptor implements Interceptor {

	/**
	 * 默认情况下替换空字符串、空数组、空对象
	 */
	private String regex = "\"\"|\\[\\]|\\{\\}";

	public ResponseFilterInterceptor() {

	}

	public ResponseFilterInterceptor(@NonNull String regex) {
		this.regex = regex;
	}

	@Override
	public Response intercept(Chain chain) throws IOException {
		Response response = chain.proceed(chain.request());
		ResponseBody body = response.body();
		String content;
		if (body == null || (content = body.string()) == null || content.length() == 0) {
			return response;
		}

		content = content.replaceAll(regex, "null");
		return response.newBuilder()
			.body(ResponseBody.create(body.contentType(), content))
			.build();
	}
}
