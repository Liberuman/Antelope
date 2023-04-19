package com.sxu.basecomponent.application;

import android.app.Application;

/*******************************************************************************
 * Description: 组件初始化接口
 *
 * Author: Freeman
 *
 * Date: 2021/9/23
 *
 * Copyright: all rights reserved by Freeman.
 *******************************************************************************/
public interface IApplication {

    /**
     * 组件初始化
     * @param application
     */
    void init(Application application);
}
