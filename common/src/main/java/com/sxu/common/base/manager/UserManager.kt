package com.sxu.common.manager.base.manager

import com.sxu.basecomponent.manager.BaseUserManager
import com.sxu.baselibrary.datasource.http.impl.HttpManager
import com.sxu.common.manager.base.bean.UserInfoBean

/*******************************************************************************
 * Description: 用户会话管理
 *
 * Author: Freeman
 *
 * Date: 2021/9/3
 *
 * Copyright: all rights reserved by Freeman.
 *******************************************************************************/
object UserManager {

    /**
     * 保存用户信息
     */
    fun setUserInfo(userInfo: UserInfoBean) {
        BaseUserManager.getInstance(UserInfoBean::class.java).userInfo = userInfo
    }

    /**
     * 获取用户信息
     */
    fun getUserInfo(): UserInfoBean? {
        return BaseUserManager.getInstance(UserInfoBean::class.java).userInfo
    }

    /**
     * 用户是否已登录
     */
    fun isLogin(): Boolean {
        return BaseUserManager.getInstance(UserInfoBean::class.java).isLogin
    }

    /**
     * 设置登录状态
     * @param isLogin 是否已登录
     */
    fun updateLoginStatus(isLogin: Boolean) {
        BaseUserManager.getInstance(UserInfoBean::class.java).updateLoginStatus(isLogin)
    }

    /**
     * 获取登录用户的token
     */
    fun getToken(): String? {
        return getUserInfo()?.token
    }

    /**
     * 获取用户id
     */
    fun getUserId(): Long {
        return getUserInfo()?.userId ?: 0L
    }
}