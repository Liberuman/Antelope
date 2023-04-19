package com.sxu.baselibrary.datasource.cache;

import android.content.Context;

import java.util.Set;

/*******************************************************************************
 * K-V类数据存储接口
 *
 * @author: Freeman
 *
 * @date: 2020/6/19
 *
 * Copyright: all rights reserved by Freeman.
 *******************************************************************************/
public interface IKVCache {

    /**
     * 初始化存储器
     * @param context
     */
    void init(Context context, String name);

    /**
     * 存储Boolean类型的值
     * @param key
     * @param value
     */
    void put(String key, boolean value);

    /**
     * 存储Int类型的值
     * @param key
     * @param value
     */
    void put(String key, int value);

    /**
     * 存储Long类型的值
     * @param key
     * @param value
     */
    void put(String key, long value);

    /**
     * 存储Float类型的值
     * @param key
     * @param value
     */
    void put(String key, float value);

    /**
     * 存储Double类型的值
     * @param key
     * @param value
     */
    void put(String key, double value);

    /**
     * 存储String类型的值
     * @param key
     * @param value
     */
    void put(String key, String value);

    /**
     * 存储Set<String>类型的值
     * @param key
     * @param value
     */
    void put(String key, Set<String> value);

    /**
     * 根据key获取存储的Boolean值
     * @param key
     * @return
     */
    boolean getBoolean(String key, boolean defaultValue);

    /**
     * 根据key获取存储的Int值
     * @param key
     * @return
     */
    int getInt(String key, int defaultValue);

    /**
     * 根据key获取存储的Long值
     * @param key
     * @return
     */
    long getLong(String key, long defaultValue);

    /**
     * 根据key获取存储的Float值
     * @param key
     * @return
     */
    float getFloat(String key, float defaultValue);

    /**
     * 根据key获取存储的Double值
     * @param key
     * @return
     */
    double getDouble(String key, double defaultValue);

    /**
     * 根据key获取存储的String值
     * @param key
     * @return
     */
    String getString(String key, String defaultValue);

    /**
     * 根据key获取存储的Set<String>值
     * @param key
     * @return
     */
    Set<String> getStringSet(String key, Set<String> defaultValue);

    /**
     * 检查指定的key是否存在
     * @param key
     * @return
     */
    boolean contains(String key);

    /**
     * 删除指定的key
     * @param key
     */
    void remove(String key);

    /**
     * 清楚所有保存的键值对
     */
    void clear();
}
