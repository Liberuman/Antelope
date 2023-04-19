package com.sxu.basecomponent.interfaces;

import net.lucode.hackware.magicindicator.MagicIndicator;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

/*******************************************************************************
 * 多页切换样式
 *
 * @author: Freeman
 *
 * @date: 2020/5/21
 *
 * Copyright: all rights reserved by Freeman.
 *******************************************************************************/
public interface IPagerViewProcess<T, R> {

	/**
	 * 设置Indicator的位置
	 * @return
	 */
	int getIndicatorGravity();

	/**
	 * 获取Tab列表
	 * @return
	 */
	@NonNull List<String> getTabList();

	/**
	 * 获取数据列表
	 * @return
	 */
	@NonNull List<T> getDataList();

	/**
	 * 设置indicatorLayout样式
	 * @param indicatorLayout
	 * @param viewPager
	 */
	void initIndicatorStyle(MagicIndicator indicatorLayout, final ViewPager viewPager);

	/**
	 * 获取创建Fragment的Factory
	 * @return
	 */
	@NonNull InstanceFactory<T, R> getInstanceFactory();
}
