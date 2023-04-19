package com.sxu.basecomponent.extension

import androidx.lifecycle.LifecycleOwner
import com.sxu.basecomponent.manager.AsyncObjManager

/**
 * 异步对象关联生命周期
 * 如Thread，Handler，Animation, Animator, LiveData, Timer, CoroutineScope等
 */
inline fun <T> T.attachLifecycle(owner: LifecycleOwner): T {
    this?.let {
        AsyncObjManager.addTarget(owner, it)
    }
    return this
}