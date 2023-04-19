package com.sxu.baselibrary.datasource.http.impl;

import android.content.Context;
import android.text.TextUtils;

import com.ihsanbal.logging.Level;
import com.ihsanbal.logging.LoggingInterceptor;
import com.readystatesoftware.chuck.ChuckInterceptor;
import com.sxu.baselibrary.commonutils.BaseContentProvider;
import com.sxu.baselibrary.commonutils.CollectionUtil;
import com.sxu.baselibrary.commonutils.LogUtil;
import com.sxu.baselibrary.commonutils.NetworkUtil;
import com.sxu.baselibrary.datasource.http.bean.ResponseBean;
import com.sxu.baselibrary.datasource.http.impl.gson.GsonManager;
import com.sxu.baselibrary.datasource.http.impl.interceptor.BaseInterceptor;
import com.sxu.baselibrary.datasource.http.impl.interceptor.HttpCacheInterceptor;
import com.sxu.baselibrary.datasource.http.impl.listener.IResponseListener;
import com.sxu.baselibrary.datasource.http.impl.listener.MultiRequestListener;
import com.sxu.baselibrary.datasource.http.impl.listener.RequestListener;
import com.sxu.baselibrary.datasource.http.impl.listener.SimpleRequestListener;
import com.sxu.baselibrary.datasource.http.util.PingUtil;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Interceptor;
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

import static android.util.Log.VERBOSE;
import static androidx.lifecycle.Lifecycle.State.DESTROYED;

/******************************************************************************
 * Description: 网络组件的封装
 *
 * Author: Freeman
 *
 * Date: 2018/1/16
 *
 * Copyright: all rights reserved by Freeman.
 *******************************************************************************/
public class HttpManager {

    private static boolean isInit = false;
    private static String mBaseUrl;
    private static Context mContext;

    private OkHttpClient httpClient;
    private Retrofit retrofit;
    private WeakHashMap<Integer, Subscription> subscriptionHashMap = new WeakHashMap<>();
    private List<Observable> failedUrlList = new ArrayList<>();

    public static void init(String baseUrl) {
        init(BaseContentProvider.context, baseUrl);
    }

    public static void init(Context context, String baseUrl) {
        isInit = true;
        mContext = context.getApplicationContext();
        mBaseUrl = baseUrl;
    }

