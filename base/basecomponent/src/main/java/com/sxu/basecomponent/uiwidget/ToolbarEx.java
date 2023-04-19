package com.sxu.basecomponent.uiwidget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;

import com.sxu.basecomponent.R;
import com.sxu.basecomponent.utils.PrivateScreenUtil;

/*******************************************************************************
 * Description: 封装ToolBar控件，继承Toolbar是为了兼容Toolbar自带的Material Design样式
 *
 * Author: Freeman
 *
 * Date: 2018/7/13
 *
 * Copyright: all rights reserved by Freeman.
 *******************************************************************************/

public class ToolbarEx extends Toolbar {

	/**
	 * 返回键
	 */
	private ImageView returnIcon;

	/**
	 * 标题
	 */
	private TextView titleText;

	/**
	 * 左侧文案
	 */
	private TextView leftText;

	/**
	 * 右侧文案
	 */
	private TextView rightText;

	/**
	 * 右侧按钮
	 */
	private ImageView rightIcon;

	/**
	 * 右侧多个按钮
	 */
	private ImageView[] rightIcons;

	/**
	 * 包含右侧多个按钮的容器
	 */
	private LinearLayout rightIconLayout;

	/**
	 * 是否是沉浸式页面
	 */
	private boolean isImmersion = true;

	/**
	 * 亮色返回按钮
	 */
	private final int lightReturnIcon;

	/**
	 * 暗色返回按钮
	 */
	private final int darkReturnIcon;

	/**
	 * 亮色标题颜色
	 */
	private final int lightTitleColor;

	/**
	 * 暗色标题颜色
	 */
	private final int darkTitleColor;

	public ToolbarEx(Context context) {
		this(context, null);
	}

	public ToolbarEx(Context context, @Nullable AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ToolbarEx(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);

		int[] attrNames = new int[] {
				android.R.attr.height,
				android.R.attr.background,
				android.R.attr.elevation,
				R.attr.lightReturnIcon,
				R.attr.darkReturnIcon,
				R.attr.lightTitleColor,
				R.attr.darkTitleColor,
		};

		TypedArray typedArray = context.obtainStyledAttributes(R.style.ToolbarStyle, attrNames);
		int toolbarHeight = typedArray.getDimensionPixelOffset(0,
				getResources().getDimensionPixelOffset(R.dimen.toolbar_height));
		if (isImmersion) {
			int statusHeight = PrivateScreenUtil.getStatusHeight(context);
			setPadding(0, statusHeight, 0, 0);
			setMinimumHeight(toolbarHeight + statusHeight);
		} else {
			setMinimumHeight(toolbarHeight);
		}

		int backgroundColor = typedArray.getColor(1, Color.WHITE);
		float elevation = typedArray.getDimension(2, getResources().getDimension(R.dimen.toolbar_elevation));
		lightReturnIcon = typedArray.getResourceId(3, R.drawable.nav_back_white_icon);
		darkReturnIcon = typedArray.getResourceId(4, R.drawable.nav_back_grey_icon);
		lightTitleColor = typedArray.getColor(5, Color.WHITE);
		darkTitleColor = typedArray.getColor(6, Color.BLACK);
		typedArray.recycle();
		ViewCompat.setElevation(this, elevation);
		setBackgroundColor(backgroundColor);
	}

	/**
	 * 需要的属性：
	 * 默认背景、高度、深色&浅色返回按钮、深色&浅色的标题颜色、 大小、字体、位置，默认的阴影宽度
	 */

	/**
	 * 设置Toolbar的沉浸式状态
	 * @param isImmersion
	 */
	public ToolbarEx isImmersion(boolean isImmersion) {
		this.isImmersion = isImmersion;
		return this;
	}

	/**
	 * 设置返回键的图片资源
	 * @param resId
	 */
	public ToolbarEx setReturnIcon(@DrawableRes int resId) {
		initReturnIcon();
		returnIcon.setImageResource(resId);
		return this;
	}

