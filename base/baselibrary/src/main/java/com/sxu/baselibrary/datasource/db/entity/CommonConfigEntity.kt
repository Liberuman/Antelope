package com.sxu.baselibrary.datasource.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sxu.baselibrary.commonutils.DateTimeUtil

@Entity(tableName = "common_config_table")
class CommonConfigEntity {

    constructor(
        businessScene: String,
        userId: Long,
        createTime: String = DateTimeUtil.formatDate(
            System.currentTimeMillis(),
            "yyyy-MM-dd"
        )
    ) {
        this.userId = userId
        this.businessScene = businessScene
        this.createTime = createTime
    }

    /**
     * 记录ID
     */
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id = 0L

    /**
     * UserId
     */
    @ColumnInfo(name = "user_id")
    var userId = 0L

    /**
     * 所使用的业务场景
     */
    @ColumnInfo(name = "business_scene")
    var businessScene = ""

    /**
     * 事件发生的日期
     */
    @ColumnInfo(name = "create_time")
    var createTime = ""
}