package com.sxu.basecomponent.interfaces;

import android.view.View;

/*******************************************************************************
 * 页面的逻辑流程
 *
 * @author: Freeman
 *
 * @date: 2020/5/20
 *
 * Copyright: all rights reserved by Freeman.
 *******************************************************************************/
public interface IComponentProcess {

	/**
	 * 获取Activity加载的布局ID
	 * @return
	 */
	int getLayoutResId();

	/**
	 * 获取需要加载的布局
	 * @return
	 */
	View getContentView();

	/**
	 * 为当前页面中View设置样式
	 */
	void initViews();

	/**
	 * 为View绑定事件
	 */
	void initListener();

	/**
	 * 页面的逻辑处理过程(数据请求完成后的数据加载)
	 */
	void bindDataForView();
}
