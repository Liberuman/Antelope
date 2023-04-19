package com.sxu.baselibrary.datasource.http.impl;

import java.lang.ref.WeakReference;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
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
public class LifecycleSubscription implements LifecycleEventObserver {

	private WeakReference<Subscription> reference;

	public LifecycleSubscription(Subscription subscription) {
		this.reference = new WeakReference<>(subscription);
	}

	@Override
	public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
		if (event != Lifecycle.Event.ON_DESTROY) {
			return;
		}

		// 取消网络请求
		Subscription subscription = reference.get();
		if (subscription != null && !subscription.isUnsubscribed()) {
			subscription.unsubscribe();
		}
	}
}
