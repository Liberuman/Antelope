package com.sxu.basecomponent.fragment;

import android.view.View;

import androidx.annotation.CallSuper;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.sxu.basecomponent.R;
import com.sxu.basecomponent.interfaces.ICommonListProcess;
import com.sxu.basecomponent.interfaces.impl.CommonListProcess;

import java.util.List;

/*******************************************************************************
 * 通用的列表页面
 * 说明：如果默认的列表布局不符合APP的风格，可通过getLayoutResId或getContentView创建新的布局。
 * 需要注意的是：布局中的id必须是固定的:
 * RecyclerView的id应指定为recycler_view, refreshLayout的id应指定为refresh_layout
 *
 * RecyclerView和RefreshLayout不作为成员属性的原因：使用不当可能出现空指针。
 *
 * @author: Freeman
 *
 * @date: 2020/5/20
 *
 * Copyright: all rights reserved by Freeman.
 *******************************************************************************/
public abstract class BaseCommonListFragment<T> extends BaseFragment implements ICommonListProcess<T> {

	/**
	 * 页面是否已加载，只让页面加载一次
	 */
	private boolean hasLoaded = false;

	/**
	 * 默认每页的元素个数
	 */
	private final int DEFAULT_PAGE_SIZE = 10;

	/**
	 * 列表数据的控制器
	 */
	private CommonListProcess listProcessInstance = null;

	@Override
	public int getLayoutResId() {
		return R.layout.view_common_list_layout;
	}

	@Override
	public View getContentView() {
		return null;
	}

	@Override
	public final void initViews() {
		View containerLayout = getContainerLayout(context);
		SmartRefreshLayout refreshLayout = containerLayout.findViewById(R.id.refresh_layout);
		RecyclerView recyclerView = containerLayout.findViewById(R.id.recycler_view);
		if (refreshLayout == null || recyclerView == null) {
			throw new IllegalStateException("RecyclerView id is not R.id.recycler_view or SmartRefreshLayout id not R.id.refresh_layout");
		}

		listProcessInstance = new CommonListProcess(context, this);
		listProcessInstance.initContentViews(refreshLayout, recyclerView);
	}

	@Override
	public void initListener() {

	}

	@Override
	public final void requestData() {
		requestListData(1);
	}

	@Override
	public void bindDataForView() {

	}

	@Override
	public int getPageSize() {
		return DEFAULT_PAGE_SIZE;
	}

	@Override
	public void setOtherContentValue(BaseViewHolder viewHolder, T itemData, int position) {

	}

	@CallSuper
	@Override
	public void initAdapter(BaseQuickAdapter adapter) {
		// 为Adapter设置默认的空页面
		adapter.setEmptyView(loadEmptyLayout());
	}

	@Override
	public int getItemViewType(int position) {
		return 0;
	}

	@Override
	public View createOtherItemView(int itemType) {
		return null;
	}

	@Override
	public final void requestComplete(List dataList, int currentPage) {
		listProcessInstance.requestComplete(dataList, currentPage);
		if (!hasLoaded && currentPage == 1) {
			// 列表页面使用Adapter设置空页面，扩展性更好，所以无论数据请求成功与否，都加载真实的内容页面
			loadFinish();
			hasLoaded = true;
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
