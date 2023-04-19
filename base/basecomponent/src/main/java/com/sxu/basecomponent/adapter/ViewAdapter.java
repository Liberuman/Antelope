package com.sxu.basecomponent.adapter;

import android.view.View;
import android.view.ViewGroup;

import com.sxu.basecomponent.interfaces.InstanceFactory;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

/*******************************************************************************
 * 针对内容为View的通用PagerAdapter
 *
 * @author: Freeman
 *
 * @date: 2020/5/21
 *
 * Copyright: all rights reserved by Freeman.
 *******************************************************************************/
public class ViewAdapter<T> extends PagerAdapter {

	private final List<T> dataList;
	private final InstanceFactory<T, View> instanceFactory;

	public ViewAdapter(@NonNull List<T> dataList, @NonNull InstanceFactory<T, View> instanceFactory) {
		this.dataList = dataList;
		this.instanceFactory = instanceFactory;
	}

	@Override
	public int getCount() {
		return dataList.size();
	}

	@Override
	public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
		return view.equals(object);
	}

	@NonNull
	@Override
	public Object instantiateItem(@NonNull ViewGroup container, int position) {
		View itemView = instanceFactory.newInstance(dataList.get(position), position);
		container.addView(itemView);
		return itemView;
	}

	@Override
	public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
		container.removeView((View) object);
	}
}
