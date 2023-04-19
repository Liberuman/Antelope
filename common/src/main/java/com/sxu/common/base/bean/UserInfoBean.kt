package com.sxu.common.manager.base.bean

import com.sxu.common.base.extension.iLog
import com.sxu.common.base.extension.shortToast

/******************************************************************************
 * Description: 登录后的用户信息 注意：集成到项目中后需要改造成项目中用户信息的结构
 *
 * Author: Freeman
 *
 * Date: 2021/9/3
 *
 * Copyright: all rights reserved by Freeman.
 *******************************************************************************/
data class UserInfoBean(
    var userId: Long = 0,
    var userName: String = "",
    var icon: String = "",
    var token: String = ""
)
