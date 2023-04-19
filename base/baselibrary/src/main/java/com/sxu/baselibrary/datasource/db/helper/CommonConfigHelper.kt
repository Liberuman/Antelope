package com.sxu.baselibrary.datasource.db.helper

import com.sxu.baselibrary.commonutils.DateTimeUtil
import com.sxu.baselibrary.datasource.db.MyDatabase
import com.sxu.baselibrary.datasource.db.entity.CommonConfigEntity

/**
 * 用户存储&检索用户每天执行N次操作的的记录
 */
object CommonConfigHelper {

    val TODAY_SHOW_FORTUNE = "today_show_fortune"

    /**
     * 插入操作记录
     */
    fun insertRecord(entity: CommonConfigEntity) {
        MyDatabase.instance.commonConfigDao().insertRecord(entity)
    }

    /**
     * 删除指定用户在指定日期、指定场景的下的操作记录
     */
    fun clearRecord(businessScene: String, userId: Long, dateTime: String = DateTimeUtil.formatDate(System.currentTimeMillis(), "yyyy-MM-dd")) {
        MyDatabase.instance.commonConfigDao().clearRecordByTime(businessScene, userId, dateTime)
    }

   /**
     * 检查指定用户是否已完成指定日期、指定场景的操作
     */
    fun finished(businessScene: String, userId: Long, dateTime: String = DateTimeUtil.formatDate(System.currentTimeMillis(), "yyyy-MM-dd"), requireRecordCount: Int = 1): Boolean {
        return MyDatabase.instance.commonConfigDao().getRecordCountByTime(businessScene, userId, dateTime) >= requireRecordCount
    }

    /**
     * 获取指定用户当天已完成的次数
     */
    fun getRecordCount(businessScene: String, userId: Long, dateTime: String = DateTimeUtil.formatDate(System.currentTimeMillis(), "yyyy-MM-dd")): Int {
        return MyDatabase.instance.commonConfigDao().getRecordCountByTime(businessScene, userId, dateTime)
    }
}