package com.sxu.basecomponent.extension

import android.content.res.Resources
import android.util.TypedValue

/**
 * 整型数字转为DP值
 */
val Int.dp: Int
    get() = getDimension(this.toFloat(), TypedValue.COMPLEX_UNIT_DIP).toInt()

/**
 * 浮点型数字转为DP值
 */
val Float.dp: Float
    get() = getDimension(this, TypedValue.COMPLEX_UNIT_DIP)

/**
 * 整型数字转为SP值
 */
val Int.sp: Int
    get() = getDimension(this.toFloat(), TypedValue.COMPLEX_UNIT_SP).toInt()

/**
 * 浮点型数字转为SP值
 */
val Float.sp: Float
    get() = getDimension(this, TypedValue.COMPLEX_UNIT_SP)

private fun getDimension(value: Float, unit: Int): Float {
    return TypedValue.applyDimension(unit, value, Resources.getSystem().displayMetrics)
}