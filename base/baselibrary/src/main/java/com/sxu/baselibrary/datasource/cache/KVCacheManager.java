package com.sxu.baselibrary.datasource.cache;

import android.content.Context;

import androidx.annotation.NonNull;

import com.sxu.baselibrary.commonutils.BaseContentProvider;

import java.util.Set;

/*******************************************************************************
 * Description: KV存储管理器 使用前需调用init进行初始化
 *
 * Author: Freeman
 *
 * Date: 2018/6/20
 *
 * Copyright: all rights reserved by Freeman.
 *******************************************************************************/
 public final class KVCacheManager implements IKVCache {

    /**
     * 是否已初始化，避免重复初始化
     */
    private boolean isInit = false;

    /**
     * 默认的KV存储引擎
     */
    private IKVCache cacheEngine = new MMKVCache();

    public static KVCacheManager getInstance() {
        return Singleton.instance;
    }

    /**
     * 初始化
     * @param cacheEngine：设置KV缓存引擎
     * @param name
     */
    public void init(@NonNull IKVCache cacheEngine, String name) {
        this.cacheEngine = cacheEngine;
        init(BaseContentProvider.context, name);
    }

    @Override
    public void init(Context context, String name) {
        if (isInit) {
            return;
        }

        isInit = true;
        cacheEngine.init(context.getApplicationContext(), name);
    }

    @Override
    public void put(String key, boolean value) {
        cacheEngine.put(key, value);
    }

    @Override
    public void put(String key, int value) {
        cacheEngine.put(key, value);
    }

    @Override
    public void put(String key, long value) {
        cacheEngine.put(key, value);
    }

    @Override
    public void put(String key, float value) {
        cacheEngine.put(key, value);
    }

    @Override
    public void put(String key, double value) {
        cacheEngine.put(key, value);
    }

    @Override
    public void put(String key, String value) {
        cacheEngine.put(key, value);
    }

    @Override
    public void put(String key, Set<String> value) {
        cacheEngine.put(key, value);
    }

    @Override
    public boolean getBoolean(String key, boolean defaultValue) {
        return cacheEngine.getBoolean(key, defaultValue);
    }

    public int getInt(String key) {
        return cacheEngine.getInt(key, 0);
    }

    @Override
    public int getInt(String key, int defaultValue) {
        return cacheEngine.getInt(key, defaultValue);
    }

    public long getLong(String key) {
        return cacheEngine.getLong(key, 0L);
    }

    @Override
    public long getLong(String key, long defaultValue) {
        return cacheEngine.getLong(key, defaultValue);
    }

    public float getFloat(String key) {
        return cacheEngine.getFloat(key, 0f);
    }

    @Override
    public float getFloat(String key, float defaultValue) {
        return cacheEngine.getFloat(key, defaultValue);
    }

    public double getDouble(String key) {
        return cacheEngine.getDouble(key, 0);
    }

    @Override
    public double getDouble(String key, double defaultValue) {
        return cacheEngine.getDouble(key, defaultValue);
    }

    public String getString(String key) {
        return cacheEngine.getString(key, null);
    }

    @Override
    public String getString(String key, String defaultValue) {
        return cacheEngine.getString(key, defaultValue);
    }

    public Set<String> getStringSet(String key) {
        return cacheEngine.getStringSet(key, null);
    }

    @Override
    public Set<String> getStringSet(String key, Set<String> defaultValue) {
        return cacheEngine.getStringSet(key, defaultValue);
    }


    @Override
    public boolean contains(String key) {
        return cacheEngine.contains(key);
    }

    @Override
    public void remove(String key) {
        cacheEngine.remove(key);
    }

    @Override
    public void clear() {
        cacheEngine.clear();
    }

    private static class Singleton {
        public final static KVCacheManager instance = new KVCacheManager();
    }
}
