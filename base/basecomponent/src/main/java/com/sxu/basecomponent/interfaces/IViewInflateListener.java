package com.sxu.basecomponent.interfaces;

import android.view.View;

/*******************************************************************************
 * View的加载监听
 *
 * @author: Freeman
 *
 * @date: 2020/5/21
 *
 * Copyright: all rights reserved by Freeman.
 *******************************************************************************/
public interface IViewInflateListener {

	/**
	 * View的加载监听
	 * @param view 加载完成的View
	 * @return
	 */
	void onViewInflated(View view);
}
