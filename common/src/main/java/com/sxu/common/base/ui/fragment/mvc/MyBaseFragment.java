package com.sxu.common.base.ui.fragment.mvc;

import com.sxu.basecomponent.fragment.BaseFragment;
import com.sxu.common.base.http.HttpHelper;

/*******************************************************************************
 * Description: Fragment的基础类
 *
 * Author: Freeman
 *
 * Date: 2021/10/12
 *
 * Copyright: all rights reserved by Freeman.
 *******************************************************************************/
public abstract class MyBaseFragment extends BaseFragment {

    @Override
    protected boolean isNetworkError(int code) {
        return HttpHelper.isNetworkError(code);
    }

}
