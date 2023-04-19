package com.sxu.basecomponent.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.CallSuper;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.sxu.basecomponent.interfaces.IComponentProcessEx;
import com.sxu.basecomponent.interfaces.IContainerLayoutStyle;
import com.sxu.basecomponent.interfaces.IRequestProcessor;
import com.sxu.basecomponent.interfaces.OnShowContentLayoutListener;
import com.sxu.basecomponent.interfaces.impl.ContainerLayoutStyleImpl;
import com.sxu.basecomponent.interfaces.impl.LayoutManager;
import com.sxu.basecomponent.interfaces.impl.RequestProcessorImpl;
import com.sxu.basecomponent.uiwidget.ToolbarEx;

/*******************************************************************************
 * Description: 不需要网络请求的Fragment
 *
 * Author: Freeman
 *
 * Date: 2018/7/27
 *
 * Copyright: all rights reserved by Freeman.
 *******************************************************************************/
public abstract class BaseFragment extends Fragment implements IComponentProcessEx, IContainerLayoutStyle, IRequestProcessor, View.OnClickListener {

    /**
     * 页面自带的Toolbar
     */
    protected ToolbarEx toolbar;
    /**
     * 真实的页面布局
     */
    protected View realContentView;

    protected Context context;
    protected FragmentManager fragmentManager;

    /**
     * 默认的页面请求处理器
     */
    protected RequestProcessorImpl defaultProcessor;

    /**
     * 默认的页面样式
     */
    private ContainerLayoutStyleImpl layoutStyle;

    /**
     * 是否已完成初始化
     */
    private boolean isInitialized = false;

    /**
     * Fragment是否是第一次可见
     */
    private boolean fragmentIsFirstVisible = true;

    /**
     * Fragment是否是可见
     */
    private boolean fragmentIsVisible = false;

    /**
     * 网络数据是否已请求
     */
    private boolean dataRequested = false;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        fragmentManager = getChildFragmentManager();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 页面预处理
        pretreatment();
        // 根据页面样式创建页面容器
        layoutStyle = new ContainerLayoutStyleImpl(this);
        // 根据是否需要网络请求，填充布局内容
        realContentView = LayoutManager.getRealContentView(context, this);
        defaultProcessor = new RequestProcessorImpl(context);
        defaultProcessor.setContainerLayout(getContainerLayout(context),
                realContentView, needRequest());
        toolbar = layoutStyle.getToolbar();
        View containerLayout = getContainerLayout(context);
        if (container != null) {
            container.addView(containerLayout);
            return super.onCreateView(inflater, container, savedInstanceState);
        } else {
            return containerLayout;
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 初始化View
        initViews();
        // 设置各种事件监听
        initListener();

        // 如果需要网络请求，则执行requestData进行数据请求, 否则为View绑定数据
        if (needRequest()) {
            defaultProcessor.setOnShowContentLayoutListener(new OnShowContentLayoutListener() {
                @Override
                public void onShowContentLayout() {
                    // 为View绑定请求的网络数据
                    bindDataForView();
                }
            });
            // 如果没有启用懒加载，则直接请求数据
            if (!lazyLoadEnable()) {
                requestData();
                dataRequested = true;
            }
        } else {
            bindDataForView();
        }

        isInitialized = true;
        if (fragmentIsVisible) {
            onVisibleEveryTime();
            fragmentIsFirstVisible = false;
            onVisibleFirst();
        }
    }


    @Override
    public boolean isImmersion() {
        return true;
    }

    /**
     * 修改状态栏的背景色
     * @return
     */
    protected @ColorInt
    int getStatusBarColor() {
        return Color.TRANSPARENT;
    }

    /**
     * 修改NavigationBar的背景色
     * @return
     */
    protected @ColorInt int getNavigationBarColor() {
        return Color.WHITE;
    }

    @Override
    public final ViewGroup getContainerLayout(Context context) {
        return layoutStyle.getContainerLayout(context);
    }

    @Override
    public void pretreatment() {

    }

    @Override
    public View getContentView() {
        return null;
    }

    @Override
    public int getToolbarStyle() {
        return TOOL_BAR_STYLE_NORMAL;
    }

    @Override
    public boolean needRequest() {
        return true;
    }

    @Override
    public View loadLoadingLayout() {
        return defaultProcessor.loadLoadingLayout();
    }

    @Override
    public View loadEmptyLayout() {
        return defaultProcessor.loadEmptyLayout();
    }

    @Override
    public View loadFailureLayout() {
        return defaultProcessor.loadFailureLayout();
    }

    @Override
    public View loadLoginLayout() {
        return defaultProcessor.loadLoginLayout();
    }

    @Override
    public void onClick(View v) {

    }

    /**
     * 网络请求成功，通知页面更换布局
     */
    protected final void loadFinish() {
        defaultProcessor.notifyLoadFinish(IRequestProcessor.MSG_LOAD_FINISH);
    }

    /**
     * 是否是网络请求错误
     * @param code
     * @return
     */
    protected abstract boolean isNetworkError(int code);

    protected boolean lazyLoadEnable() {
        return false;
    }

    /**
     * 首次可见时调用
     */
    @CallSuper
    protected void onVisibleFirst() {
        if (needRequest() && lazyLoadEnable() && !dataRequested) {
            requestData();
        }
    }

    /**
     * 非首次可见时调用
     */
    protected void onVisibleExceptFirst() {

    }

    /**
     * 每次可见时调用
     */
    protected void onVisibleEveryTime() {

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        updateVisibleStatus(!hidden);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        updateVisibleStatus(isVisibleToUser);
    }

    /**
     * 更新Fragment显示标志并调用相关方法
     * @param isVisible
     */
    private void updateVisibleStatus(boolean isVisible) {
        fragmentIsVisible = isVisible;
        if (isVisible && isInitialized) {
            onVisibleEveryTime();
            if (fragmentIsFirstVisible) {
                fragmentIsFirstVisible = false;
                onVisibleFirst();
            } else {
                onVisibleExceptFirst();
            }
        }
    }
}

