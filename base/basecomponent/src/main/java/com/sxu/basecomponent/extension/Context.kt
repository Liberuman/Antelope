package com.sxu.basecomponent.extension

import android.content.Context
import android.content.Intent

/*******************************************************************************
 * Description: 扩展Context功能
 *
 * Author: Freeman
 *
 * Date:
 *
 *******************************************************************************/
/**
 * 跳转到指定Activity
 */
fun Context.toActivity(targetClass: Class<*>) {
    val intent = Intent(this, targetClass)
    startActivity(intent)
}

/**
 * 创建Intent
 */
fun Context.newIntent(targetClass: Class<*>): Intent {
    return Intent(this, targetClass)
}