package com.sxu.common.base.ui.fragment.mvc;

import com.sxu.basecomponent.fragment.BaseCommonListFragment;
import com.sxu.common.base.http.HttpHelper;

/*******************************************************************************
 * Description: 列表类的Fragment基础类
 *
 * Author: Freeman
 *
 * Date: 2021/10/12
 *
 * Copyright: all rights reserved by Freeman.
 *******************************************************************************/
public abstract class MyBaseListFragment<T> extends BaseCommonListFragment<T> {

    @Override
    protected boolean isNetworkError(int code) {
        return HttpHelper.isNetworkError(code);
    }
}
