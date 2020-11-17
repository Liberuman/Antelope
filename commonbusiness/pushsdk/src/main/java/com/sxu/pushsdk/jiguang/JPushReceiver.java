package com.sxu.pushsdk.jiguang;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.sxu.pushsdk.PushManager;

import cn.jpush.android.api.CustomMessage;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.NotificationMessage;
import cn.jpush.android.service.JPushMessageReceiver;


/*******************************************************************************
 * Description: 极光推送（PS: 不定义此Receiver时，点击通知默认会打开APP，且接收不到自定义消息）
 *
 * Author: Freeman
 *
 * Date: 2018/3/7
 *
 * Copyright: all rights reserved by Freeman.
 *******************************************************************************/
public class JPushReceiver extends JPushMessageReceiver {

	private static final String TAG = "JPush";


	@Override
	public void onRegister(Context context, String s) {
		super.onRegister(context, s);
		Log.d(TAG, "[MyReceiver] 接收Registration Id : " + s);
	}

	@Override
	public void onConnected(Context context, boolean b) {
		super.onConnected(context, b);
	}

	@Override
	public void onMessage(Context context, CustomMessage customMessage) {
		super.onMessage(context, customMessage);
		PushManager.getInstance().getPushListener().onReceiveMessage(context,
			PushManager.PUSH_TYPE_JPUSH, customMessage);
	}

	@Override
	public void onNotifyMessageArrived(Context context, NotificationMessage notificationMessage) {
		super.onNotifyMessageArrived(context, notificationMessage);
		PushManager.getInstance().getPushListener().onReceiveNotification(context,
			PushManager.PUSH_TYPE_JPUSH, notificationMessage.notificationExtras);
	}

	@Override
	public void onNotifyMessageOpened(Context context, NotificationMessage notificationMessage) {
		super.onNotifyMessageOpened(context, notificationMessage);
		PushManager.getInstance().getPushListener().onReceiveNotification(context,
			PushManager.PUSH_TYPE_JPUSH, notificationMessage.notificationExtras);
	}
}

