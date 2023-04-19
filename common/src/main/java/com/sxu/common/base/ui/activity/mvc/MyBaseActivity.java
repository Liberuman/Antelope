package com.sxu.common.base.ui.activity.mvc;

import com.sxu.basecomponent.activity.BaseActivity;
import com.sxu.common.base.http.HttpHelper;

/*******************************************************************************
 * Description: Activity基础类
 *
 * Author: Freeman
 *
 * Date: 2021/10/12
 *
 * Copyright: all rights reserved by Freeman.
 *******************************************************************************/
public abstract class MyBaseActivity extends BaseActivity {

    @Override
    protected boolean isNetworkError(int code) {
        return HttpHelper.isNetworkError(code);
    }
}
