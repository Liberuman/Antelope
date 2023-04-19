package com.sxu.basecomponent.adapter;

import com.sxu.basecomponent.interfaces.InstanceFactory;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

/*******************************************************************************
 * 针对内容为Fragment的通用PagerAdapter
 *
 * @author: Freeman
 *
 * @date: 2020/5/21
 *
 * Copyright: all rights reserved by Freeman.
 *******************************************************************************/
public class FragmentAdapter<T> extends FragmentStatePagerAdapter {

	private final List<T> dataList;
	private final InstanceFactory<T, Fragment> instanceFactory;

	public FragmentAdapter(FragmentManager fm, @NonNull List<T> dataList, @NonNull InstanceFactory<T, Fragment> instanceFactory) {
		super(fm);
		this.dataList = dataList;
		this.instanceFactory = instanceFactory;
	}

	@Override
	public Fragment getItem(int position) {
		return instanceFactory.newInstance(dataList.get(position), position);
	}

	@Override
	public int getCount() {
		return dataList.size();
	}
}
