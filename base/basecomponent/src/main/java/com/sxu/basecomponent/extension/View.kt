package com.sxu.basecomponent.extension

import android.view.View
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * View点击事件，防止快速点击
 */
fun View.onClick(delay: Long = 200L, click: (View) -> Unit) {
    setOnClickListener {
        if (!isClickable) {
            return@setOnClickListener
        }
        click.invoke(this)
        if (delay > 0){
            isClickable = false
            GlobalScope.launch(Dispatchers.Main) {
                delay(delay)
                isClickable = true
            }
        }
    }
}