package com.sxu.basecomponent.interfaces.impl;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

import com.sxu.basecomponent.R;
import com.sxu.basecomponent.adapter.FragmentAdapter;
import com.sxu.basecomponent.interfaces.IPagerViewProcess;
import com.sxu.basecomponent.interfaces.InstanceFactory;
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
import androidx.viewpager.widget.ViewPager;

/*******************************************************************************
 *
 *
 * @author: Freeman
 *
 * @date: 2020/5/21
 *
 * Copyright: all rights reserved by Freeman.
 *******************************************************************************/
public class PagerViewProcessImpl<T> implements IPagerViewProcess {

	private boolean indicatorIsTop;
	private Context context;
	private IPagerViewProcess pagerViewProcess;

	private int indicatorLayoutHeight = DisplayUtil.dpToPx(30);

	public PagerViewProcessImpl(Context context, @NonNull IPagerViewProcess pagerViewProcess, boolean indicatorIsTop) {
		this.context = context;
		this.pagerViewProcess = pagerViewProcess;
		this.indicatorIsTop = indicatorIsTop;
	}

	@Override
	public View getContentLayout(FragmentManager fm) {
		MagicIndicator indicatorLayout = new MagicIndicator(context);
		ViewPager viewPager = new ViewPager(context);
		viewPager.setId(R.id.view_pager);
		FrameLayout contentLayout = new FrameLayout(context);
		FrameLayout.LayoutParams indicatorParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
			indicatorLayoutHeight);
		FrameLayout.LayoutParams pagerParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
			ViewGroup.LayoutParams.MATCH_PARENT);
		if (indicatorIsTop) {
			indicatorParams.gravity = Gravity.TOP;
			pagerParams.topMargin = indicatorLayoutHeight;
		} else {
			indicatorParams.gravity = Gravity.BOTTOM;
			pagerParams.bottomMargin = indicatorLayoutHeight;
		}
		contentLayout.addView(indicatorLayout, indicatorParams);
		contentLayout.addView(viewPager, pagerParams);
		ViewCompat.setElevation(indicatorLayout, DisplayUtil.dpToPx(4));
		setComponentStyle(indicatorLayout, viewPager);

		initIndicatorStyle(indicatorLayout, viewPager);
		viewPager.setAdapter(new FragmentAdapter(fm, getDataList(), getInstanceFactory()));

		return contentLayout;
	}

	@Override
	public void initIndicatorStyle(MagicIndicator indicatorLayout, final ViewPager viewPager) {
		final List<String> tabList = getTabList();
		CommonNavigator commonNavigator = new CommonNavigator(context);
		commonNavigator.setScrollPivotX(0.8f);
		final int normalColor = ContextCompat.getColor(context, R.color.white_60);
		final int selectedColor = ContextCompat.getColor(context, R.color.white);
		CommonNavigatorAdapter navigatorAdapter = new CommonNavigatorAdapter() {
			@Override
			public int getCount() {
				return getTabList().size();
			}

			@Override
			public IPagerTitleView getTitleView(Context context, final int index) {
				ColorTransitionPagerTitleView titleView = new ColorTransitionPagerTitleView(context);
				titleView.setTextSize(12);
				titleView.setNormalColor(normalColor);
				titleView.setSelectedColor(selectedColor);
				titleView.setText(tabList.get(index));
				titleView.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						viewPager.setCurrentItem(index, false);
					}
				});
				return titleView;
			}

			@Override
			public IPagerIndicator getIndicator(Context context) {
				LinePagerIndicator linePagerIndicator = new LinePagerIndicator(context);
				linePagerIndicator.setStartInterpolator(new AccelerateInterpolator());
				linePagerIndicator.setEndInterpolator(new DecelerateInterpolator(1.6f));
				linePagerIndicator.setColors(selectedColor);
				linePagerIndicator.setLineHeight(DisplayUtil.dpToPx(2));
				linePagerIndicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
				return linePagerIndicator;
			}
		};

		commonNavigator.setAdjustMode(true);
		commonNavigator.setAdapter(navigatorAdapter);
		indicatorLayout.setNavigator(commonNavigator);
		ViewPagerHelper.bind(indicatorLayout, viewPager);
	}

	@Override
	public void setComponentStyle(MagicIndicator indicatorLayout, ViewPager viewPager) {
		pagerViewProcess.setComponentStyle(indicatorLayout, viewPager);
	}

	@NonNull
	@Override
	public List<String> getTabList() {
		return pagerViewProcess.getTabList();
	}

	@NonNull
	@Override
	public List<T> getDataList() {
		return pagerViewProcess.getDataList();
	}

	@NonNull
	@Override
	public InstanceFactory<T, Fragment> getInstanceFactory() {
		return pagerViewProcess.getInstanceFactory();
	}
}
