package com.sxu.basecomponent.interfaces.impl;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sxu.basecomponent.R;
import com.sxu.basecomponent.adapter.BaseCommonRecyclerAdapter;
import com.sxu.basecomponent.bean.ItemBaseBean;
import com.sxu.basecomponent.interfaces.ICommonListProcess;
import com.sxu.baselibrary.commonutils.CollectionUtil;
import com.sxu.baselibrary.commonutils.DisplayUtil;
import com.sxu.baselibrary.commonutils.LogUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

/*******************************************************************************
 *
 *
 * @author: Freeman
 *
 * @date: 2020/5/22
 *
 * Copyright: all rights reserved by Freeman.
 *******************************************************************************/
public class CommonListProcess<T extends ItemBaseBean> implements ICommonListProcess<T> {

	private RecyclerView recyclerView;
	private SwipeRefreshLayout refreshLayout;

	private int currentPage = 1;
	private int itemType = 0;
	private int defaultLoadingHeight;
	private boolean isLoading = true;
	private boolean hasMoreData = true;
	private Context context;
	private ICommonListProcess listProcess;
	private List<T> contentListData = new ArrayList<>();
	private BaseCommonRecyclerAdapter<T> contentAdapter = null;

	/**
	 * 数据子布局
	 */
	private final int ITEM_TYPE_DEFAULT = 0;
	/**
	 * 加载中
	 */
	private final int ITEM_TYPE_LOADING = 0x101;
	/**
	 * 没有更多数据
	 */
	private final int ITEM_TYPE_NO_MORE_DATA = 0x102;

	public CommonListProcess(Context context, ICommonListProcess listProcess) {
		this.context = context;
		this.listProcess = listProcess;
		defaultLoadingHeight = DisplayUtil.dpToPx(64);
	}

	@Override
	public int getPageSize() {
		return listProcess.getPageSize();
	}

