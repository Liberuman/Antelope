package com.sxu.basecomponent.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.LayoutRes;

/*******************************************************************************
 * Description: 通用的ListView/GridView Adapter
 *
 * Author: Freeman
 *
 * Date: 2018/7/26
 *
 * Copyright: all rights reserved by Freeman.
 *******************************************************************************/

public abstract class BaseListAdapter<T> extends BaseAdapter {

	private final Context mContext;
	private List<T> mData;
	private @LayoutRes
	final
	int[] mResId;

	public BaseListAdapter(Context context, List<T> data, @LayoutRes int resId) {
		this.mContext = context;
		this.mData = data;
		this.mResId = new int[] {resId};
	}

	public BaseListAdapter(Context context, List<T> data, @LayoutRes int[] resId) {
		this.mContext = context;
		this.mData = data;
		this.mResId = resId;
	}

	public BaseListAdapter(Context context, T[] data, @LayoutRes int resId) {
		this(context, Arrays.asList(data), resId);
	}

	@Override
	public int getCount() {
		return mData != null ? mData.size() : 0;
	}

	@Override
	public T getItem(int i) {
		return mData != null ? mData.get(i) : null;
	}

	@Override
	public long getItemId(int i) {
		return i;
	}

	/**
	 * 对于需要多种View的Adapter重写此方法，并返回与resId中布局对应的Index值，
	 * 在convert中设置值时也需要根据此值进行区分，避免NullPointException
	 * @param position
	 * @return
	 */
	@Override
	public int getItemViewType(int position) {
		return 0;
	}

	@Override
	public View getView(int i, View view, ViewGroup viewGroup) {
		ViewHolder holder;
		if (view == null) {
			view = LayoutInflater.from(mContext).inflate(mResId[getItemViewType(i)], viewGroup, false);
			holder = ViewHolder.getInstance(view);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		convert(holder , getItem(i), i);

		return view;
	}

	/**
	 * Adapter中数据的填充过程
	 * @param holder
	 * @param paramT
	 * @param position
	 */
	public abstract void convert(final ViewHolder holder, final T paramT, final int position);


	/**
	 * 更新数据，替换原有数据
	 */
	public void updateItems(List<T> items) {
		mData = new ArrayList<>(items);
		notifyDataSetChanged();
	}
}
