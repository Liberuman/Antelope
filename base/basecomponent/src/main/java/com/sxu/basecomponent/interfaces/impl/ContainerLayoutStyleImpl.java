package com.sxu.basecomponent.interfaces.impl;

import android.content.Context;
import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.sxu.basecomponent.interfaces.IContainerLayoutStyle;
import com.sxu.basecomponent.uiwidget.ToolbarEx;

/*******************************************************************************
 * Description: 默认实现的Activity/Fragment的基本样式
 *
 * Author: Freeman
 *
 * Date: 2018/7/30
 *
 * Copyright: all rights reserved by Freeman.
 *******************************************************************************/

public class ContainerLayoutStyleImpl implements IContainerLayoutStyle {

	private ToolbarEx toolbar;
	/**
	 * 页面的根布局
	 */
	private ViewGroup containerLayout;

	/**
	 * 真实的页面组件
	 */
	private final IContainerLayoutStyle containerLayoutProxy;

	public ContainerLayoutStyleImpl(@NonNull IContainerLayoutStyle containerLayoutProxy) {
		this.containerLayoutProxy = containerLayoutProxy;
	}

	@Override
	public int getToolbarStyle() {
		return containerLayoutProxy.getToolbarStyle();
	}

	@Override
	public boolean isImmersion() {
		return containerLayoutProxy.isImmersion();
	}

	private void initContainerLayout(Context context) {
		switch (getToolbarStyle()) {
			case TOOL_BAR_STYLE_NORMAL:
				containerLayout = createNormalToolbarLayout(context);
				break;
			case TOOL_BAR_STYLE_TRANSPARENT:
				containerLayout = createTransparentToolbarLayout(context);
				break;
			default:
				containerLayout = new FrameLayout(context);
				break;
		}
	}

	/**
	 * 获取Toolbar
	 * @return
	 */
	public ToolbarEx getToolbar() {
		if (toolbar == null) {
			throw new NullPointerException("getToolbarStyle should return TOOL_BAR_STYLE_NORMAL or TOOL_BAR_STYLE_TRANSPARENT");
		}
		return toolbar;
	}

	/**
	 * 获取页面根布局
	 * @return
	 */
	@Override
	public ViewGroup getContainerLayout(Context context) {
		if (containerLayout == null) {
			initContainerLayout(context);
		}
		return containerLayout;
	}

	/**
	 * 创建线性的布局
	 * @return
	 */
	private ViewGroup createNormalToolbarLayout(Context context) {
		LinearLayout containerLayout = new LinearLayout(context);
		containerLayout.setOrientation(LinearLayout.VERTICAL);
		toolbar = new ToolbarEx(context);
		toolbar.isImmersion(isImmersion());
		containerLayout.addView(toolbar);
		return containerLayout;
	}

	/**
	 * 创建层叠式布局
	 * @return
	 */
	private ViewGroup createTransparentToolbarLayout(Context context) {
		FrameLayout containerLayout = new FrameLayout(context);
		toolbar = new ToolbarEx(context);
		toolbar.isImmersion(isImmersion());
		toolbar.setBackgroundColor(Color.TRANSPARENT);
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		containerLayout.addView(toolbar, params);

		return containerLayout;
	}
}
