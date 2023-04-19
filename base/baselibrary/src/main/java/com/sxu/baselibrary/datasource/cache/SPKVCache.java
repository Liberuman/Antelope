package com.sxu.baselibrary.datasource.cache;

import static java.lang.Double.parseDouble;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

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

public class SPKVCache implements IKVCache {

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private String PREFERENCE_NAME = "";

    @Override
    public void init(Context context, String name) {
        PREFERENCE_NAME = !TextUtils.isEmpty(name) ? name : context.getPackageName() + "_SP_KV";
        preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    @Override
    public void put(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.commit();
    }

    @Override
    public void put(String key, int value) {
        editor.putInt(key, value);
        editor.commit();
    }

    @Override
    public void put(String key, long value) {
        editor.putLong(key, value);
        editor.commit();
    }

    @Override
    public void put(String key, float value) {
        editor.putFloat(key, value);
        editor.commit();
    }

    /**
     * 注意：由于SP不支持写入Double值，为了避免丢失精度，对于大于Float的值会采用String的方式写入，
     * @param key
     * @param value
     */
    @Override
    public void put(String key, double value) {
        if (value > Float.MAX_VALUE) {
            editor.putString(key, value + "");
        } else {
            editor.putFloat(key, (float) value);
        }
        editor.commit();
    }

    @Override
    public void put(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    @Override
    public void put(String key, Set<String> value) {
        editor.putStringSet(key, value);
        editor.commit();
    }

    @Override
    public boolean getBoolean(String key, boolean defaultValue) {
        return preferences.getBoolean(key, defaultValue);
    }

    @Override
    public int getInt(String key, int defaultValue) {
        return preferences.getInt(key, defaultValue);
    }

    @Override
    public long getLong(String key, long defaultValue) {
        return preferences.getLong(key, defaultValue);
    }

    @Override
    public float getFloat(String key, float defaultValue) {
        return preferences.getFloat(key, defaultValue);
    }

    @Override
    public double getDouble(String key, double defaultValue) {
        // 由于SP不支持Double值的写入，所以读取时需要额外处理
        if (!contains(key)) {
            return defaultValue;
        }

        String value = preferences.getString(key, null);
        if (!TextUtils.isEmpty(value)) {
            try {
                return parseDouble(value);
            } catch (Exception e) {
                e.printStackTrace(System.out);
            }
            return defaultValue;
        } else {
            return preferences.getFloat(key, 0f);
        }
    }

    @Override
    public String getString(String key, String defaultValue) {
        return preferences.getString(key, defaultValue);
    }


    @Override
    public Set<String> getStringSet(String key, Set<String> defaultValue) {
        return preferences.getStringSet(key, defaultValue);
    }


    @Override
    public boolean contains(String key) {
        return preferences.contains(key);
    }

    @Override
    public void remove(String key) {
        editor.remove(key);
        editor.commit();
    }

    @Override
    public void clear() {
        editor.clear();
        editor.commit();
    }
}
