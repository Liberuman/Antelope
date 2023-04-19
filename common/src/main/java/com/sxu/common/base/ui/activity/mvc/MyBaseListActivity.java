package com.sxu.common.base.ui.activity.mvc;

import com.sxu.basecomponent.activity.BaseCommonListActivity;
import com.sxu.common.base.http.HttpHelper;

/*******************************************************************************
 * Description: 列表类型的Activity基础类
 *
 * Author: Freeman
 *
 * Date: 2021/10/12
 *
 * Copyright: all rights reserved by Freeman.
 *******************************************************************************/
public abstract class MyBaseListActivity<T> extends BaseCommonListActivity<T> {

    @Override
    protected boolean isNetworkError(int code) {
        return HttpHelper.isNetworkError(code);
    }
}
