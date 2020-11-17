package com.sxu.pushsdk.huawei;

import com.huawei.hms.push.HmsMessageService;
import com.huawei.hms.push.RemoteMessage;
import com.sxu.pushsdk.PushManager;

/*******************************************************************************
 *
 *
 * @author: Freeman
 *
 * @date: 2020/6/17
 *
 * Copyright: all rights reserved by Freeman.
 *******************************************************************************/
public class HwPushService extends HmsMessageService {

	@Override
	public void onMessageReceived(RemoteMessage var1) {
		PushManager.getInstance().getPushListener().onNotificationClicked(getApplicationContext(),
			PushManager.PUSH_TYPE_HUAWEI, var1);
	}

	@Override
	public void onNewToken(String var1) {

	}
}
