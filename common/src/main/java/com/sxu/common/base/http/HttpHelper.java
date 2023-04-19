package com.sxu.common.base.http;

import androidx.lifecycle.LifecycleOwner;

import com.blankj.utilcode.util.NetworkUtils;
import com.sxu.basecomponent.manager.SingletonManager;
import com.sxu.baselibrary.datasource.http.impl.HttpManager;
import com.sxu.baselibrary.datasource.http.impl.listener.IResponseListener;
import com.sxu.baselibrary.datasource.http.impl.listener.RequestListener;
import com.sxu.baselibrary.datasource.http.impl.listener.SimpleRequestListener;
import com.sxu.common.base.http.bean.ResponseBean;
import com.sxu.common.manager.base.bean.UserInfoBean;

import java.util.Map;

import rx.Observable;
import rx.Observer;

/*******************************************************************************
 * Description: 网络请求包装类
 *
 * Author: Freeman
 *
 * Date: 2021/10/12
 *
 * Copyright: all rights reserved by Freeman.
 *******************************************************************************/
public class HttpHelper {

    /**
     * 网络请求
     * @param owner 请求所在的组件
     * @param observer 请求对应的Observable
     * @param listener 请求监听
     * @param <T> 请求应答对应的数据结构
     */
    public static <T> void request(LifecycleOwner owner, Observable observer, SimpleRequestListener<T> listener) {
        HttpManager.getInstance().executeRequest(owner, observer, new SimpleRequestListener<ResponseBean<T>>() {
            @Override
            public void onSuccess(ResponseBean<T> response) {
                listener.onSuccess(response.data);
            }

            @Override
            public void onFailure(int code, String msg) {
                listener.onFailure(code, msg);
            }
        });
    }

    /**
     * 指定地址的请求，用于非主域名的请求
     * @param owner 请求所在的组件
     * @param url 请求链接
     * @param listener 请求监听
     */
    public static void request(LifecycleOwner owner, String url, SimpleRequestListener listener) {
        request(owner, url, null, listener);
    }

    /**
     * 指定地址的请求，用于非主域名的请求
     * @param owner 请求所在的组件
     * @param url 请求链接
     * @param paramsMap post时的请求参数
     * @param listener 请求监听
     */
    public static void request(LifecycleOwner owner, String url, Map<String, String> paramsMap, SimpleRequestListener listener) {
        HttpManager.getInstance().executeRequest(owner, url, paramsMap, listener);
    }

    /**
     * 是否是网络错误
     * @param code
     * @return
     */
    public static boolean isNetworkError(int code) {
        return !NetworkUtils.isAvailable() || code != 200;
    }
}
