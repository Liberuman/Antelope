package com.sxu.basecomponent.activity;

import android.content.Context;
import android.view.View;

import com.sxu.basecomponent.adapter.BaseCommonRecyclerAdapter;
import com.sxu.basecomponent.bean.ItemBaseBean;
import com.sxu.basecomponent.interfaces.ICommonListProcess;
import com.sxu.basecomponent.interfaces.impl.CommonListProcess;
import com.sxu.baselibrary.commonutils.CollectionUtil;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/*******************************************************************************
 *
 *
 * @author: Freeman
 *
 * @date: 2020/5/20
 *
 * Copyright: all rights reserved by Freeman.
 *******************************************************************************/
public abstract class BaseCommonListActivity<T> extends BaseProgressActivity implements ICommonListProcess<T> {

	private ICommonListProcess listProcessInstance = null;

	@Override
	public int getLayoutResId() {
		return 0;
	}

	@Override
	public void initViews() {
		updateContentLayout(getContentLayout());
	}

	@Override
	public void requestData() {
		listProcessInstance = new CommonListProcess(this, this);
		requestListData(1);
	}

	@Override
	public void initComponent() {

	}

	@Override
	public int getPageSize() {
		return 0;
	}

	@Override
	public View getContentLayout() {
		return listProcessInstance.getContentLayout();
	}

	@Override
	public void initContentViews(View refreshLayout, RecyclerView recyclerView) {

	}

	@Override
	public void setOtherContentValue(BaseCommonRecyclerAdapter.RecyclerViewHolder viewHolder) {

	}

	@Override
	public View createLoadingView() {
		return null;
	}

	@Override
	public View createNoMoreDataView() {
		return null;
	}

	@Override
	public View createOtherItemView(int itemType) {
		return null;
	}

	@Override
	public void requestComplete(List dataList, int currentPage) {
		listProcessInstance.requestComplete(dataList, currentPage);
		if (!hasLoaded && currentPage == 1) {
			if (!CollectionUtil.isEmpty(dataList)) {
				notifyLoadFinish(MSG_LOAD_FINISH);
			} else {
				notifyLoadFinish(MSG_LOAD_EMPTY);
			}
		}
	}

	@Override
	public boolean canLoadMore() {
		return false;
	}

	@Override
	public boolean canRefresh() {
		return false;
	}
}