	@Override
	public View getContentLayout() {
		View contentView = View.inflate(context, R.layout.view_common_list_layout, null);
		refreshLayout = contentView.findViewById(R.id.refresh_layout);
		recyclerView = contentView.findViewById(R.id.recycler_view);

		initContentViews(refreshLayout, recyclerView);
		setContentAdapter();
		if (canRefresh()) {
			refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
				@Override
				public void onRefresh() {
					currentPage = 1;
					isLoading = true;
					hasMoreData = true;
					refreshLayout.setRefreshing(true);
					requestListData(currentPage);
				}
			});
		} else {
			refreshLayout.setEnabled(false);
		}
		if (canLoadMore()) {
			recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
				@Override
				public void onScrolled(final @NonNull RecyclerView recyclerView, int dx, int dy) {
					super.onScrolled(recyclerView, dx, dy);
					LogUtil.i("isLoading==" + isLoading + " hasMoreData==" + hasMoreData + " bottom==" + recyclerView.canScrollVertically(1));
					if (!isLoading && hasMoreData && !recyclerView.canScrollVertically(1)) {
						isLoading = true;
						requestListData(++currentPage);
						contentListData.add(null);
						itemType = ITEM_TYPE_LOADING;
						int lastIndex = contentListData.size()-1;
						contentAdapter.notifyItemInserted(lastIndex);
						recyclerView.scrollBy(0, defaultLoadingHeight);
					}
				}
			});
		}

		return contentView;
	}

	@Override
	public void initContentViews(View refreshLayout, RecyclerView recyclerView) {
		RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
		recyclerView.setLayoutManager(layoutManager);
		RecyclerView.ItemDecoration decoration = new DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL);
		recyclerView.addItemDecoration(decoration);
		listProcess.initContentViews(refreshLayout, recyclerView);
	}

	@Override
	public int getItemLayoutRes() {
		return listProcess.getItemLayoutRes();
	}

	private void setContentAdapter() {
		if (contentAdapter == null) {
			contentAdapter = new BaseCommonRecyclerAdapter<T>(contentListData, getItemLayoutRes()) {
				@Override
				public void convert(RecyclerViewHolder viewHolder, T itemData, int position) {
					if (getItemViewType(position) == ITEM_TYPE_DEFAULT) {
						setContentValue(viewHolder, itemData, position);
					} else {
						setOtherContentValue(viewHolder);
					}
				}

				@Override
				public int getItemViewType(int position) {
					T itemData = contentListData.get(position);
					if (itemData != null) {
						return itemData.itemType;
					}

					return itemType;
				}

				@NonNull
				@Override
				public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
					if (viewType == ITEM_TYPE_DEFAULT) {
						return super.onCreateViewHolder(parent, viewType);
					} else if (viewType == ITEM_TYPE_LOADING) {
						return new RecyclerViewHolder(createLoadingView());
					} else if (viewType == ITEM_TYPE_NO_MORE_DATA) {
						return new RecyclerViewHolder(createNoMoreDataView());
					} else {
						return new RecyclerViewHolder(createOtherItemView(viewType));
					}
				}
			};
			recyclerView.setAdapter(contentAdapter);
		} else {
			contentAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void requestComplete(List dataList, int currentPage) {
		if (currentPage == 1) {
			contentListData.clear();
		}
		if (!CollectionUtil.isEmpty(dataList)) {
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
		isLoading = false;
		if (refreshLayout == null) {
			contentListData.addAll(listData);
			return;
		}

		if (refreshLayout.isRefreshing()) {
			refreshLayout.setRefreshing(false);
		}
		final int lastItemIndex = contentListData.size() - 1;
		if (lastItemIndex >= 0 && contentListData.get(lastItemIndex) == null) {
			contentListData.remove(lastItemIndex);
		}
		contentListData.addAll(listData);
		setContentAdapter();
	}

	private void notifyNoMoreData(List<T> listData) {
		isLoading = false;
		hasMoreData = false;
		if (refreshLayout == null) {
			return;
		}
		if (refreshLayout.isRefreshing()) {
			refreshLayout.setRefreshing(false);
		}

		int lastItemIndex = contentListData.size() - 1;
		if (lastItemIndex < 1) {
			return;
		}
		itemType = ITEM_TYPE_NO_MORE_DATA;
		if (!CollectionUtil.isEmpty(listData)) {
			contentListData.addAll(lastItemIndex, listData);
			setContentAdapter();
		} else {
			contentAdapter.notifyItemChanged(lastItemIndex);
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
	public void setContentValue(BaseCommonRecyclerAdapter.RecyclerViewHolder viewHolder, T itemData, int position) {
		listProcess.setContentValue(viewHolder, itemData, position);
	}

	@Override
	public void requestListData(int currentPage) {
		listProcess.requestListData(currentPage);
	}

	@Override
	public void setOtherContentValue(BaseCommonRecyclerAdapter.RecyclerViewHolder viewHolder) {
		int itemType = viewHolder.getItemViewType();
		if (itemType != ITEM_TYPE_LOADING && itemType != ITEM_TYPE_NO_MORE_DATA) {
			listProcess.setOtherContentValue(viewHolder);
		}
	}

	@Override
	public View createLoadingView() {
		View customLoadingView = listProcess.createLoadingView();
		if (customLoadingView != null) {
			return customLoadingView;
		}

		FrameLayout loadingLayout = new FrameLayout(context);
		loadingLayout.setMinimumWidth(DisplayUtil.getScreenWidth());
		loadingLayout.setMinimumHeight(defaultLoadingHeight);
		loadingLayout.setBackgroundColor(Color.RED);
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
			ViewGroup.LayoutParams.WRAP_CONTENT);
		params.gravity = Gravity.CENTER;
		ProgressBar progressBar = new ProgressBar(context);
		loadingLayout.addView(progressBar, params);
		return loadingLayout;
	}

	@Override
	public View createNoMoreDataView() {
		View customNoMoreDataText = listProcess.createNoMoreDataView();
		if (customNoMoreDataText != null) {
			return customNoMoreDataText;
		}

		TextView noMoreDataText = new TextView(context);
		noMoreDataText.setMinWidth(DisplayUtil.getScreenWidth());
		noMoreDataText.setMinimumHeight(DisplayUtil.dpToPx(64));
		noMoreDataText.setGravity(Gravity.CENTER);
		noMoreDataText.setText("没有更多数据啦~");
		noMoreDataText.setTextSize(13);
		noMoreDataText.setTextColor(ContextCompat.getColor(context, R.color.b2));
		noMoreDataText.setBackgroundColor(Color.GREEN);
		return noMoreDataText;
	}

	@Override
	public View createOtherItemView(int itemType) {
		return listProcess.createOtherItemView(itemType);
	}
}
