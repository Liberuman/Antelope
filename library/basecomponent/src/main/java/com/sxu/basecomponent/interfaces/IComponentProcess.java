package com.sxu.basecomponent.interfaces;

import android.os.Bundle;
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
	 * 预处理，加载布局之前添加相关逻辑，如设置window样式，重置切换动画，设置toolbar风格
	 */
	void pretreatment();

	/**
	 * 获取Activity加载的布局ID
	 * @return
	 */
	int getLayoutResId();

	/**
	 * 更新当前页面的布局
	 * @param contentLayout
	 */
	void updateContentLayout(View contentLayout);

	/**
	 * 为当前页面中View设置样式，事件等
	 */
	void initViews();

	/**
	 * 页面的逻辑处理过程(数据请求完成后的数据加载)
	 */
	void initComponent();

	/**
	 * 是否需要注册EventBus
	 * @return
	 */
	boolean needEventBus();
}
