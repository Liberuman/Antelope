package com.sxu.baselibrary.datasource.http.impl.interceptor;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sxu.baselibrary.commonutils.CollectionUtil;
import com.sxu.baselibrary.datasource.cache.KVCacheManager;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Cookie;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/******************************************************************************
 * Description: Cookie拦截器, 负责Cookie的管理
 *
 * Author: Freeman
 *
 * Date: 2018/01/16
 *
 * Copyright: all rights reserved by Freeman.
 *******************************************************************************/
public class CookieInterceptor implements Interceptor {

	private static Map<String, String> cookieMap;

	@Override
	public Response intercept(Chain chain) throws IOException {
		// 使用CookieMap保存，避免每次都从SharePreference读取数据
		String cookieKey = "KEY_COOKIE";
		if (CollectionUtil.isEmpty(cookieMap)) {
			String persistCookie = KVCacheManager.getInstance().getString(cookieKey);
			if (!TextUtils.isEmpty(persistCookie)) {
				cookieMap = new Gson().fromJson(persistCookie, new TypeToken<Map<String, String>>() {
				}.getType());
			}
		}
		Request request = chain.request();
		if (!CollectionUtil.isEmpty(cookieMap)) {
			Request.Builder requestBuilder = request.newBuilder();
			for (Map.Entry<String, String> entry : cookieMap.entrySet()) {
				requestBuilder.addHeader("Cookie", entry.getKey() + "=" + entry.getValue());
			}
			request = requestBuilder.build();
		}

		Response response = chain.proceed(request);
		if (response != null) {
			List<Cookie> cookieList = Cookie.parseAll(request.url(), response.headers());
			if (!CollectionUtil.isEmpty(cookieList)) {
				if (cookieMap == null) {
					cookieMap = new LinkedHashMap<>();
				}
				for (Cookie cookie : cookieList) {
					cookieMap.put(cookie.name(), cookie.value());
				}
				KVCacheManager.getInstance().put(cookieKey, new Gson().toJson(cookieMap));
			}
		}

		return response;
	}
}
