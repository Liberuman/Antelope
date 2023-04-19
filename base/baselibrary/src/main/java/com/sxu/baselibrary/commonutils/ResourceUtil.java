package com.sxu.baselibrary.commonutils;

import android.graphics.drawable.Drawable;

import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.IntegerRes;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;

/*******************************************************************************
 * Description: 获取资源
 *
 * Author: Freeman
 *
 * Date: 2018/8/10
 *
 * Copyright: all rights reserved by Freeman.
 *******************************************************************************/
public final class ResourceUtil {

	private ResourceUtil() {
		throw new UnsupportedOperationException();
	}

	/**
	 * 获取Drawable资源
	 * @param resId
	 * @return
	 */
	public static Drawable getDrawable(@DrawableRes int resId) {
		return ContextCompat.getDrawable(BaseContentProvider.context, resId);
	}

	/**
	 * 获取Color资源
	 * @param colorId
	 * @return
	 */
	public static int getColor(@ColorRes int colorId) {
		return ContextCompat.getColor(BaseContentProvider.context, colorId);
	}

	/**
	 * 获取字符串
	 * @param resId
	 * @return
	 */
	public static String getString(@StringRes int resId) {
		return BaseContentProvider.context.getResources().getString(resId);
	}

	/**
	 * 获取整型值
	 * @param resId
	 * @return
	 */
	public static int getInteger(@IntegerRes int resId) {
		return BaseContentProvider.context.getResources().getInteger(resId);
	}

	/**
	 * 获取整型值
	 * @param resId
	 * @return
	 */
	public static int getDimen(@DimenRes int resId) {
		return BaseContentProvider.context.getResources().getDimensionPixelSize(resId);
	}
}
