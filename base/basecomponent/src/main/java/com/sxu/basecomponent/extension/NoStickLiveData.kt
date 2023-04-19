package com.sxu.basecomponent.extension

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

/*******************************************************************************
 * Description: 重写LiveData，解决数据倒灌问题
 *
 * Author: Freeman
 *
 * Date: 2021/9/3
 *
 * Copyright: all rights reserved by Freeman.
 *******************************************************************************/
class NoStickLiveData<T>(private var isStickEvent: Boolean = false) : MutableLiveData<T>() {

    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        if (isStickEvent) {
            super.observe(owner, observer)
        } else {
            super.observe(owner, CustomObserver(observer, value))
        }
    }

    override fun observeForever(observer: Observer<in T>) {
        super.observeForever(observer)
    }

    class CustomObserver<T>(private var observer: Observer<in T>, private var initValue: T?) : Observer<T> {

        override fun onChanged(t: T) {
            // 如果监听之前已有初始值则忽略
            if (initValue != null && initValue === t) {
                initValue = null
                return
            }
            observer.onChanged(t)
        }
    }
}