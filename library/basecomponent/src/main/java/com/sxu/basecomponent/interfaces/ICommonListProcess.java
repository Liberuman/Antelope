package com.sxu.basecomponent.interfaces;

import android.content.Context;
import android.view.View;

import com.sxu.basecomponent.adapter.BaseCommonRecyclerAdapter;

import net.lucode.hackware.magicindicator.MagicIndicator;

import java.util.List;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
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
public interface ICommonListProcess<T> {

	/**
	 * 请求数据
	 * @param currentPage
	 */
	void requestListData(int currentPage);

	/**
	 * 数据请求完成后刷新UI
	 * @param dataList
	 */
	void requestComplete(List dataList, int currentPage);

	/**
	 * 是否可下拉刷新
	 * @return
	 */
	boolean canRefresh();

	/**
	 * 是否可加载更多
	 * @return
	 */
	boolean canLoadMore();

	/**
	 * 分页时每页的数量, 默认为0。如果设置了则可在返回的数据不满一页时自动添加【暂无更多数据】提示
	 * @return
	 */
	int getPageSize();

	/**
	 * 初始化布局
	 */
	View getContentLayout();

	/**
	 * 设置下拉刷新和RecyclerView的样式
	 * @param refreshLayout
	 * @param recyclerView
	 */
	void initContentViews(View refreshLayout, RecyclerView recyclerView);

	/**
	 * 获取子View的布局
	 * @return
	 */
	@LayoutRes int getItemLayoutRes();

	/**
	 * 创建加载布局
	 * @return
	 */
	View createLoadingView();

	/**
	 * 创建没有更多数据的布局
	 * @return
	 */
	View createNoMoreDataView();

	/**
	 * 为RecyclerView中的每一项绑定数据
	 * @param viewHolder
	 * @param itemData
	 * @param position
	 */
	void setContentValue(BaseCommonRecyclerAdapter.RecyclerViewHolder viewHolder, T itemData, int position);

	/**
	 * 为不同类型的View绑定数据，例如HeaderView和FooterView
	 * @param viewHolder
	 */
	void setOtherContentValue(BaseCommonRecyclerAdapter.RecyclerViewHolder viewHolder);

	/**
	 * 根据类型为不同的Item创建View, 例如HeaderView和FooterView
	 * @param itemType
	 * @return
	 */
	View createOtherItemView(int itemType);
}
