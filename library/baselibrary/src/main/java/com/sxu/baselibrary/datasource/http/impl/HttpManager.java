package com.sxu.baselibrary.datasource.http.impl;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Space;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sxu.baselibrary.commonutils.BaseContentProvider;
import com.sxu.baselibrary.commonutils.CollectionUtil;
import com.sxu.baselibrary.commonutils.LogUtil;
import com.sxu.baselibrary.commonutils.NetworkUtil;
import com.sxu.baselibrary.commonutils.SpannableStringUtil;
import com.sxu.baselibrary.datasource.http.bean.ResponseBean;
import com.sxu.baselibrary.datasource.http.impl.gson.GsonManager;
import com.sxu.baselibrary.datasource.http.impl.interceptor.BaseInterceptor;
import com.sxu.baselibrary.datasource.http.impl.interceptor.HttpCacheInterceptor;
import com.sxu.baselibrary.datasource.http.impl.listener.MultiRequestListener;
import com.sxu.baselibrary.datasource.http.impl.listener.RequestListener;
import com.sxu.baselibrary.datasource.http.impl.listener.SimpleRequestListener;
import com.sxu.baselibrary.datasource.http.util.PingUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.FuncN;
import rx.schedulers.Schedulers;

/******************************************************************************
 * Description: 网络组件的封装
 *
 * Author: Freeman
 *
 * Date: 2018/1/16
 *
 * Copyright: all rights reserved by Freeman.
 *******************************************************************************/
public class HttpManager implements LifecycleObserver {

    private static boolean isInited = false;
    private static String mBaseUrl;
    private static Context mContext;

    private OkHttpClient httpClient;
    private Retrofit retrofit;
    private WeakHashMap<Integer, Subscription> subscriptionHashMap = new WeakHashMap<>();
    private List<rx.Observable> failedUrlList = new ArrayList<>();

    public static void init(String baseUrl) {
        init(BaseContentProvider.context, baseUrl);
    }

    public static void init(Context context, String baseUrl) {
        isInited = true;
        mContext = context.getApplicationContext();
        mBaseUrl = baseUrl;
    }

    public static void init(Context context, @NonNull final List<String> hostList) {
        isInited = true;
        mContext = context.getApplicationContext();
        List<String> IPList = new ArrayList<>();
        for (String host : hostList) {
            host = host.replace("http://", "").replace("https://", "");
            int endIndex = host.indexOf(":");
            if (endIndex < 0) {
                endIndex = host.indexOf("/");
            }
            if (endIndex < 0) {
                endIndex = host.length();
            }
            host = host.substring(0, endIndex);
            IPList.add(host);
        }
        PingUtil.getBestIP(IPList, new PingUtil.OnGetBestIpListener() {
            @Override
            public void onGetBestIP(String bestIP, int index) {
                mBaseUrl = hostList.get(index);
                LogUtil.i("mBaseUrl===" + mBaseUrl);
            }
        });
    }

    public static boolean isInited() {
        return isInited;
    }

