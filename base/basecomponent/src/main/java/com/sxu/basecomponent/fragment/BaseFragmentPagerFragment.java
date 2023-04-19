package com.sxu.basecomponent.fragment;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.core.view.ViewCompat;
import androidx.viewpager.widget.ViewPager;

import com.sxu.basecomponent.R;
import com.sxu.basecomponent.interfaces.IPagerViewProcess;
import com.sxu.basecomponent.interfaces.impl.PagerViewProcessImpl;
import com.sxu.basecomponent.utils.PrivateScreenUtil;

import net.lucode.hackware.magicindicator.MagicIndicator;

/*******************************************************************************
 * ViewPager + Fragment类型的页面基类
 *
 * @author: Freeman
 *
 * @date: 2020/5/20
 *
 * Copyright: all rights reserved by Freeman.
 *******************************************************************************/
public abstract class BaseFragmentPagerFragment<T> extends BaseFragment implements IPagerViewProcess {

	@Override
	public int getLayoutResId() {
		return 0;
	}

	@Override
	public View getContentView() {
		// 创建默认的布局
		int indicatorLayoutHeight = PrivateScreenUtil.dpToPx(30);
		MagicIndicator indicatorLayout = new MagicIndicator(context);
		indicatorLayout.setId(R.id.indicator);
		ViewPager viewPager = new ViewPager(context);
		viewPager.setId(R.id.view_pager);
		FrameLayout contentLayout = new FrameLayout(context);
		FrameLayout.LayoutParams indicatorParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
				indicatorLayoutHeight);
		FrameLayout.LayoutParams pagerParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);
		if (getIndicatorGravity() == Gravity.TOP) {
			indicatorParams.gravity = Gravity.TOP;
			pagerParams.topMargin = indicatorLayoutHeight;
		} else {
			indicatorParams.gravity = Gravity.BOTTOM;
			pagerParams.bottomMargin = indicatorLayoutHeight;
		}
		contentLayout.addView(indicatorLayout, indicatorParams);
		contentLayout.addView(viewPager, pagerParams);
		ViewCompat.setElevation(indicatorLayout, PrivateScreenUtil.dpToPx(4));
		return contentLayout;
	}

	@Override
	public boolean needRequest() {
		return false;
	}

	@Override
	public void initViews() {
		View containerLayout = getContainerLayout(context);
		MagicIndicator indicator = containerLayout.findViewById(R.id.indicator);
		ViewPager viewPager = containerLayout.findViewById(R.id.view_pager);
		if (indicator == null || viewPager == null) {
			return;
		}
		PagerViewProcessImpl pagerViewProcess = new PagerViewProcessImpl<T>(context, this);
		pagerViewProcess.initIndicatorStyle(indicator, viewPager);
	}

	@Override
	public void bindDataForView() {

	}

	@Override
	public void initIndicatorStyle(MagicIndicator indicatorLayout, ViewPager viewPager) {

	}
}
