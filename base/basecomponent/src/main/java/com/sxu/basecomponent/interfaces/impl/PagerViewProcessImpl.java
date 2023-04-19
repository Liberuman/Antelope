package com.sxu.basecomponent.interfaces.impl;

import android.content.Context;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.sxu.basecomponent.R;
import com.sxu.basecomponent.adapter.FragmentAdapter;
import com.sxu.basecomponent.interfaces.IPagerViewProcess;
import com.sxu.basecomponent.interfaces.InstanceFactory;
import com.sxu.basecomponent.utils.PrivateScreenUtil;

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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
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

	private final Context context;
	private final IPagerViewProcess pagerViewProcess;

	public PagerViewProcessImpl(Context context, @NonNull IPagerViewProcess pagerViewProcess) {
		this.context = context;
		this.pagerViewProcess = pagerViewProcess;
	}

	@Override
	public void initIndicatorStyle(MagicIndicator indicatorLayout, final ViewPager viewPager) {
		// 提供的默认指示器样式
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
				linePagerIndicator.setLineHeight(PrivateScreenUtil.dpToPx(2));
				linePagerIndicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
				return linePagerIndicator;
			}
		};

		commonNavigator.setAdjustMode(true);
		commonNavigator.setAdapter(navigatorAdapter);
		indicatorLayout.setNavigator(commonNavigator);

		// 使用自定义的指示器
		pagerViewProcess.initIndicatorStyle(indicatorLayout, viewPager);
		// 绑定指示器和ViewPager
		ViewPagerHelper.bind(indicatorLayout, viewPager);

		if (context instanceof FragmentActivity) {
			viewPager.setAdapter(new FragmentAdapter(((FragmentActivity) context).getSupportFragmentManager(),
					getDataList(), getInstanceFactory()));
		}
	}

	@Override
	public int getIndicatorGravity() {
		return pagerViewProcess.getIndicatorGravity();
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
