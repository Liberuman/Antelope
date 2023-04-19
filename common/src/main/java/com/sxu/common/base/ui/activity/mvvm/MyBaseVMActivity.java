package com.sxu.common.base.ui.activity.mvvm;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.sxu.basecomponent.activity.BaseActivity;
import com.sxu.common.base.http.HttpHelper;
import com.sxu.common.base.ui.IBaseVM;

/*******************************************************************************
 * Description: 使用ViewModel和DataBinding的Activity基础类
 *
 * Author: Freeman
 *
 * Date: 2021/10/12
 *
 * Copyright: all rights reserved by Freeman.
 *******************************************************************************/
public abstract class MyBaseVMActivity<VB extends ViewDataBinding, VM extends ViewModel> extends BaseActivity implements IBaseVM {

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        initVM();
    }

    /**
     * 使用ViewDataBinding时，布局需要包裹在<layout></layout>中，所以动态创建布局的方法没必要使用
     * @return
     */
    @Override
    public final View getContentView() {
        return super.getContentView();
    }

    /**
     * 为了保持组件逻辑的一致性，使用ViewModel时在数据监听中为View绑定数据，所以没必要重写此方法
     */
    @Override
    public void bindDataForView() {

    }

    /**
     * 使用了ViewModel, 所以不需要在requestData中进行网络请求
     * 只需要监听ViewModel中的数据，然后调用loadFinish或loadError即可
     */
    @Override
    public void requestData() {

    }

    @Override
    protected boolean isNetworkError(int code) {
        return HttpHelper.isNetworkError(code);
    }

    @Nullable
    @Override
    public VM getMModel() {
        return (VM) new ViewModelProvider(getViewModelStore(), getDefaultViewModelProviderFactory()).get(registerVM());
    }

    @Override
    public void setMModel(@Nullable ViewModel model) {

    }

    @Nullable
    @Override
    public VB getMBinding() {
        return DataBindingUtil.bind(realContentView);
    }

    @Override
    public void setMBinding(@Nullable ViewDataBinding binding) {

    }
}
