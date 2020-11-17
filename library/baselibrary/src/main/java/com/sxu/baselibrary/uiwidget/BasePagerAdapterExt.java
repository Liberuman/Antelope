package com.sxu.baselibrary.uiwidget;

import android.view.View;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

/*******************************************************************************
 * FileName: PageAdapterEx
 *
 * Description: 增加getTabView，使我们可以自定义TabIndicator布局
 *
 * Author: Freeman
 *
 * Version: v1.0
 *
 * Date: 16/8/4
 *******************************************************************************/
public abstract class BasePagerAdapterExt extends FragmentPagerAdapter {

	public BasePagerAdapterExt(FragmentManager fm) {
		super(fm);
	}

	/**
	 * 自定义每一项的View
	 * @param position
	 * @return
	 */
	public abstract View getTabView(int position);
}