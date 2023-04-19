package com.sxu.basecomponent.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.sxu.basecomponent.interfaces.IComponentProcess;
import com.sxu.basecomponent.utils.PrivateScreenUtil;

/*******************************************************************************
 * Description: 对话框基类
 *
 * Author: Freeman
 *
 * Date: 2018/7/25
 *
 * Copyright: all rights reserved by Freeman.
 *******************************************************************************/

public abstract class BaseDialog extends DialogFragment implements IComponentProcess {

	/**
	 * 弹框的优先级
	 */
	private int priority = 0;

	/**
	 * 解决Activity在后台被销毁之后Dialog（通过点击弹出而非Activity的生命周期中直接弹出）内容不显示的问题
	 *
	 * Activity因配置发生变化重建之后Dialog可恢复，但是当Activity被系统杀掉后恢复时（可通过
	 * 开启开发者选项中的【不保存活动】开关来进行模拟），Dialog中的数据未被保存，导致Dialog只
	 * 有背景，没有内容。如果Dialog是设置为不可取消的，会出现页面无法操作的情况。
	 */
	private boolean enabled = false;

	/**
	 * 弹框样式是否已设置
	 */
	private boolean dialogStyleSet = false;

	/**
	 * 点击空白区域是否可关闭弹框
	 */
	private boolean canceledOnTouchOutside = true;

	/**
	 * Dialog的界面布局
	 */
	protected View contentView;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		enabled = !TextUtils.isEmpty(toString());
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		contentView = getContentView();
		if (contentView != null) {
			return contentView;
		}

		int layoutResId = getLayoutResId();
		if (layoutResId <= 0) {
			throw new IllegalArgumentException("Should return valid layout resource in getLayoutResId method");
		}
		contentView = inflater.inflate(getLayoutResId(), container, false);
		return contentView;
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		initViews();
		initListener();
		bindDataForView();
	}

	/**
	 * 初始化Dialog样式
	 */
	private void initDialogStyle() {
		Dialog dialog = getDialog();
		if (dialog == null || dialogStyleSet) {
			return;
		}

		dialog.setCanceledOnTouchOutside(getCanceledOnTouchOutside());
		Window window = dialog.getWindow();
		if (window == null) {
			return;
		}

		window.setBackgroundDrawable(null);
		WindowManager.LayoutParams windowParams = window.getAttributes();
		windowParams.width = getWidth();
		windowParams.height = getHeight();
		windowParams.gravity = getGravity();
		windowParams.windowAnimations = getWindowAnimation();
		window.setAttributes(windowParams);

//		View decorView = window.getDecorView();
//		// 清除默认的padding
//		decorView.setPadding(0, 0, 0, 0);
//		// 如果是沉浸式状态栏则背景全屏显示
//		if (isImmersionStatusBar()) {
//			decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
//		}
		dialogStyleSet = true;
	}

	@Override
	public void onResume() {
		super.onResume();
		initDialogStyle();
	}

	protected boolean isImmersionStatusBar() {
		return true;
	}

	@Override
	public View getContentView() {
		return null;
	}

	/**
	 * 设置弹框的宽度
	 * @return
	 */
	protected int getWidth() {
		return PrivateScreenUtil.getScreenWidth() - PrivateScreenUtil.dpToPx(72);
	}

	/**
	 * 设置弹框的高度
	 * @return
	 */
	protected int getHeight() {
		return WindowManager.LayoutParams.WRAP_CONTENT;
	}

	/**
	 * 设置弹框的位置
	 * @return
	 */
	protected int getGravity() {
		return Gravity.CENTER;
	}

	/**
	 * 设置Window的动画
	 * @return
	 */
	protected int getWindowAnimation() {
		return 0;
	}

	public BaseDialog setCanceledOnTouchOutside(boolean canceledOnTouchOutside) {
		this.canceledOnTouchOutside = canceledOnTouchOutside;
		return this;
	}

	/**
	 * 点击弹框以外的区域是否关闭弹框
	 * @return
	 */
	protected boolean getCanceledOnTouchOutside() {
		return canceledOnTouchOutside;
	}

	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		if (!enabled) {
			dismiss();
		}

		return super.onCreateDialog(savedInstanceState);
	}

	public void show(FragmentManager fm) {
		if (fm.isDestroyed() || isAdded()) {
			return;
		}

		/**
		 * 对于Activity中直接显示对话框的情况，如果Activity在切换到后台后被杀掉，重建Activity时，
		 * Activity会从保存的数据中恢复Fragment，同时也会执行创建新Fragment的逻辑，导致Dialog出
		 * 现多次。所以在Dialog显示时先将恢复的Dialog删掉。
		 */
		Fragment fragment = fm.findFragmentByTag(toString());
		if (fragment instanceof DialogFragment) {
			FragmentTransaction transaction = fm.beginTransaction();
			transaction.remove(fragment);
			transaction.commitAllowingStateLoss();
		}
		super.show(fm, toString());
	}

	public void show(Context context) {
		if (!(context instanceof FragmentActivity) || ((FragmentActivity) context).isFinishing()) {
			return;
		}

		show(((FragmentActivity) context).getSupportFragmentManager());
	}

	@Override
	public void dismiss() {
		if (!isAdded()) {
			return;
		}

		super.dismiss();
	}

	@Override
	public void dismissAllowingStateLoss() {
		if (!isAdded()) {
			return;
		}

		super.dismissAllowingStateLoss();
	}

	/**
	 * 设置弹框优先级
	 * @param priority
	 * @return
	 */
	public BaseDialog setPriority(int priority) {
		this.priority = priority;
		return this;
	}

	/**
	 * 获取弹框优先级
	 * @return
	 */
	public int getPriority() {
		return priority;
	}
}
