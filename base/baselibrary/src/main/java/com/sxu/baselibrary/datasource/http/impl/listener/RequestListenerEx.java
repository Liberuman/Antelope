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
public interface RequestListenerEx<T> extends RequestListener {

	/**
	 * 网络请求成功时被调用
	 * @param response
	 * @param msg
	 */
	void onSuccess(T response, String msg);

	/**
	 * 网络请求失败时被调用
	 * @param response 应答数据
	 * @param code 应答Code
	 * @param msg 应答消息提示
	 */
	void onFailure(T response, int code, String msg);
}
