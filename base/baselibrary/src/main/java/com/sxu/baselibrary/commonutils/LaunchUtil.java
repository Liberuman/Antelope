package com.sxu.baselibrary.commonutils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.CalendarContract;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.text.TextUtils;

import java.io.File;

import androidx.core.content.FileProvider;

/*******************************************************************************
 * Description: 启动其他的APP
 *
 * Author: Freeman
 *
 * Date: 2017/06/20
 *
 * Copyright: all rights reserved by Freeman.
 *******************************************************************************/
public final class LaunchUtil {

	private LaunchUtil() {
		throw new UnsupportedOperationException();
	}

	/**
	 * 打开浏览器
	 * @param context
	 */
	public static void openBrowse(Context context, String url) {
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		startActivitySafely(context, intent);
	}

	/**
	 * 使用浏览器的搜索功能
	 * @param key
	 */
	public void searcyByBrowse(Context context, String key) {
		Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
		intent.putExtra(SearchManager.QUERY, key);
		startActivitySafely(context, intent);
	}

	/**
	 * 打开手机中支持搜索功能的APP
	 * @param packageName 指定APP的包名
	 * @param key
	 */
	public void searcy(Context context, String packageName, String key) {
		Intent intent = new Intent(Intent.ACTION_SEARCH);
		intent.putExtra(SearchManager.QUERY, key);
		if (!TextUtils.isEmpty(packageName)) {
			intent.setPackage(packageName);
		}
 		startActivitySafely(context, intent);
	}

	/**
	 * 选择指定类型的文件
	 * @param context
	 * @param fileType MIME类型的字符串
	 * @param requestCode
	 */
	public void selectSpecificTypeFile(Context context, String fileType, int requestCode) {
		Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
		intent.setType(fileType);
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		startActivityForResultSafely(context, intent, requestCode);
	}

	/**
	 * 打开应用市场
	 * @param context
	 */
	public static void openAppMarket(Context context) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse("market://details?id=" + context.getPackageName()));
		startActivitySafely(context, intent);
	}

	/**
	 * 发送短信
	 * @param context
	 * @param telNumber
	 * @param content
	 */
	public static void sendMessage(Context context, String telNumber, String content) {
		if (!VerificationUtil.isValidTelNumber(telNumber)) {
			ToastUtil.showShort("请输入正确的手机号");
			return;
		}

		Uri uri = Uri.parse("smsto:" + telNumber);
		Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
		intent.putExtra("sms_body", content);
		startActivitySafely(context, intent);
	}

	/**
	 * 打开短信界面
	 * @param context
	 * @param telNumber
	 */
	public static void openMessage(Context context, String telNumber) {
		Uri uri = Uri.parse("smsto:" + telNumber);
		Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
		startActivitySafely(context, intent);
	}

	/**
	 * 打开拨号界面
	 * @param context
	 * @param telNumber
	 */
	@SuppressLint("MissingPermission")
	public static void openPhone(Context context, String telNumber) {
		Uri uri = Uri.parse("tel:" + telNumber);
		Intent intent = new Intent(Intent.ACTION_CALL, uri);
		startActivitySafely(context, intent);
	}

	/**
	 * 添加日历事件
	 * @param context
	 * @param title
	 * @param begin
	 * @param end
	 */
	public static void addCalendarEvent(Context context, String title, long begin, long end) {
		Intent intent = new Intent(Intent.ACTION_INSERT)
			.setData(CalendarContract.Events.CONTENT_URI)
			.putExtra(CalendarContract.Events.TITLE, title)
			.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, begin)
			.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, end);
		startActivitySafely(context, intent);
	}

	/**
	 * 选择联系人
	 * @param context
	 */
	public static void selectContact(Context context, int requestCode) {
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
		startActivityForResultSafely(context, intent, requestCode);
	}

	/**
	 * 插入联系人
	 * @param context
	 * @param name
	 * @param phone
	 */
	public void insertContact(Context context, String name, String phone) {
		Intent intent = new Intent(Intent.ACTION_INSERT);
		intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
		intent.putExtra(ContactsContract.Intents.Insert.NAME, name);
		intent.putExtra(ContactsContract.Intents.Insert.PHONE, phone);
		startActivitySafely(context, intent);
	}

	/**
	 * 安装APP
	 * @param context
	 * @param fileName
	 */
	public static void installApp(Context context, String fileName) {
		Uri uri = Uri.fromFile(new File(fileName));
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(uri, "application/vnd.android.package-archive");
		startActivitySafely(context, intent);
	}

	/**
	 * 打开应用的设置界面
	 * @param context
	 */
	public static void openAppSetting(Context context) {
		Intent intent = new Intent(Settings.ACTION_APPLICATION_SETTINGS);
		intent.setData(Uri.fromParts("package", context.getPackageName(), null));
		startActivitySafely(context, intent);
	}

	/**
	 * 回到桌面
	 * @param context
	 */
	public static void backToLauncher(Context context) {
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		startActivitySafely(context, intent);
	}

	/**
	 * APP进行隐式跳转或跳转到第三方APP页面时使用此方法
	 * @param context
	 * @param intent
	 */
	public static void startActivitySafely(Context context, Intent intent) {
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		if (intent.resolveActivity(context.getPackageManager()) != null) {
			context.startActivity(intent);
		}
	}

	public static void startActivityForResultSafely(Context context, Intent intent, int requestCode) {
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		if (context instanceof Activity && intent.resolveActivity(context.getPackageManager()) != null) {
			((Activity) context).startActivityForResult(intent, requestCode);
		}
	}

	/**
	 * 跳转到安装程序安装APP
	 * @param context
	 * @param apkFile
	 * @throws ActivityNotFoundException
	 */
	public static void installApp(Context context, File apkFile) throws ActivityNotFoundException {
		Uri uri;
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			uri = FileProvider.getUriForFile(context.getApplicationContext(), context.getPackageName() + ".fileprovider", apkFile);
			intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
		} else {
			uri = Uri.fromFile(apkFile);
		}
		intent.setDataAndType(uri, "application/vnd.android.package-archive");
		if (intent.resolveActivity(context.getPackageManager()) == null) {
			throw new ActivityNotFoundException();
		}

		context.startActivity(intent);
		if (context instanceof Activity) {
			((Activity) context).finish();
		}
	}
}
