package com.sxu.baselibrary.datasource.http.impl.listener;

/**
 * 应答监听
 */
public interface IResponseListener<T> {

    /**
     * 应答监听
     * @param response http应答的原始数据
     */
    void onResponse(T response);
}
