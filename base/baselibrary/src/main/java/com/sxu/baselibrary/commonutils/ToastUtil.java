package com.sxu.baselibrary.commonutils;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

/*******************************************************************************
 * Description: 自定义Toast
 *
 * Author: Freeman
 *
 * Date: 2017/06/20
 *
 * Copyright: all rights reserved by Freeman.
 *******************************************************************************/

public final class ToastUtil {

	private ToastUtil() {
		throw new UnsupportedOperationException();
	}

	/**
	 * 默认样式
	 */
	public final static int TOAST_STYLE_DEFAULT = 0;
	/**
	 * 成功样式
	 */
	public final static int TOAST_STYLE_CORRECT = 1;
	/**
	 * 警告样式
	 */
	public final static int TOAST_STYLE_WARNING = 2;
	/**
	 * 错误样式
	 */
	public final static int TOAST_STYLE_ERROR = 3;

	private static Toast toast;

	/**
	 * Toast的背景色
	 */
	private static final int DEFAULT_COLOR = Color.parseColor("#424242");
	private static final int CORRECT_COLOR = Color.parseColor("#4caf50");
	private static final int WARNING_COLOR = Color.parseColor("#ffc107");
	private static final int ERROR_COLOR = Color.parseColor("#f44336");

	/**
	 * 记录上次的显示样式，避免每次设置样式
	 */
	private static int lastStyle = TOAST_STYLE_DEFAULT;

	public static void showShort(String info) {
		show(BaseContentProvider.context, info, TOAST_STYLE_DEFAULT, Toast.LENGTH_SHORT);
	}

	public static void showShort(String info, int toastStyle) {
		show(BaseContentProvider.context, info, toastStyle, Toast.LENGTH_SHORT);
	}

	public static void showLong(String info) {
		show(BaseContentProvider.context, info, TOAST_STYLE_DEFAULT, Toast.LENGTH_LONG);
	}

	public static void showLong(String info, int toastStyle) {
		show(BaseContentProvider.context, info, toastStyle, Toast.LENGTH_LONG);
	}

	/**
	 * 注意：如果将activity theme中的fitsSystemWindows设置为true, toast会出现文件和背景错位的情况，
	 *  且自定义样式时设置setPadding无效
	 * @param context
	 * @param info
	 */
	private static void show(Context context, String info, int toastStyle, int duration) {
		if (context == null) {
			return;
		}

		if (toast == null) {
			toast = Toast.makeText(context, info, duration);
			TextView textView = new TextView(context);
			textView.setText(info);
			textView.setTextSize(16);
			textView.setTextColor(Color.WHITE);
			textView.setGravity(Gravity.CENTER);
			int verticalPadding = ScreenUtil.dpToPx(8);
			int horizontalPadding = ScreenUtil.dpToPx(12);
			textView.setPadding(horizontalPadding, verticalPadding, horizontalPadding, verticalPadding);
			ViewBgUtil.setShapeBg(textView, DEFAULT_COLOR, ScreenUtil.dpToPx(6));
			toast.setView(textView);
			toast.setGravity(Gravity.CENTER, 0, ScreenUtil.dpToPx(70));
		}

		if (lastStyle != toastStyle) {
			int backgroundColor = DEFAULT_COLOR;
			switch (toastStyle) {
				case TOAST_STYLE_CORRECT:
					backgroundColor = CORRECT_COLOR;
					break;
				case TOAST_STYLE_WARNING:
					backgroundColor = WARNING_COLOR;
					break;
				case TOAST_STYLE_ERROR:
					backgroundColor = ERROR_COLOR;
					break;
				default:
					break;
			}
			ViewBgUtil.setShapeBg(toast.getView(), backgroundColor, ScreenUtil.dpToPx(6));
		}
		((TextView)toast.getView()).setText(info);
		toast.show();
		lastStyle = toastStyle;
	}
}
