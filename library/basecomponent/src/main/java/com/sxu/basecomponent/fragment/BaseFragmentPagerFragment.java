package com.sxu.basecomponent.fragment;

import android.view.View;

import com.sxu.basecomponent.R;
import com.sxu.basecomponent.activity.BaseProgressActivity;
import com.sxu.basecomponent.interfaces.IPagerViewProcess;
import com.sxu.basecomponent.interfaces.impl.PagerViewProcessImpl;

import net.lucode.hackware.magicindicator.MagicIndicator;

import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

/*******************************************************************************
 *
 *
 * @author: Freeman
 *
 * @date: 2020/5/20
 *
 * Copyright: all rights reserved by Freeman.
 *******************************************************************************/
public abstract class BaseFragmentPagerFragment<T> extends BaseProgressFragment implements IPagerViewProcess {

	private IPagerViewProcess pagerViewProcess;

	@Override
	public int getLayoutResId() {
		return 0;
	}

	@Override
	public void initViews() {
		pagerViewProcess = new PagerViewProcessImpl<T>(context, this, true);
		updateContentLayout(getContentLayout(getChildFragmentManager()));
	}

	@Override
	public void initComponent() {

	}

	@Override
	public View getContentLayout(FragmentManager fm) {
		return pagerViewProcess.getContentLayout(fm);
	}

	@Override
	public void initIndicatorStyle(MagicIndicator indicatorLayout, ViewPager viewPager) {
		pagerViewProcess.initIndicatorStyle(indicatorLayout, viewPager);
	}

	@Override
	public void setComponentStyle(MagicIndicator indicatorLayout, ViewPager viewPager) {

	}
}
