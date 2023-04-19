package com.sxu.basecomponent.interfaces.impl;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;

import com.sxu.basecomponent.R;
import com.sxu.basecomponent.interfaces.IRequestProcessor;
import com.sxu.basecomponent.interfaces.OnShowContentLayoutListener;
import com.sxu.basecomponent.uiwidget.ToolbarEx;


/*******************************************************************************
 * Description: 网络请求过程中页面的处理过程
 *
 * Author: Freeman
 *
 * Date: 2018/7/30
 *
 * Copyright: all rights reserved by Freeman.
 *******************************************************************************/

public class RequestProcessorImpl implements IRequestProcessor {

	/**
	 * 页面的根布局
	 */
	private ViewGroup containerLayout;

	/**
	 * 真实的内容布局
	 */
	private View contentLayout;

	/**
	 * Loading布局
	 */
	private View loadingLayout;

	/**
	 * 异常布局
	 */
	private View exceptionLayout;

	/**
	 * 异常类型，取值为LAYOUT_TYPE_FAILURE、LAYOUT_TYPE_EMPTY、LAYOUT_TYPE_LOGIN
	 */
	private int exceptionLayoutType;

	/**
	 * 异常布局的刷新操作
	 */
	private View.OnClickListener refreshListener;

	/**
	 * 页面布局加载监听
	 */
	private OnShowContentLayoutListener showLayoutListener;

	/**
	 * 当前页面的Activity实例
	 */
	private final Context context;

	/**
	 * 接口失败时的布局
	 */
	private final int LAYOUT_TYPE_FAILURE = 1;

	/**
	 * 数据为空时的布局
	 */
	private final int LAYOUT_TYPE_EMPTY = 2;

	/**
	 * 需要登录时的布局
	 */
	private final int LAYOUT_TYPE_LOGIN = 3;

	private final Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (activityDestroyed()) {
				removeCallbacksAndMessages(null);
				return;
			}