	/**
	 * 获取返回键
	 * @return
	 */
	public View getReturnIcon() {
		initReturnIcon();
		return returnIcon;
	}

	/**
	 * 设置返回键的图片资源和点击事件
	 * @param resId
	 * @param listener
	 */
	public ToolbarEx setReturnIconAndEvent(@DrawableRes int resId, OnClickListener listener) {
		initReturnIcon();
		returnIcon.setImageResource(resId);
		returnIcon.setOnClickListener(listener);
		return this;
	}

	public ToolbarEx hideReturnIcon() {
		if (returnIcon != null) {
			returnIcon.setVisibility(GONE);
		}

		return this;
	}

	/**
	 * 设置返回键样式
	 */
	private void initReturnIcon() {
		if (returnIcon != null) {
			return;
		}

		returnIcon = new ImageView(getContext());
		int padding = PrivateScreenUtil.dpToPx(16);
		returnIcon.setPadding(padding, 0, padding, 0);
		Toolbar.LayoutParams params = generateDefaultLayoutParams();
		params.gravity = Gravity.LEFT|Gravity.CENTER_VERTICAL;
		addView(returnIcon, params);
		returnIcon.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Context context = getContext();
				if (context instanceof Activity) {
					((Activity) context).finish();
				}
			}
		});
	}

	private void setTextStyle(TextView textView, @StyleRes int styleId) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			textView.setTextAppearance(styleId);
		} else {
			textView.setTextAppearance(getContext(),styleId);
		}
	}

	/**
	 * 设置标题
	 * @param title
	 * @return
	 */
	public ToolbarEx setTitle(String title) {
		initTitleText(true);
		titleText.setText(title);
		return this;
	}

	/**
	 * 调用默认的setTitle
	 * @param title
	 * @return
	 */
	public ToolbarEx setDefaultTitle(String title) {
		super.setTitle(title);
		return this;
	}

	public ToolbarEx setTitleGravity(int gravity) {
		initTitleText(true);
		((LayoutParams) titleText.getLayoutParams()).gravity = gravity;
		return this;
	}

	/**
	 * 设置标题样式
	 * @param styleId
	 * @return
	 */
	public ToolbarEx setTitleTextAppearance(@StyleRes int styleId) {
		initTitleText(false);
		setTextStyle(titleText, styleId);
		return this;
	}

	/**
	 * 获取标题View
	 * @return
	 */
	public TextView getTitleText() {
		initTitleText(true);
		return titleText;
	}

	/**
	 * 初始化标题View
	 * @param isDefaultStyle
	 */
	private void initTitleText(boolean isDefaultStyle) {
		if (titleText != null) {
			return;
		}

		titleText = new TextView(getContext());
		titleText.setGravity(Gravity.CENTER);
		if (isDefaultStyle) {
			setTextStyle(titleText, R.style.NavigationTitleAppearance);
		}
		Toolbar.LayoutParams params = generateDefaultLayoutParams();
		params.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
		addView(titleText, params);
	}

	/**
	 * 设置左边TextView的文案
	 * @param text
	 * @return
	 */
	public ToolbarEx setLeftText(String text) {
		setNavigationIcon(null);
		initLeftText(true);
		leftText.setText(text);
		return this;
	}

	/**
	 * 设置左边TextView的文案和点击事件
	 * @param text
	 * @param listener
	 * @return
	 */
	public ToolbarEx setLeftTextAndEvent(String text, OnClickListener listener) {
		initLeftText(true);
		leftText.setText(text);
		leftText.setOnClickListener(listener);

		return this;
	}

	/**
	 * 设置左边TextView的文字样式
	 * @param styleId
	 * @return
	 */
	public ToolbarEx setLeftTextAppearance(@StyleRes int styleId) {
		initLeftText(false);
		setTextStyle(leftText, styleId);
		return this;
	}

	/**
	 * 获取左边的TextView
	 * @return
	 */
	public TextView getLeftText() {
		initRightText(true);
		return leftText;
	}

	/**
	 * 初始化左边TextView
	 * @param isDefaultStyle
	 */
	private void initLeftText(boolean isDefaultStyle) {
		if (leftText != null) {
			return;
		}

		int itemPadding = getResources().getDimensionPixelOffset(R.dimen.toolbar_padding);
		leftText = new TextView(getContext());
		leftText.setPadding(itemPadding, 0, itemPadding, 0);
		leftText.setGravity(Gravity.CENTER);
		if (isDefaultStyle) {
			setTextStyle(leftText, R.style.NavigationLeftTextAppearance);
		}
		Toolbar.LayoutParams params = generateDefaultLayoutParams();
		params.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
		addView(leftText, params);
	}

	/**
	 * 设置右边TextView的文案
	 * @param text
	 * @return
	 */
	public ToolbarEx setRightText(String text) {
		initRightText(true);
		rightText.setText(text);
		return this;
	}

	/**
	 * 设置右边View的文案和点击事件
	 * @param text
	 * @param listener
	 * @return
	 */
	public ToolbarEx setRightTextAndEvent(String text, OnClickListener listener) {
		initRightText(true);
		rightText.setText(text);
		rightText.setOnClickListener(listener);
		return this;
	}

	/**
	 * 设置右边TextVIew的文字样式
	 * @param styleId
	 * @return
	 */
	public ToolbarEx setRightTextAppearance(@StyleRes int styleId) {
		initRightText(false);
		setTextStyle(rightText, styleId);
		return this;
	}

	/**
	 * 获取右边的TextView
	 * @return
	 */
	public TextView getRightText() {
		initRightText(true);
		return rightText;
	}

	/**
	 * 初始化右边的TextView
	 * @param isDefaultStyle
	 */
	private void initRightText(boolean isDefaultStyle) {
		if (rightText != null) {
			return;
		}

		int itemPadding = getResources().getDimensionPixelOffset(R.dimen.toolbar_padding);
		rightText = new TextView(getContext());
		rightText.setPadding(itemPadding, 0, itemPadding, 0);
		rightText.setGravity(Gravity.CENTER);
		if (isDefaultStyle) {
			setTextStyle(rightText, R.style.NavigationRightTextAppearance);
		}
		Toolbar.LayoutParams params = generateDefaultLayoutParams();
		params.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
		addView(rightText, params);
	}

	public ToolbarEx setRightIcon(@DrawableRes int resId, OnClickListener listener) {
		initRightIcon(PrivateScreenUtil.dpToPx(24), 0);
		rightIcon.setImageResource(resId);
		rightIcon.setOnClickListener(listener);
		return this;
	}

	public ToolbarEx setRightIcon(@DrawableRes int resId, int horizontalPadding, OnClickListener listener) {
		initRightIcon(horizontalPadding, 0);
		rightIcon.setImageResource(resId);
		rightIcon.setOnClickListener(listener);
		return this;
	}

	/**
	 * 设置右边Icon的资源
	 * @param resId
	 * @param horizontalPadding
	 * @param marginRight 最右边Icon的右边距
	 * @param listener
	 * @return
	 */
	public ToolbarEx setRightIcon(@DrawableRes int resId, int horizontalPadding, int marginRight, OnClickListener listener) {
		initRightIcon(horizontalPadding, marginRight);
		rightIcon.setImageResource(resId);
		rightIcon.setOnClickListener(listener);
		return this;
	}

	public ImageView getRightIcon() {
		initRightIcon(PrivateScreenUtil.dpToPx(24), 0);
		return rightIcon;
	}

	/**
	 * 初始化右侧按钮样式
	 * @param horizontalPadding
	 * @param marginRight
	 */
	private void initRightIcon(int horizontalPadding, int marginRight) {
		if (rightIcon != null) {
			return;
		}

		Toolbar.LayoutParams params = generateDefaultLayoutParams();
		params.rightMargin = marginRight;
		params.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
		rightIcon = new ImageView(getContext());
		rightIcon.setPadding(horizontalPadding, 0, horizontalPadding, 0);
		addView(rightIcon, params);
	}

	public ToolbarEx setRightIcons(@DrawableRes int[] resIds, OnClickListener[] listeners) {
		return setRightIcons(resIds, PrivateScreenUtil.dpToPx(8), PrivateScreenUtil.dpToPx(12), listeners);
	}

	public ToolbarEx setRightIcons(@DrawableRes int[] resIds, int horizontalPadding, OnClickListener[] listeners) {
		return setRightIcons(resIds, horizontalPadding, PrivateScreenUtil.dpToPx(12), listeners);
	}

	/**
	 * 设置右边多个Icon资源
	 * @param resIds
	 * @param horizontalPadding 每个Icon的padding
	 * @param marginRight 最右边icon的右边距
	 * @param listeners
	 * @return
	 */
	public ToolbarEx setRightIcons(@DrawableRes int[] resIds, int horizontalPadding, int marginRight, OnClickListener[] listeners) {
		if (resIds == null || resIds.length == 0) {
			throw new IllegalArgumentException("resIds can't be null");
		}
		if (listeners == null || listeners.length == 0) {
			throw new IllegalArgumentException("listeners can't be null");
		}
		if (resIds.length != listeners.length) {
			throw new IllegalArgumentException("resId's length should equals listener's length");
		}

		if (rightIconLayout == null) {
			Toolbar.LayoutParams params = generateDefaultLayoutParams();
			params.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
			rightIconLayout = new LinearLayout(getContext());
			rightIconLayout.setPadding(0, 0, marginRight, 0);
			rightIconLayout.setOrientation(LinearLayout.HORIZONTAL);
			rightIcons = new ImageView[resIds.length];
			LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.MATCH_PARENT);
			for (int i = 0; i < resIds.length; i++) {
				rightIcons[i] = new ImageView(getContext());
				rightIcons[i].setPadding(horizontalPadding, 0, horizontalPadding, 0);
				rightIcons[i].setImageResource(resIds[i]);
				rightIcons[i].setOnClickListener(listeners[i]);
				rightIconLayout.addView(rightIcons[i], iconParams);
			}
			addView(rightIconLayout, params);
		}

		return this;
	}

	/**
	 * 获取指定位置的右侧按钮
	 * @param index
	 * @return
	 */
	public ImageView getRightIcon(int index) {
		if (rightIcons == null || index < 0 || index >= rightIcons.length) {
			return null;
		}

		return rightIcons[index];
	}

	/**
	 * 获取添加到右侧的所有按钮
	 * @return
	 */
	public ImageView[] getRightIcons() {
		return rightIcons;
	}

	/**
	 * 设置背景的透明度
	 * @param alpha
	 */
	public void setBackgroundAlpha(int alpha) {
		Drawable drawable = getBackground();
		drawable.setAlpha(alpha);
		setBackground(drawable);
	}

	@Override
	public void setBackgroundColor(int color) {
		super.setBackgroundColor(color);
		setChildViewStyle(isDarkColor(color));
	}

	@Override
	protected LayoutParams generateDefaultLayoutParams() {
		int toolbarHeight = getResources().getDimensionPixelOffset(R.dimen.toolbar_height);
		return new Toolbar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, toolbarHeight);
	}

	/**
	 * 根据背景设置返回按钮和标题的样式
	 * @param isDarkBackground 背景色是否为暗色调
	 */
	public void setChildViewStyle(boolean isDarkBackground) {
		if (returnIcon == null) {
			initReturnIcon();
		}
		if (titleText == null) {
			initTitleText(true);
		}

		if (isDarkBackground) {
			returnIcon.setImageResource(lightReturnIcon);
			titleText.setTextColor(lightTitleColor);
		} else {
			returnIcon.setImageResource(darkReturnIcon);
			titleText.setTextColor(darkTitleColor);
		}
	}

	/**
	 * 判断颜色值是否为暗色调
	 * @param color
	 * @return
	 */
	public boolean isDarkColor(int color) {
		// 颜色值的临界值
		double darknessThreshold = 0.5;
		double darkness = 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255;
		return darkness > darknessThreshold;
	}
}
