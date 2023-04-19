package com.sxu.basecomponent.manager

import android.animation.Animator
import android.os.AsyncTask
import android.os.CountDownTimer
import android.os.Handler
import android.view.animation.Animation
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import java.util.*

/**
 * 异步对象管理器，用户解决异步对象的生命周期问题
 * 如：Animation, Animator对象等
 */
object AsyncObjManager: LifecycleEventObserver {

    private val valueMap = WeakHashMap<LifecycleOwner, MutableList<Any>>()

    fun addTarget(owner: LifecycleOwner, target: Any) {
        if (!valueMap.containsKey(owner)) {
            owner.lifecycle.addObserver(this)
            valueMap[owner] = mutableListOf(target)
        } else {
            valueMap[owner]?.add(target)
        }
    }

    override fun onStateChanged(owner: LifecycleOwner, event: Lifecycle.Event) {
        if (event != Lifecycle.Event.ON_DESTROY) {
            return
        }

        val targetList = valueMap[owner]
        targetList?.forEach {
            when (it) {
                is Animation -> {
                    it.cancel()
                }
                is Animator -> {
                    it.cancel()
                }
                is Handler -> {
                    it.removeCallbacksAndMessages(null)
                }
                /**
                 * LiveData本身是可绑定生命周期的，但是绑定后当其关联的页面处于后台时，LiveData无法实时更新数据，
                 * 此时可使用observeForever监听解决此问题。但这种方式不会绑定生命周期，所以需要在页面关闭时
                 * 清理掉其绑定的监听器。
                 */
                is LiveData<*> -> {
                    it.removeObservers(owner)
                }
                is CoroutineScope -> {
                    it.cancel()
                }
                is Thread -> {
                    it.interrupt()
                }
                is Timer -> {
                    it.cancel()
                }
                is TimerTask -> {
                    it.cancel()
                }
                is CountDownTimer -> {
                    it.cancel()
                }
                is AsyncTask<*, *, *> -> {
                    it.cancel(true)
                }
            }
        }
        targetList?.clear()
        valueMap.remove(owner)
    }
}