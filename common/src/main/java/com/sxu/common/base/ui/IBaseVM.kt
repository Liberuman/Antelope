package com.sxu.common.base.ui

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel

/*******************************************************************************
 * Description: ViewModel+DataBinding接口定义
 *
 * Author: Freeman
 *
 * Date: 2021/10/12
 *
 * Copyright: all rights reserved by Freeman.
 *******************************************************************************/
interface IBaseVM<VB: ViewDataBinding, VM: ViewModel>  {

    /**
     * 与当前页面关联的ViewModel
     */
    var mModel: VM?

    /**
     * 当前页面布局对应的DataBinding
     */
    var mBinding: VB?

    /**
     * 注册ViewModel
     * @return
     */
    fun registerVM(): Class<VM>

    /**
     * 调用网络请求，监听ViewModel中的数据变化
     */
    fun initVM()
}