package com.sxu.basecomponent.utils;

import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

/*******************************************************************************
 * 避免Handle引起内存泄漏
 *
 * @author: Freeman
 *
 * @date: 2020/10/5
 *
 * Copyright: all rights reserved by Freeman.
 *******************************************************************************/
public class SafeHandler extends Handler {

	private final WeakReference<? extends Callback> activityRef;

	public SafeHandler(Callback object) {
		this.activityRef = new WeakReference<>(object);
	}

	@Override
	public void handleMessage(Message msg) {
		super.handleMessage(msg);
		Callback callback = activityRef.get();
		if (callback == null) {
			return;
		}

		callback.handleMessage(msg);
	}
}
