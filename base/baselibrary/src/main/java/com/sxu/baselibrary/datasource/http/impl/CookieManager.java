package com.sxu.baselibrary.datasource.http.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sxu.baselibrary.datasource.cache.KVCacheManager;

import java.net.URI;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/*******************************************************************************
 * Description: Cookie管理
 *
 * Author: Freeman
 *
 * Date: 2018/10/17
 *
 * Copyright: all rights reserved by Freeman.
 *******************************************************************************/
public class CookieManager {

	private final String COOKIE_HOST_KEY = "COOKIE";
	private Set<String> hostSet;
	private Map<String, ConcurrentHashMap<String, CookieAdapter>> cookieMap;

	private CookieManager() {
		cookieMap = new HashMap<>();
		hostSet = KVCacheManager.getInstance().getStringSet(COOKIE_HOST_KEY);
		if (hostSet == null) {
			hostSet = new HashSet<>();
		}
		Gson gson = new Gson();
		for (String key : hostSet) {
			try {
				cookieMap.put(key, (ConcurrentHashMap<String, CookieAdapter>) gson.fromJson(
						KVCacheManager.getInstance().getString(key), new TypeToken<Map<String, CookieAdapter>>(){}.getType()));
			} catch (Exception e) {
				e.printStackTrace(System.out);
			}
		}
	}

	public static CookieManager getInstance() {
		return Singleton.INSTANCE;
	}

	public void addCookie(URI uri, CookieAdapter cookie) {
		updateCookieMap(uri, cookie);

		KVCacheManager.getInstance().put(COOKIE_HOST_KEY, hostSet);
		KVCacheManager.getInstance().put(uri.getHost(), cookieMap.get(uri.getHost()) != null
				? new Gson().toJson(cookieMap.get(uri.getHost())) : "");
	}

	public void addCookie(URI uri, List<CookieAdapter> cookieList) {
		for (CookieAdapter cookie : cookieList) {
			updateCookieMap(uri, cookie);
		}

		KVCacheManager.getInstance().put(COOKIE_HOST_KEY, hostSet);
		KVCacheManager.getInstance().put(uri.getHost(), cookieMap.get(uri.getHost()) != null
				? new Gson().toJson(cookieMap.get(uri.getHost())) : "");
	}

	private void updateCookieMap(URI uri, CookieAdapter cookie) {
		if (!cookie.isExpires()) {
			if (!cookieMap.containsKey(uri.getHost())) {
				cookieMap.put(uri.getHost(), new ConcurrentHashMap<String, CookieAdapter>(8));
			}

			if (cookieMap.get(uri.getHost()) != null) {
				cookieMap.get(uri.getHost()).put(cookie.getName(), cookie);
			}
			hostSet.add(uri.getHost());
		} else {
			if (cookieMap.containsKey(uri.getHost())) {
				cookieMap.get(uri.getHost()).remove(cookie.getName());
			}
			hostSet.remove(uri.getHost());
		}
	}

	public Map<String, CookieAdapter> getCookie(URI uri) {
		return cookieMap.get(uri.getHost());
	}

	public void removeCookie(URI uri) {
		if (cookieMap.get(uri.getHost()) == null) {
			return;
		}

		cookieMap.remove(uri.getHost());
		KVCacheManager.getInstance().remove(uri.getHost());
	}

	public void clear() {
		for (Map.Entry<String, ConcurrentHashMap<String, CookieAdapter>> entry : cookieMap.entrySet()) {
			KVCacheManager.getInstance().remove(entry.getKey());
		}
		cookieMap.clear();
	}

	private final static class Singleton {
		private final static CookieManager INSTANCE = new CookieManager();
	}
}
