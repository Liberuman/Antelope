package com.sxu.pushsdk;

import android.os.Build;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/*******************************************************************************
 * Description: 检查ROM类型
 *
 * Author: Freeman
 *
 * Date: 2018/02/26
 *
 * Copyright: all rights reserved by Freeman.
 *******************************************************************************/
 final class RomUtil {

	private RomUtil() {
		throw new UnsupportedOperationException();
	}

	public static final String ROM_FLYME = "FLYME";

	private static final String KEY_VERSION_MIUI = "ro.miui.ui.version.name";
	private static final String KEY_VERSION_EMUI = "ro.build.version.emui";
	private static final String KEY_VERSION_OPPO = "ro.build.version.opporom";
	private static final String KEY_VERSION_SMARTISAN = "ro.smartisan.version";
	private static final String KEY_VERSION_VIVO = "ro.vivo.os.version";

	/**
	 * 是否是华为系统
	 * @return
	 */
	public static boolean isEmui() {
		return !TextUtils.isEmpty(getRomProp(KEY_VERSION_EMUI));
	}

	/**
	 * 是否是小米系统
	 * @return
	 */
	public static boolean isMiui() {
		return !TextUtils.isEmpty(getRomProp(KEY_VERSION_MIUI));
	}

	/**
	 * 是否是VIVO系统
	 * @return
	 */
	public static boolean isVivo() {
		return !TextUtils.isEmpty(getRomProp(KEY_VERSION_VIVO));
	}

	/**
	 * 是否是OPPO系统
	 * @return
	 */
	public static boolean isOppo() {
		return !TextUtils.isEmpty(getRomProp(KEY_VERSION_OPPO));
	}

	/**
	 * 是否是魅族系统
	 * @return
	 */
	public static boolean isFlyme() {
		return Build.DISPLAY.toUpperCase().contains(ROM_FLYME);
	}

	/**
	 * 是否是锤子系统
	 * @return
	 */
	public static boolean isSmartisan() {
		return !TextUtils.isEmpty(getRomProp(KEY_VERSION_SMARTISAN));
	}

	public static String getRomProp(String name) {
		Process process = null;
		try {
			process = Runtime.getRuntime().exec("getprop " + name);
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
		if (process == null) {
			return null;
		}

		String line = null;
		try (InputStream inputStream = process.getInputStream();
		     InputStreamReader inputReader = new InputStreamReader(inputStream, "UTF-8");
			 BufferedReader reader = new BufferedReader(inputReader, 1024)) {
			line = reader.readLine();
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
		return line;
	}
}
