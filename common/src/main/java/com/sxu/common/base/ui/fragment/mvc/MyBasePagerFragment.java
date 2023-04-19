package com.sxu.common.base.ui.fragment.mvc;

import com.sxu.basecomponent.fragment.BaseFragmentPagerFragment;
import com.sxu.common.base.http.HttpHelper;

/*******************************************************************************
 * Description: Tab+ViewPager类型的基础类
 *
 * Author: Freeman
 *
 * Date: 2021/10/12
 *
 * Copyright: all rights reserved by Freeman.
 *******************************************************************************/
public abstract class MyBasePagerFragment<T> extends BaseFragmentPagerFragment<T> {

    @Override
    protected boolean isNetworkError(int code) {
        return HttpHelper.isNetworkError(code);
    }
}
