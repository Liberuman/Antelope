package com.sxu.baselibrary.datasource.cache;

import android.content.Context;
import android.text.TextUtils;

import com.tencent.mmkv.MMKV;

import java.util.Set;

/*******************************************************************************
 * Description: 基于SharePreference的KV存储器
 *
 * Author: Freeman
 *
 * Date: 2018/6/20
 *
 * Copyright: all rights reserved by Freeman.
 *******************************************************************************/

public class MMKVCache implements IKVCache {

    /**
     * 是否支持多进程
     */
    private boolean supportMultiProcess = true;

    /**
     * 加密时使用的key
     */
    private String encryptKey = "";

    private MMKV kvCore = null;

    @Override
    public void init(Context context, String name) {
        MMKV.initialize(context.getApplicationContext());
        kvCore = MMKV.mmkvWithID(!TextUtils.isEmpty(name) ? name : context.getPackageName() + "_MM_KV",
            supportMultiProcess ? MMKV.MULTI_PROCESS_MODE : MMKV.SINGLE_PROCESS_MODE, encryptKey);
    }

    /**
     * 配置MMVK
     * 注意：需要在init方法之前调用
     * @param supportMultiProcess: 是否支持多进程，默认为true
     * @param encryptKey：加密需要的key
     */
    public void setConfig(boolean supportMultiProcess, String encryptKey) {
        this.supportMultiProcess = supportMultiProcess;
        this.encryptKey = encryptKey;
    }

    @Override
    public void put(String key, boolean value) {
        kvCore.encode(key, value);
    }

    @Override
    public void put(String key, int value) {
        kvCore.encode(key, value);
    }

    @Override
    public void put(String key, long value) {
        kvCore.encode(key, value);
    }

    @Override
    public void put(String key, float value) {
        kvCore.encode(key, value);
    }

    @Override
    public void put(String key, double value) {
        kvCore.encode(key, value);
    }

    @Override
    public void put(String key, String value) {
        kvCore.encode(key, value);
    }

    @Override
    public void put(String key, Set<String> value) {
        kvCore.encode(key, value);
    }

    @Override
    public boolean getBoolean(String key, boolean defaultValue) {
        return kvCore.decodeBool(key, defaultValue);
    }

    @Override
    public int getInt(String key, int defaultValue) {
        return kvCore.decodeInt(key, defaultValue);
    }

    @Override
    public long getLong(String key, long defaultValue) {
        return kvCore.decodeLong(key, defaultValue);
    }

    @Override
    public float getFloat(String key, float defaultValue) {
        return kvCore.decodeFloat(key, defaultValue);
    }

    @Override
    public double getDouble(String key, double defaultValue) {
        return kvCore.decodeDouble(key, defaultValue);
    }

    @Override
    public String getString(String key, String defaultValue) {
        return kvCore.decodeString(key, defaultValue);
    }


    @Override
    public Set<String> getStringSet(String key, Set<String> defaultValue) {
        return kvCore.decodeStringSet(key, defaultValue);
    }


    @Override
    public boolean contains(String key) {
        return kvCore.contains(key);
    }

    @Override
    public void remove(String key) {
        kvCore.removeValueForKey(key);
    }

    @Override
    public void clear() {
        kvCore.clearAll();
    }
}
