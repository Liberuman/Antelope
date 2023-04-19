package com.sxu.basecomponent.interfaces;

import android.view.View;

/*******************************************************************************
 * Description: 需要网络请求的页面处理过程的接口
 *
 * Author: Freeman
 *
 * Date: 2018/7/30
 *
 * Copyright: all rights reserved by Freeman.
 *******************************************************************************/

public interface IRequestProcessor {

	/**
	 * 数据请求成功
	 */
	int MSG_LOAD_FINISH = 0x100;
	/**
	 * 网络请求失败
	 */
	int MSG_LOAD_FAILURE  = 0x101;
	/**
	 * 数据为空
	 */
	int MSG_LOAD_EMPTY = 0x102;
	/**
	 * 未登录
	 */
	int MSG_LOAD_NO_LOGIN = 0x103;

	/**
	 * 页面加载时是否需要网络请求
	 * @return
	 */
	boolean needRequest();

	/**
	 * 请求网络数据
	 */
	void requestData();

	/**
	 * 加载Loading布局
	 * @return
	 */
	View loadLoadingLayout();

	/**
	 * 加载数据为空时的布局
	 * @return
	 */
	View loadEmptyLayout();

	/**
	 * 加载网络请求为空时的布局
	 * @return
	 */
	View loadFailureLayout();

	/**
	 * 加载需要未登录时的布局
	 * @return
	 */
	View loadLoginLayout();
}
