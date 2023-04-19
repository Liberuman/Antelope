package com.sxu.basecomponent.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.List;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.sxu.basecomponent.uiwidget.SwipeMenuLayout;

/*******************************************************************************
 * Description: RecyclerView的通用适配器
 *
 * Author: Freeman
 *
 * Date: 2018/9/11
 *
 * Copyright: all rights reserved by Freeman.
 *******************************************************************************/
public abstract class BaseCommonAdapter<T> extends BaseQuickAdapter<T, BaseViewHolder> {

	public BaseCommonAdapter(@LayoutRes int layoutId, List<T> data) {
		super(layoutId, data);
	}

	/**
	 * 侧滑删除布局资源Id: 0表示不需要侧滑删除
	 * @return
	 */
	protected int getSlidingDeleteLayoutResId() {
		return 0;
	}

	/**
	 * 分组布局Id：0表示不启用分组，否则表示启用
	 */
	protected int getGroupLayoutResId() {
		return 0;
	}

	/**
	 * 当前位置是否是分组布局
	 */
	protected boolean isGroupLayout(int position) {
		return false;
	}

	/**
	 * 初始化分组布局
	 * @param holder
	 * @param itemData
	 * @param position
	 */
	protected void initGroupLayout(BaseViewHolder holder, T itemData, int position) {

	}

	/**
	 * 根据是否支持侧滑删除，获取真实的ItemView
	 */
	private View getRealItemView(View itemView) {
		int slidingDeleteLayoutResId = getSlidingDeleteLayoutResId();
		if (slidingDeleteLayoutResId <= 0) {
			return itemView;
		}

		// 如果支持侧滑删除，则需要讲itemView包裹在侧滑删除组件中
		SwipeMenuLayout swipeMenuLayout = new SwipeMenuLayout(itemView.getContext());
		swipeMenuLayout.addView(itemView);
		View deleteView = View.inflate(itemView.getContext(), slidingDeleteLayoutResId, null);
		swipeMenuLayout.addView(deleteView, new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
		return swipeMenuLayout;
	}

	@Override
	public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
		super.onBindViewHolder(holder, position);
		int groupLayoutResId = getGroupLayoutResId();
		if (groupLayoutResId <= 0) {
			return;
		}

		View groupView = ((ViewGroup)holder.itemView).getChildAt(0);
		View contentView = ((ViewGroup)holder.itemView).getChildAt(1);
		if (isGroupLayout(position)) {
			initGroupLayout(holder, getData().get(position), position);
			groupView.setVisibility(View.VISIBLE);
			contentView.setVisibility(View.GONE);
		} else {
			groupView.setVisibility(View.GONE);
			contentView.setVisibility(View.VISIBLE);
		}
	}

	@NonNull
	@Override
	protected BaseViewHolder createBaseViewHolder(@NonNull View view) {
		// 如果不需要分组，则执行执行
		int groupLayoutResId = getGroupLayoutResId();
		if (groupLayoutResId <= 0) {
			return super.createBaseViewHolder(getRealItemView(view));
		}

		// 需要分组时，自动添加分组的ItemView
		View groupView = View.inflate(view.getContext(), groupLayoutResId, null);
		LinearLayout containerLayout = new LinearLayout(view.getContext());
		containerLayout.setOrientation(LinearLayout.VERTICAL);
		containerLayout.addView(groupView);
		containerLayout.addView(getRealItemView(view));
		return super.createBaseViewHolder(containerLayout);
	}


	/**
	 * 最后一次点击的Item位置
	 */
	private int lastClickIndex = -1;

	/**
	 * 解决快速点击问题
	 * @param v
	 * @param position
	 */
	@Override
	protected void setOnItemClick(@NonNull View v, int position) {
		if (lastClickIndex == position) {
			return;
		}
		super.setOnItemClick(v, position);
		v.postDelayed(new Runnable() {
			@Override
			public void run() {
				lastClickIndex = -1;
			}
		}, 200);
		lastClickIndex = position;
	}
}