    private HttpManager() {
        httpClient = new OkHttpClient.Builder()
                .addInterceptor(new BaseInterceptor(mContext))
//				.addInterceptor(new CookieInterceptor())
                .addNetworkInterceptor(new HttpCacheInterceptor(mContext))
                .addNetworkInterceptor(new HttpLoggingInterceptor())
                .cache(new Cache(mContext.getCacheDir(), 20 * 1024 * 1024))
//				.cookieJar(new CookieJar() {
//					@Override
//					public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
//
//					}
//
//					@Override
//					public List<Cookie> loadForRequest(HttpUrl url) {
//						return new ArrayList<>();
//					}
//				})
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        if (!TextUtils.isEmpty(mBaseUrl) && mBaseUrl.contains(hostname)) {
                            return true;
                        } else {
                            HostnameVerifier hostnameVerifier = HttpsURLConnection.getDefaultHostnameVerifier();
                            return hostnameVerifier.verify(hostname, session);
                        }
                    }
                })
                .build();
        retrofit = new Retrofit.Builder()
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(GsonManager.defaultInstance()))
            .client(httpClient)
            .baseUrl(mBaseUrl)
            .build();
    }

    public <T> T getApiService(Class<T> classT) {
        return retrofit.create(classT);
    }

    /**
     * 执行网络请求
     * @param observable
     * @param listener
     * @param <T>
     */
    public <T> void executeRequest(@NonNull final rx.Observable<ResponseBean<T>> observable,
                                   final RequestListener listener) {
        final Subscription subscription = observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseBean>() {
                    @Override
                    public void onCompleted() {
                        unsubscribeSubscription(observable.hashCode());
                        failedUrlList.remove(observable);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace(System.out);
                        ResponseProcessor.process(null, listener);
                        unsubscribeSubscription(observable.hashCode());
                        if (!NetworkUtil.isConnected(mContext)) {
                            failedUrlList.add(observable);
                        }
                    }

                    @Override
                    public void onNext(ResponseBean result) {
                        ResponseProcessor.process(result, listener);
                    }
                });
        subscriptionHashMap.put(observable.hashCode(), subscription);
    }

    /**
     * 并行执行多个网络请求
     * @param observableList
     * @param listener
     */
    public void executeRequest(@NonNull final List<rx.Observable> observableList,
                                   final MultiRequestListener listener) {
        for (rx.Observable item : observableList) {
            item.subscribeOn(Schedulers.io());
        }

        final Subscription subscription = Observable.zip((Iterable)observableList, new FuncN<List<Object>>() {
            @Override
            public List<Object> call(Object... args) {
                return Arrays.asList(args);
            }
        })
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<List<Object>>() {
            @Override
            public void onCompleted() {
                unsubscribeSubscription(observableList.hashCode());
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace(System.out);
                if (listener != null) {
                    listener.onFailure(0, e.getMessage());
                }
                unsubscribeSubscription(observableList.hashCode());
            }

            @Override
            public void onNext(List<Object> result) {
                if (listener != null) {
                    listener.onSuccess(result);
                }
            }
        });
        subscriptionHashMap.put(observableList.hashCode(), subscription);
    }

    public void executeRequest(@NonNull String url, SimpleRequestListener listener) {
        executeRequest(url, null, listener);
    }

    /**
     * 非标准数据协议的网络请求
     * @param url
     * @param paramsMap
     * @param listener
     */
    public void executeRequest(@NonNull String url, Map<String, String> paramsMap, final SimpleRequestListener listener) {
        if (httpClient == null) {
            throw new NullPointerException("httpClient is null, please call init method.");
        }

        Request.Builder requestBuilder = new Request.Builder()
            .url(url);
        if (!CollectionUtil.isEmpty(paramsMap)) {
            FormBody.Builder builder = new FormBody.Builder();
            Iterator<Map.Entry<String, String>> iterator = paramsMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> entry = iterator.next();
                builder.add(entry.getKey(), entry.getValue());
            }
            requestBuilder.post(builder.build());
        }
        httpClient.newCall(requestBuilder.build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (listener == null) {
                    return;
                }

                listener.onFailure(0, e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (listener == null) {
                    return;
                }

                if (response.isSuccessful()) {
                    String content = response.body().string();
                    if (TextUtils.isEmpty(content)) {
                        listener.onSuccess(response.message());
                    } else {
                        listener.onFailure(response.code(), response.message());
                    }
                } else {
                    listener.onFailure(response.code(), response.message());
                }
            }
        });
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onActivityDestroied() {

    }
    private void unsubscribeSubscription(int hashCode) {
        Subscription subscript = subscriptionHashMap.get(hashCode);
        if (subscript != null && !subscript.isUnsubscribed()) {
            subscript.unsubscribe();
            subscriptionHashMap.remove(hashCode);
        }
    }

    public static HttpManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        public final static HttpManager INSTANCE = new HttpManager();
    }
}
