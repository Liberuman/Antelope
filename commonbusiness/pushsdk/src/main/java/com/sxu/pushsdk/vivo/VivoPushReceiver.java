package com.sxu.pushsdk.vivo;

import android.content.Context;

import com.vivo.push.model.UPSNotificationMessage;
import com.vivo.push.model.UnvarnishedMessage;
import com.vivo.push.sdk.OpenClientPushMessageReceiver;

/*******************************************************************************
 * Vivo推送
 *
 * @author: Freeman
 *
 * @date: 2020/6/17
 *
 * Copyright: all rights reserved by Freeman.
 *******************************************************************************/
public class VivoPushReceiver extends OpenClientPushMessageReceiver {

	@Override
	public void onReceiveRegId(Context context, String s) {

	}

	@Override
	public void onTransmissionMessage(Context context, UnvarnishedMessage unvarnishedMessage) {
		super.onTransmissionMessage(context, unvarnishedMessage);
	}


	@Override
	public void onNotificationMessageClicked(Context context, UPSNotificationMessage upsNotificationMessage) {

	}
}
