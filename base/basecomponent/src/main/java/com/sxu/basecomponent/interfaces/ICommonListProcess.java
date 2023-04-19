package com.sxu.basecomponent.interfaces;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import java.util.List;

import androidx.annotation.LayoutRes;
import androidx.recyclerview.widget.RecyclerView;

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
	 * 请求列表数据
	 * @param currentPage
	 */
	void requestListData(int currentPage);

	/**
	 * 数据请求完成后刷新UI
	 * @param dataList: 请求完成后获取的数据
	 * @param currentPage: 本次请求的页码
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
	 * 分页时每页的数量, 默认为10。如果设置了则可在返回的数据不满一页时自动添加【暂无更多数据】提示
	 * @return
	 */
	int getPageSize();

	/**
	 * 设置下拉刷新和RecyclerView的样式
	 * @param refreshLayout
	 * @param recyclerView
	 */
	void initContentViews(SmartRefreshLayout refreshLayout, RecyclerView recyclerView);

	/**
	 * 初始化Adapter 如设置Header、Footer、空页面等
	 * @param adapter
	 */
	void initAdapter(BaseQuickAdapter adapter);

	/**
	 * 获取子View的布局
	 * @return
	 */
	@LayoutRes int getItemLayoutRes();

	/**
	 * 为RecyclerView中的每一项绑定数据
	 * @param viewHolder
	 * @param itemData
	 * @param position
	 */
	void setContentValue(BaseViewHolder viewHolder, T itemData, int position);

	/**
	 * 为不同类型的View绑定数据
	 * @param viewHolder
	 * @param itemData: 当前Item对应的数据
	 * @param position: 当前Item的位置
	 */
	void setOtherContentValue(BaseViewHolder viewHolder, T itemData, int position);

	/**
	 * 获取每一项的viewType
	 * @param position
	 * @return
	 */
	int getItemViewType(int position);

	/**
	 * 为类型为不同的Item创建View
	 * @param itemType
	 * @return
	 */
	View createOtherItemView(int itemType);
}
