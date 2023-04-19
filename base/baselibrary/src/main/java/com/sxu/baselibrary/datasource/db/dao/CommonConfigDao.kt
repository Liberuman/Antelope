package com.sxu.baselibrary.datasource.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sxu.baselibrary.datasource.db.entity.CommonConfigEntity

@Dao
interface CommonConfigDao {

    /**
     * 插入配置记录
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRecord(entity: CommonConfigEntity?)

    /**
     * 清空配置记录
     * @param businessScene 指定的业务场景
     * @param dateTime 指定的日期值
     * @param userId    用户id
     */
    @Query("DELETE FROM common_config_table WHERE user_id = :userId AND create_time = :dateTime AND business_scene=:businessScene")
    fun clearRecordByTime(businessScene: String, userId: Long, dateTime: String)

    /**
     * 查询指定用户在指定日期、指定场景下的操作次数
     *
     * @param dateTime 指定的日期值
     * @param userId    用户id
     */
    @Query("SELECT COUNT() FROM common_config_table WHERE user_id = :userId AND create_time = :dateTime AND business_scene=:businessScene")
    fun getRecordCountByTime(businessScene: String, userId: Long, dateTime: String): Int

    /**
     * 查询指定场景下的操作次数
     */
    @Query("SELECT COUNT() FROM common_config_table WHERE business_scene=:businessScene")
    fun getRecordCount(businessScene: String): Int
}