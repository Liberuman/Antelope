package com.sxu.common.base.ui.activity.mvvm;

import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.sxu.basecomponent.activity.BaseFragmentPagerActivity;
import com.sxu.common.base.http.HttpHelper;
import com.sxu.common.base.ui.IBaseVM;

/*******************************************************************************
 * Description: 使用ViewModel和DataBinding的Tab+ViewPager类型的Activity基础类
 *
 * Author: Freeman
 *
 * Date: 2021/10/12
 *
 * Copyright: all rights reserved by Freeman.
 *******************************************************************************/
public abstract class MyBaseVMPagerActivity<T, VB extends ViewDataBinding, VM extends ViewModel> extends BaseFragmentPagerActivity<T> implements IBaseVM {

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        initVM();
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
