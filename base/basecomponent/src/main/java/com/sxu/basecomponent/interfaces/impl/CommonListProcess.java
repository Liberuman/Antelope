package com.sxu.basecomponent.interfaces.impl;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.sxu.basecomponent.interfaces.ICommonListProcess;

import java.util.ArrayList;
import java.util.List;

/*******************************************************************************
 * 列表页面默认的逻辑实现
 *
 * @author: Freeman
 *
 * @date: 2020/5/22
 *
 * Copyright: all rights reserved by Freeman.
 *******************************************************************************/
public class CommonListProcess<T> implements ICommonListProcess<T> {

	/**
	 * 当前页，默认从第一页开始
	 */
	private int currentPage = 1;

	/**
	 * 是否还有更多数据
	 */
	private boolean hasMoreData = true;

	/**
	 * 真实的列表处理器
	 */
	private final ICommonListProcess listProcess;

	/**
	 * 列表数据集合
	 */
	private final List<T> contentListData = new ArrayList<>();

	private final Context context;
	private RecyclerView recyclerView;
	private SmartRefreshLayout refreshLayout;
	private BaseQuickAdapter contentAdapter = null;

	public CommonListProcess(Context context, ICommonListProcess listProcess) {
		this.context = context;
		this.listProcess = listProcess;
	}

	@Override
	public int getPageSize() {
		return listProcess.getPageSize();
	}

	@Override
	public void initContentViews(SmartRefreshLayout refreshLayout, RecyclerView recyclerView) {
		this.refreshLayout = refreshLayout;
		this.recyclerView = recyclerView;

		RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
		recyclerView.setLayoutManager(layoutManager);
		setContentAdapter();

		refreshLayout.setEnableRefresh(canRefresh());
		refreshLayout.setEnableLoadMore(canLoadMore());
		if (canRefresh()) {
			refreshLayout.setOnRefreshListener(new OnRefreshListener() {
				@Override
				public void onRefresh(@NonNull RefreshLayout refreshLayout) {
					currentPage = 1;
					hasMoreData = true;
					requestListData(currentPage);
				}
			});
		}
		if (canLoadMore()) {
			refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
				@Override
				public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
					if (hasMoreData && !recyclerView.canScrollVertically(1)) {
						requestListData(++currentPage);
					}
				}
			});
		}

		listProcess.initContentViews(refreshLayout, recyclerView);
	}

	@Override
	public void initAdapter(BaseQuickAdapter adapter) {
		listProcess.initAdapter(adapter);
	}

	@Override
	public int getItemLayoutRes() {
		return listProcess.getItemLayoutRes();
	}

	private void setContentAdapter() {
		if (contentAdapter != null) {
			contentAdapter.notifyDataSetChanged();
			return;
		}

		contentAdapter = new BaseQuickAdapter<T, BaseViewHolder>(getItemLayoutRes(), contentListData) {

			@Override
			protected void convert(@NonNull BaseViewHolder baseViewHolder, T t) {
				if (getItemViewType(baseViewHolder.getAdapterPosition()) != 0) {
					setOtherContentValue(baseViewHolder, t, baseViewHolder.getAdapterPosition());
				} else {
					setContentValue(baseViewHolder, t, baseViewHolder.getAdapterPosition());
				}
			}

			@NonNull
			@Override
			protected BaseViewHolder onCreateDefViewHolder(@NonNull ViewGroup parent, int viewType) {
				if (viewType == 0) {
					return super.onCreateDefViewHolder(parent, viewType);
				} else {
					return new BaseViewHolder(createOtherItemView(viewType));
				}
			}

			@Override
			public int getItemViewType(int position) {
				return CommonListProcess.this.getItemViewType(position);
			}

		};

		initAdapter(contentAdapter);
		recyclerView.setAdapter(contentAdapter);
	}

	@Override
	public void requestComplete(List dataList, int currentPage) {
		if (currentPage == 1) {
			contentListData.clear();
		}
		if (dataList != null && !dataList.isEmpty()) {
			int pageSize = getPageSize();
			if (pageSize == 0 || dataList.size() % pageSize == 0) {
				notifyLoadComplete(dataList);
			} else {
				notifyNoMoreData(dataList);
			}
		} else {
			notifyNoMoreData(dataList);
		}
	}

	private void notifyLoadComplete(@NonNull List<T> listData) {
		if (refreshLayout.isRefreshing()) {
			refreshLayout.finishRefresh();
		}
		if (refreshLayout.isLoading()) {
			refreshLayout.finishLoadMore();
		}
		final int lastItemIndex = contentListData.size() - 1;
		if (lastItemIndex >= 0 && contentListData.get(lastItemIndex) == null) {
			contentListData.remove(lastItemIndex);
		}
		contentListData.addAll(listData);
		setContentAdapter();
	}

	private void notifyNoMoreData(List<T> listData) {
		hasMoreData = false;
		if (refreshLayout.isLoading()) {
			refreshLayout.finishLoadMoreWithNoMoreData();
		}
	}

	@Override
	public boolean canRefresh() {
		return listProcess.canRefresh();
	}

	@Override
	public boolean canLoadMore() {
		return listProcess.canLoadMore();
	}

	@Override
	public void setContentValue(BaseViewHolder viewHolder, T itemData, int position) {
		listProcess.setContentValue(viewHolder, itemData, position);
	}

	@Override
	public void requestListData(int currentPage) {
		listProcess.requestListData(currentPage);
	}

	@Override
	public void setOtherContentValue(BaseViewHolder viewHolder, T itemData, int position) {
		listProcess.setOtherContentValue(viewHolder, itemData, position);
	}

	@Override
	public int getItemViewType(int position) {
		return listProcess.getItemViewType(position);
	}

	@Override
	public View createOtherItemView(int itemType) {
		return listProcess.createOtherItemView(itemType);
	}
}
