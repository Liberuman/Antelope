package com.sxu.baselibrary.commonutils;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationManagerCompat;

/*******************************************************************************
 * Description: Notification工具类
 *
 * Author: Freeman
 *
 * Date: 2017/06/20
 *
 * Copyright: all rights reserved by Freeman.
 *******************************************************************************/
public final class NotificationUtil {

	private NotificationUtil() {
		throw new UnsupportedOperationException();
	}

	/**
	 * 通知是否被打开
	 *
	 * @param context
	 * @return
	 */
	public static boolean notificationEnabled(Context context) {
		boolean isOpened = false;
		try {
			isOpened = NotificationManagerCompat.from(context).areNotificationsEnabled();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return isOpened;
	}

	/**
	 * 打开应用通知设置页面
	 *
	 * @param context
	 */
	public static void gotoNotificationSetting(Context context) {
		try {
			Intent intent = new Intent();
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
				intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
				intent.putExtra("android.provider.extra.APP_PACKAGE", context.getPackageName());
			} else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
				intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
				intent.putExtra("app_package", context.getPackageName());
				intent.putExtra("app_uid", context.getApplicationInfo().uid);
			} else {
				intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
				intent.setData(Uri.fromParts("package", context.getPackageName(), null));
			}
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
		} catch (Exception e) {
			Intent intent = new Intent();
			intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
			intent.setData(Uri.fromParts("package", context.getPackageName(), null));
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
		}
	}
}
