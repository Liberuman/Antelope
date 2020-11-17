package com.sxu.basecomponent.uiwidget;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.sxu.basecomponent.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/*******************************************************************************
 * Description: 对话框基类
 *
 * Author: Freeman
 *
 * Date: 2018/7/25
 *
 * Copyright: all rights reserved by Freeman.
 *******************************************************************************/

public abstract class BaseDialog extends DialogFragment {

	/**
	 * 解决Activity在后台被销毁之后Dialog（通过点击弹出而Activity的生命周期中直接弹出）内容不显示的问题
	 *
	 * Activity因配置发生变化重建之后Dialog可恢复，但是当Activity被系统杀掉后恢复时（可通过
	 * 开启开发者选项中的【不保存活动】开关来进行模拟），Dialog中的数据未被保存，导致Dialog只
	 * 有背景，没有内容。如果Dialog是设置为不可取消的，会出现页面无法操作的情况。
	 */
	private boolean enabled = false;
	protected String title;
	protected String cancelText;
	protected String okText;
	protected DialogInterface.OnClickListener cancelListener;
	protected DialogInterface.OnClickListener okListener;

	public final static int DIALOG_STYLE_MATERIAL = 1;
	public final static int DIALOG_STYLE_CUSTOM = 2;

	protected int dialogStyle = DIALOG_STYLE_MATERIAL;

	public BaseDialog setTitle(String title) {
		this.title = title;
		return this;
	}

	public BaseDialog setDialogStyle(int dialogStyle) {
		this.dialogStyle = dialogStyle;
		return this;
	}

	public BaseDialog setCancelText(String cancelText) {
		this.cancelText = cancelText;
		return this;
	}

	public BaseDialog setOkText(String okText) {
		this.okText = okText;
		return this;
	}

	public BaseDialog setOkButtonClickListener(final DialogInterface.OnClickListener listener) {
		return setOkButtonClickListener("确定", listener);
	}

	public BaseDialog setOkButtonClickListener(String buttonText, final DialogInterface.OnClickListener listener) {
		this.okText = buttonText;
		this.okListener = listener;
		return this;
	}

	public BaseDialog setCancelButtonClickListener(final DialogInterface.OnClickListener listener) {
		return setCancelButtonClickListener("取消", listener);
	}

	public BaseDialog setCancelButtonClickListener(String buttonText, final DialogInterface.OnClickListener listener) {
		this.cancelText = buttonText;
		this.cancelListener = listener;
		return this;
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		enabled = !TextUtils.isEmpty(getParamsValue());
	}

	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder;
		if (!enabled) {
			dismiss();
		}

		if (dialogStyle == DIALOG_STYLE_MATERIAL) {
			builder = new AlertDialog.Builder(getContext());
			if (!TextUtils.isEmpty(title)) {
				builder.setTitle(title);
			}
			if (!TextUtils.isEmpty(cancelText)) {
				builder.setNegativeButton(cancelText, cancelListener);
			}
			if (!TextUtils.isEmpty(okText)) {
				builder.setPositiveButton(okText, okListener);
			}
			initMaterialDialog(builder);
		} else {
			builder = new AlertDialog.Builder(getContext(), R.style.CommonDialog);
			builder.setView(createCustomDialogView());
		}

		return builder.create();
	}

	/**
	 * 初始化Material Design风格的对话框
	 * @param builder
	 */
	protected abstract void initMaterialDialog(AlertDialog.Builder builder);

	/**
	 * 初始化自定义的对话框
	 * @return
	 */
	protected abstract View createCustomDialogView();

	public void show(FragmentManager fm) {
		if (fm.isDestroyed() || isAdded()) {
			return;
		}

		/**
		 * 对于Activity中直接显示对话框的情况，如果Activity在切换到后台后被杀掉，重建Activity时，
		 * Activity会从保存的数据中恢复Fragment，同时也会执行创建新Fragment的逻辑，导致Dialog出
		 * 现多次。所以在Dialog显示时先将恢复的Dialog删掉。
		 */
		String tag = getParamsValue();
		Fragment fragment = fm.findFragmentByTag(tag);
		if (fragment != null && fragment instanceof DialogFragment) {
			FragmentTransaction transaction = fm.beginTransaction();
			transaction.remove(fragment);
			transaction.commitAllowingStateLoss();
		}
		super.show(fm, tag);
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
	 * 以对话框的参数作为Dialog的Tag
	 * @return
	 */
	private String getParamsValue() {
		StringBuilder builder = new StringBuilder();
		if (!TextUtils.isEmpty(title)) {
			builder.append(title);
		}
		if (!TextUtils.isEmpty(okText)) {
			builder.append(okText);
		}
		if (!TextUtils.isEmpty(cancelText)) {
			builder.append(cancelText);
		}
		if (dialogStyle == DIALOG_STYLE_CUSTOM) {
			builder.append(dialogStyle);
		}

		return builder.toString();
	}
}
