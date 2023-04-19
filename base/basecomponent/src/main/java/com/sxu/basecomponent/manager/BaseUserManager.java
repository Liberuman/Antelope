package com.sxu.basecomponent.manager;

import com.google.gson.Gson;

/*******************************************************************************
 * Description: 用户会话管理
 *
 * Author: Freeman
 *
 * Date: 2018/11/21
 *
 * Copyright: all rights reserved by Freeman.
 *******************************************************************************/
public class BaseUserManager<T> {

	/**
	 * 用户信息
	 */
	private T userInfo;

	/**
	 * 用户是否已登录
	 * 默认只支持非游客模式，存在游客模式时需要手动调用updateLoginStatus设置登录状态
	 */
	private boolean isLogin;

	/**
	 * 用户id
	 */
	private long userId;

	/**
	 * 登录后的token信息
	 */
	private String token;

	private final String KEY_USER_INFO = "key_user_info";

	private static BaseUserManager instance;

	private BaseUserManager(Class<T> tClass) {
		loadUserInfo(tClass);
	}

	public static <T> BaseUserManager<T> getInstance(Class<T> tClass) {
		if (instance == null) {
			synchronized (BaseUserManager.class) {
				if (instance == null) {
					instance = new BaseUserManager(tClass);
				}
			}
		}

		return instance;
	}

	/**
	 * 读取存储的用户信息
	 * @param tClass
	 */
	private void loadUserInfo(Class<T> tClass) {
		userInfo = new Gson().fromJson(PreferencesManager.getString(KEY_USER_INFO), tClass);
		if (userInfo != null) {
			isLogin = true;
		}
	}

	/**
	 * 用户是否已登录
	 * @return
	 */
	public boolean isLogin() {
		return isLogin;
	}

	/**
	 * 设置登录状态
	 * @param isLogin
	 */
	public void updateLoginStatus(boolean isLogin) {
		this.isLogin = isLogin;
	}

	/**
	 * 获取用户信息
	 * @return
	 */
	public T getUserInfo() {
		return userInfo;
	}

	public void setToken(long userId) {
		this.userId = userId;
	}

	public long getUserId() {
		return userId;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getToken() {
		return token;
	}

	/**
	 * 设置用户信息
	 * @param userInfo
	 */
	public void setUserInfo(T userInfo) {
		this.userInfo = userInfo;
		if (userInfo != null) {
			isLogin = true;
		}
		PreferencesManager.putString(KEY_USER_INFO, new Gson().toJson(userInfo));
	}

	/**
	 * 清除用户信息，退出登录时调用
	 */
	public void clearUserInfo() {
		isLogin = false;
		userInfo = null;
		PreferencesManager.putString(KEY_USER_INFO, "");
	}
}
