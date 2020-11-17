package com.sxu.basecomponent.activity;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

import com.sxu.basecomponent.R;
import com.sxu.basecomponent.adapter.FragmentAdapter;
import com.sxu.basecomponent.interfaces.IComponentProcess;
import com.sxu.basecomponent.interfaces.IPagerViewProcess;
import com.sxu.basecomponent.interfaces.InstanceFactory;
import com.sxu.basecomponent.interfaces.impl.PagerViewProcessImpl;
import com.sxu.baselibrary.commonutils.DisplayUtil;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
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
public abstract class BaseFragmentPagerActivity<T> extends BaseProgressActivity implements IPagerViewProcess {

	private IPagerViewProcess pagerViewProcess;

	@Override
	public int getLayoutResId() {
		return 0;
	}

	@Override
	public void initViews() {
		pagerViewProcess = new PagerViewProcessImpl<T>(this, this, true);
		updateContentLayout(getContentLayout(fragmentManager));
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
		ViewCompat.setElevation(toolbar, 0);
		indicatorLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
	}
}