    public static void init(Context context, @NonNull final List<String> hostList) {
        isInit = true;
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

    public static boolean isInit() {
        return isInit;
    }

    private HttpManager() {
        final X509TrustManager trustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(
                    java.security.cert.X509Certificate[] chain,
                    String authType) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(
                    java.security.cert.X509Certificate[] chain,
                    String authType) throws CertificateException {
            }

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };

        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, new TrustManager[]{trustManager}, new java.security.SecureRandom());
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }

        httpClient = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .addInterceptor(new LoggingInterceptor.Builder()
                        .setLevel(Level.BASIC)
                        .log(VERBOSE)
                        .build())
                .addInterceptor(new ChuckInterceptor(mContext))
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
//                    if (!TextUtils.isEmpty(mBaseUrl) && mBaseUrl.contains(hostname)) {
//                        return true;
//                    } else {
//                        HostnameVerifier hostnameVerifier = HttpsURLConnection.getDefaultHostnameVerifier();
//                        return hostnameVerifier.verify(hostname, session);
//                    }
                        return true;
                    }
                })
                .sslSocketFactory(sslContext.getSocketFactory(), trustManager)
                .build();
        retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(GsonManager.defaultInstance()))
                .client(httpClient)
                .baseUrl(mBaseUrl)
                .build();
    }

    /**
     * 添加拦截器
     * @param interceptor
     */
    public HttpManager addInterceptor(Interceptor interceptor) {
        httpClient.interceptors().add(interceptor);
        return SingletonHolder.INSTANCE;
    }

    /**
     * 添加网络拦截器
     * @param interceptor
     */
    public HttpManager addNetworkInterceptor(Interceptor interceptor) {
        httpClient.networkInterceptors().add(interceptor);
        return SingletonHolder.INSTANCE;
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
    public <T> void executeRequest(@Nullable LifecycleOwner owner, @NonNull final Observable<T> observable,
                                   SimpleRequestListener<T> listener) {
        if (!checkLifecycle(owner)) {
            return;
        }

        final Subscription subscription = observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<T>() {
                    @Override
                    public void onCompleted() {
                        unsubscribeSubscription(observable.hashCode());
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace(System.out);
                        if (!NetworkUtil.isConnected(mContext)) {
                            failedUrlList.add(observable);
                        }
                        listener.onFailure(0, e.getMessage());
                    }

                    @Override
                    public void onNext(T o) {
                        failedUrlList.remove(observable);
                        listener.onSuccess(o);
                    }
                });

        // 为网络请求关联组件的生命周期
        if (owner != null) {
            owner.getLifecycle().addObserver(new LifecycleSubscription(subscription));
        }
        subscriptionHashMap.put(observable.hashCode(), subscription);
    }

    /**
     * 并行执行多个网络请求
     * @param observableList
     * @param listener
     */
    public void executeRequest(@Nullable LifecycleOwner owner, @NonNull final List<Observable<Object>> observableList,
                               final MultiRequestListener listener) {
        if (!checkLifecycle(owner)) {
            return;
        }

        for (Observable item : observableList) {
            item.subscribeOn(Schedulers.io());
        }

        final Subscription subscription = Observable.zip((Iterable)observableList, new FuncN<List<Object>>() {
            @Override
            public List<Object> call(Object... args) {
                return Arrays.asList(args);
            }
        })
                .subscribeOn(Schedulers.io())
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

        // 为网络请求关联组件的生命周期
        if (owner != null) {
            owner.getLifecycle().addObserver(new LifecycleSubscription(subscription));
        }
        subscriptionHashMap.put(observableList.hashCode(), subscription);
    }

    public void executeRequest(LifecycleOwner owner, @NonNull String url, SimpleRequestListener listener) {
        executeRequest(owner, url, null, listener);
    }

    /**
     * 非标准数据协议的网络请求
     * @param url
     * @param paramsMap 请求参数，不为空时默认为post请求
     * @param listener 网络请求监听
     */
    public void executeRequest(@Nullable LifecycleOwner owner, @NonNull String url, Map<String, String> paramsMap, final SimpleRequestListener listener) {
        if (!checkLifecycle(owner)) {
            return;
        }

        Request.Builder requestBuilder = new Request.Builder().url(url);
        if (!CollectionUtil.isEmpty(paramsMap)) {
            FormBody.Builder builder = new FormBody.Builder();
            Iterator<Map.Entry<String, String>> iterator = paramsMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> entry = iterator.next();
                builder.add(entry.getKey(), entry.getValue());
            }
            requestBuilder.post(builder.build());
        }

        Call newCall = httpClient.newCall(requestBuilder.build());
        // 为网络请求关联组件的生命周期
        if (owner != null) {
            owner.getLifecycle().addObserver(new LifecycleCall(newCall));
        }
        newCall.enqueue(new Callback() {
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
                        listener.onSuccess(content);
                    } else {
                        listener.onFailure(response.code(), response.message());
                    }
                } else {
                    listener.onFailure(response.code(), response.message());
                }
            }
        });
    }

    private void unsubscribeSubscription(int hasCode) {
        Subscription subscript = subscriptionHashMap.get(hasCode);
        if (subscript != null && !subscript.isUnsubscribed()) {
            subscript.unsubscribe();
            subscriptionHashMap.remove(hasCode);
        }
    }

    /**
     * 检查生命周期的有效性
     * @param owner
     * @return true表示当前组件在运行中，否则表示已关闭
     */
    public boolean checkLifecycle(LifecycleOwner owner) {
        return owner != null && owner.getLifecycle().getCurrentState() != DESTROYED;
    }

    public static HttpManager getInstance() {
        return HttpManager.SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        public final static HttpManager INSTANCE = new HttpManager();
    }
}
