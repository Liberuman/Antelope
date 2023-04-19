package com.sxu.basecomponent.interfaces;

import android.content.Context;
import android.view.ViewGroup;

/*******************************************************************************
 * Description: Activity/Fragment的页面的基本样式
 *
 * Author: Freeman
 *
 * Date: 2018/7/30
 *
 * Copyright: all rights reserved by Freeman.
 *******************************************************************************/

public interface IContainerLayoutStyle {

	/**
	 * 没有Toolbar
	 */
	int TOOL_BAR_STYLE_NONE = 0;
	/**
	 * 白色背景的Toolbar
	 */
	int TOOL_BAR_STYLE_NORMAL = 1;
	/**
	 * 透明的Toolbar
	 */
	int TOOL_BAR_STYLE_TRANSPARENT = 2;

	/**
	 * 获取toolbar的样式
	 * @return
	 */
	int getToolbarStyle();

	/**
	 * 是否是沉浸式页面
	 * @return
	 */
	boolean isImmersion();

	/**
	 * 根据Toolbar的样式设置相应的布局
	 * @param context
	 * @return 返回页面容器布局
	 */
	ViewGroup getContainerLayout(Context context);
}
