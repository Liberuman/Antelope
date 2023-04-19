package com.sxu.baselibrary.datasource.http.impl;

import java.lang.ref.WeakReference;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import okhttp3.Call;
import rx.Subscription;

/*******************************************************************************
 * 网络请求添加生命周期
 *
 * @author: Freeman
 *
 * @date: 2021/3/18
 *
 * Copyright: all rights reserved by Freeman.
 *******************************************************************************/
public class LifecycleCall implements LifecycleEventObserver {

	private WeakReference<Call> reference;

	public LifecycleCall(Call call) {
		this.reference = new WeakReference<>(call);
	}

	@Override
	public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
		if (event != Lifecycle.Event.ON_DESTROY) {
			return;
		}

		// 取消网络请求
		Call value = reference.get();
		if (value != null && !value.isCanceled()) {
			value.cancel();
		}
	}
}