			handleMsg(msg);
		}
	};

	public RequestProcessorImpl(Context context) {
		this.context = context;
	}

	/**
	 * 设置当前页面内容布局的根布局
	 * @param containerLayout 当前页面的根布局
	 * @param contentLayout 真实的内容布局, 如果为null，表示执行的是请求成功后再加载布局的流程。
	 */
	public void setContainerLayout(ViewGroup containerLayout, @Nullable View contentLayout) {
		setContainerLayout(containerLayout, contentLayout, true);
	}

	/**
	 * 填充页面布局
	 * 说明：如果需要Loading，则先添加并隐藏内容布局，则添加Loading布局，否则直接添加内容布局
	 * @param containerLayout 当前页面的根布局
	 * @param contentLayout 真实的内容布局
	 * @param withLoading 页面加载时是否需要显示Loading页面
	 */
	public void setContainerLayout(ViewGroup containerLayout, @Nullable View contentLayout, boolean withLoading) {
		this.containerLayout = containerLayout;
		this.contentLayout = contentLayout;
		if (withLoading) {
			loadingLayout = loadLoadingLayout();
			ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.MATCH_PARENT);
			// 如果是TOOL_BAR_STYLE_TRANSPARENT样式，则LoadingLayout需要添加在Toolbar下一层
			if (containerLayout instanceof FrameLayout && containerLayout.getChildCount() > 0) {
				if (contentLayout != null) {
					contentLayout.setVisibility(View.GONE);
					this.containerLayout.addView(contentLayout, containerLayout.getChildCount() - 1, layoutParams);
				}
				this.containerLayout.addView(loadingLayout, containerLayout.getChildCount() - 1, layoutParams);
			} else {
				if (contentLayout != null) {
					contentLayout.setVisibility(View.GONE);
					this.containerLayout.addView(contentLayout, layoutParams);
				}
				this.containerLayout.addView(loadingLayout, layoutParams);
			}
		} else {
			ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.MATCH_PARENT);
			// 如果是TOOL_BAR_STYLE_TRANSPARENT样式，则LoadingLayout需要添加在Toolbar下一层
			if (containerLayout instanceof FrameLayout && containerLayout.getChildCount() > 0) {
				if (contentLayout != null) {
					this.containerLayout.addView(contentLayout, containerLayout.getChildCount() - 1, layoutParams);
				}
			} else {
				if (contentLayout != null) {
					this.containerLayout.addView(contentLayout, layoutParams);
				}
			}
		}
	}

	/**
	 * 设置页面的加载监听器
	 * @param showLayoutListener
	 */
	public void setOnShowContentLayoutListener(OnShowContentLayoutListener showLayoutListener) {
		this.showLayoutListener = showLayoutListener;
	}

	/**
	 * 发送消息触发页面的加载
	 * @param msg
	 */
	public void notifyLoadFinish(int msg) {
		if (activityDestroyed()) {
			return;
		}

		handler.sendEmptyMessage(msg);
	}

	/**
	 * 页面替换流程：
	 * 1. 先加载内容布局并隐藏，然后加载Loading，成功后删除loading并显示内容布局。失败后将loading更换为失败布局，
	 * 点击失败布局将布局更换为loading，继续以上步骤；
	 * 2. 直接加载loading（此时contentLayout为null），成功后将Loading更换为内容布局，失败后更换为失败布局，
	 * 点击失败布局将布局更换为loading，重复以上步骤；
	 */
	/**
	 * 网络请求完成后更换布局
	 * @param oldView
	 * @param newView
	 */
	public void updateContentView(View oldView, View newView) {
		if (oldView == null || newView == null) {
			return;
		}

		if (containerLayout != null) {
			int index = containerLayout.indexOfChild(oldView);
			if (index < 0) {
				return;
			}
			containerLayout.removeView(oldView);
			containerLayout.addView(newView, index, new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		} else {
			if (context instanceof Activity) {
				((Activity)context).setContentView(newView);
			}
		}
	}

	/**
	 * 获取ContainerLayout中最后一个布局
	 * @return
	 */
	public View getTopView() {
		int childCount = containerLayout.getChildCount();
		for (int i = childCount-1; i >= 0; i--) {
			View currentView = containerLayout.getChildAt(i);
			if (!(currentView instanceof ToolbarEx)) {
				return currentView;
			}
		}

		return null;
	}

	/**
	 * Activity是否已被销毁
	 * @return
	 */
	private boolean activityDestroyed() {
		if (!(context instanceof Activity)) {
			return false;
		}

		Activity activity = (Activity) context;
		return activity.isFinishing() || Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && activity.isDestroyed();
	}

	/**
	 * 页面的交互过程
	 * @param msg
	 */
	private void handleMsg(Message msg) {
		switch (msg.what) {
			case MSG_LOAD_FINISH:
				showContentLayout();
				break;
			case MSG_LOAD_FAILURE:
				showExceptionLayout(LAYOUT_TYPE_FAILURE);
				break;
			case MSG_LOAD_EMPTY:
				showExceptionLayout(LAYOUT_TYPE_EMPTY);
				break;
			case MSG_LOAD_NO_LOGIN:
				showExceptionLayout(LAYOUT_TYPE_LOGIN);
				break;
			default:
				throw new IllegalArgumentException("The Message can't be supported!");
		}
	}

	/**
	 * 显示真实内容布局
	 */
	private void showContentLayout() {
		if (contentLayout != null) {
			contentLayout.setVisibility(View.VISIBLE);
		}
		if (loadingLayout != null) {
			containerLayout.removeView(loadingLayout);
		}
		if (showLayoutListener != null) {
			showLayoutListener.onShowContentLayout();
		}
	}

	/**
	 * 显示异常页面
	 * @param layoutType
	 */
	private void showExceptionLayout(int layoutType) {
		if (contentLayout != null) {
			contentLayout.setVisibility(View.GONE);
		}
		if (exceptionLayout == null || exceptionLayoutType != layoutType) {
			if (layoutType == LAYOUT_TYPE_EMPTY) {
				exceptionLayout = loadEmptyLayout();
				exceptionLayout.setOnClickListener(getExceptionLayoutClickListener());
			} else if (layoutType == LAYOUT_TYPE_FAILURE) {
				exceptionLayout = loadFailureLayout();
				exceptionLayout.setOnClickListener(getExceptionLayoutClickListener());
			} else if (layoutType == LAYOUT_TYPE_LOGIN) {
				exceptionLayout = loadLoginLayout();
				exceptionLayout.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						updateContentView(exceptionLayout, loadingLayout);
					}
				});
			}
		}

		exceptionLayoutType = layoutType;
		updateContentView(loadingLayout, exceptionLayout);
	}

	/**
	 * 异常布局的点击事件
	 * @return
	 */
	private View.OnClickListener getExceptionLayoutClickListener() {
		return new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				requestData();
				// 将页面更换为Loading页面
				updateContentView(exceptionLayout, loadingLayout);
			}
		};
	}

	@Override
	public boolean needRequest() {
		return false;
	}

	@Override
	public void requestData() {

	}

	@Override
	public View loadLoadingLayout() {
		return View.inflate(context, R.layout.common_progress_layout, null);
	}

	@Override
	public View loadEmptyLayout() {
		return View.inflate(context, R.layout.common_empty_layout, null);
	}

	@Override
	public View loadFailureLayout() {
		return View.inflate(context, R.layout.common_failure_layout, null);
	}

	@Override
	public View loadLoginLayout() {
		return View.inflate(context, R.layout.common_login_layout, null);
	}
}
