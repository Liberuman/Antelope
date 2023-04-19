package com.sxu.basecomponent.extension

/**
 * 安全转换为Enum
 */
inline fun <reified T : Enum<T>> CharSequence?.convertEnum(defaultEnum: T): T {
    if (this.isNullOrEmpty()) {
        return defaultEnum
    }
    return try {
        enumValueOf(this.toString())
    } catch (e: Exception) {
        defaultEnum
    }
}