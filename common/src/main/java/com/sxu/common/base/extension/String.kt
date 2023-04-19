package com.sxu.common.base.extension

import com.sxu.baselibrary.commonutils.LogUtil
import com.sxu.baselibrary.commonutils.ToastUtil

/**
 * 显示toast
 */
fun String.shortToast() {
    ToastUtil.showShort(this)
}

fun String.longToast() {
    ToastUtil.showLong(this)
}

/**
 * 打印Log
 */
fun String.iLog() {
    LogUtil.i(this)
}

fun String.dLog() {
    LogUtil.d(this)
}

fun String.vLog() {
    LogUtil.v(this)
}

fun String.wLog() {
    LogUtil.w(this)
}

fun String.eLog() {
    LogUtil.e(this)
}

