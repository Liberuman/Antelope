package com.sxu.baselibrary.commonutils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.ViewConfiguration;

import java.lang.reflect.Method;
import java.util.UUID;

import static android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;

/*******************************************************************************
 * Description: 获取设备的相关信息
 *
 * Author: Freeman
 *
 * Date: 2017/06/20
 *
 * Copyright: all rights reserved by Freeman.
 *******************************************************************************/

public final class DeviceUtil {

	private DeviceUtil() {
		throw new UnsupportedOperationException();
	}

	/**
	 * 空的DeviceId
	 */
	private final static String EMPTY_DEVICE_ID = "0";
	/**
	 * 是否有虚拟按键
	 */
	private final static String NAV_BAR_STATUS = "0";

	/**
	 * 获取手机的Mac地址
	 * @param context
	 * @return
	 */
	@SuppressLint("MissingPermission")
	public static String getMacAddress(Context context) {
		String macAddress = "";
		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		if (wifiManager != null) {
			try {
				WifiInfo wifiInfo = wifiManager.getConnectionInfo();
				macAddress = wifiInfo != null ? wifiInfo.getMacAddress() : "";
			} catch (Exception e) {
				e.printStackTrace(System.out);
			}
		}

		return macAddress;
	}

	/**
	 * 获取手机的IMEI码
	 * @param context
	 * @return
	 */
	@SuppressLint("MissingPermission")
	public static String getDeviceId(Context context) {
		TelephonyManager phoneManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		if (phoneManager != null) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
				return phoneManager.getImei();
			} else {
				return phoneManager.getDeviceId();
			}
		}

		return "";
	}

	/**
	 * 获取设备Token
	 * @param context
	 * @return
	 */
	public static String getDeviceToken(Context context) {
		String deviceId = getDeviceId(context);
		if (TextUtils.isEmpty(deviceId) || EMPTY_DEVICE_ID.equals(deviceId)) {
			String macAddress = getMacAddress(context);
			if (!TextUtils.isEmpty(macAddress)) {
				deviceId = macAddress;
			} else {
				UUID uuid = UUID.randomUUID();
				deviceId = uuid.toString();
			}
		}

		return deviceId;
	}

	/**
	 * 获取OS的版本
	 * @return
	 */
	public static String getOSVersion() {
		return Build.VERSION.RELEASE;
	}

	/**
	 * 获取手机生产生信息
	 * @return
	 */
	public static String getManufactuer() {
		return Build.MANUFACTURER;
	}

	/**
	 * 获取设备AndroidId
	 * @return
	 */
	public static String getAndroidId() {
		return Build.ID;
	}

	/**
	 * 判断是否存在虚拟按键
	 * @return
	 */
	public static boolean checkDeviceHasVisualKey(Context context) {
		boolean hasVisualKey;
		Resources rs = Resources.getSystem();
		int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
		if (id > 0) {
			hasVisualKey = rs.getBoolean(id);
			try {
				Class<?> systemPropertiesClass = Class.forName("android.os.SystemProperties");
				Method m = systemPropertiesClass.getMethod("get", String.class);
				String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
				hasVisualKey = NAV_BAR_STATUS.equals(navBarOverride);
			} catch (Exception e) {
				e.printStackTrace(System.out);
			}
		} else {
			hasVisualKey = !ViewConfiguration.get(context).hasPermanentMenuKey();
		}

		return hasVisualKey;
	}

	/**
	 * 虚拟按键是否显示
	 * @param context
	 * @return
	 */
	public static boolean visualKeyIsVisible(Context context) {
		if (context instanceof Activity) {
			return ((Activity) context).getWindow().getDecorView().getSystemUiVisibility()
					!= SYSTEM_UI_FLAG_HIDE_NAVIGATION;
		}
		return false;
	}

	/**
	 * 获取虚拟按钮的高度
	 * @param context
	 * @return
	 */
	public static int getVisualKeyHeight(Context context) {
		int height = 0;
		if (checkDeviceHasVisualKey(context) && visualKeyIsVisible(context)){
			Resources res = Resources.getSystem();
			int resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android");
			if (resourceId > 0) {
				height = res.getDimensionPixelSize(resourceId);
			}
		}
		return height;
	}

}
