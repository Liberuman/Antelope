package com.sxu.baselibrary.datasource.http.impl.listener;

/*******************************************************************************
 *
 *
 * @author: Freeman
 *
 * @date: 2020/3/31
 *
 * Copyright: all rights reserved by Freeman.
 *******************************************************************************/
public interface SimpleRequestListener<T> extends RequestListener {

	/**
	 * 网络请求成功时被调用
	 * @param response 应答数据
	 */
	void onSuccess(T response);

	/**
	 * 网络请求失败时被调用
	 * @param code 应答Code
	 * @param msg 应答消息提示
	 */
	void onFailure(int code, String msg);
}
