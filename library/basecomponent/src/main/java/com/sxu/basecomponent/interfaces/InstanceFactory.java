package com.sxu.basecomponent.interfaces;

import android.view.View;

import androidx.fragment.app.Fragment;

/*******************************************************************************
 * PagerAdapter实例创建工厂
 *
 * @author: Freeman
 *
 * @date: 2020/5/21
 *
 * Copyright: all rights reserved by Freeman.
 *******************************************************************************/
public interface InstanceFactory<T, R> {

	/**
	 * 为PagerAdapter的每一项创建实例
	 * @param itemData
	 * @param position
	 * @return
	 */
	R newInstance(T itemData, int position);
}
