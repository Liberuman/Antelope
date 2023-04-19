package com.sxu.baselibrary.datasource.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sxu.baselibrary.commonutils.BaseContentProvider
import com.sxu.baselibrary.datasource.db.dao.CommonConfigDao
import com.sxu.baselibrary.datasource.db.entity.CommonConfigEntity

/**
 * 构建数据库
 * 1.加表仅需更新版本号
 * 2.如果变更表结构，必须加MIGRATION
 */
@Database(
    entities = [CommonConfigEntity::class],
    version = 2,
    exportSchema = false
)
abstract class MyDatabase : RoomDatabase() {

    companion object {
        val instance = SingletonHolder.instance
    }

    private object SingletonHolder {

        /**
         * 默认的数据库名
         */
        private const val DATABASE_NAME = "common_config_db"

        val instance = Room.databaseBuilder(
            BaseContentProvider.context,
            MyDatabase::class.java,
            DATABASE_NAME
        )
            //如数据库升级失败，则重建数据库，但会造成数据丢失
            .fallbackToDestructiveMigration()
            //允许主线程执行
            .allowMainThreadQueries()
            .build()
    }

    abstract fun commonConfigDao(): CommonConfigDao
}