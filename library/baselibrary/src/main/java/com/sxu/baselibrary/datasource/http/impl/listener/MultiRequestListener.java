package com.sxu.baselibrary.datasource.http.impl.listener;

import java.util.List;

/*******************************************************************************
 * 多个接口并行请求过程的监听
 *
 * @author: Freeman
 *
 * @date: 2020/5/20
 *
 * Copyright: all rights reserved by Freeman.
 *******************************************************************************/
public interface MultiRequestListener extends RequestListener {

	/**
	 * 网络请求成功时被调用
	 * @param responseList 应答数据
	 */
	void onSuccess(List<Object> responseList);

	/**
	 * 网络请求失败时被调用
	 * @param code 应答Code
	 * @param msg 应答消息提示
	 */
	void onFailure(int code, String msg);
}
