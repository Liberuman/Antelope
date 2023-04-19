package com.sxu.basecomponent.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

/*******************************************************************************
 * Description: 可在任意页面显示的Dialog（PS：仅为容器，Dialog的样式需自实现）
 *
 * Author: Freeman
 *
 * Date: 2018/12/16
 *
 * Copyright: all rights reserved by Freeman.
 *******************************************************************************/
public class MagicDialog extends AppCompatActivity {

	private boolean hasShown = false;
	private static View contentView;

	private final static String EXTRA_KEY_CANCELABLE = "canceledOnTouchOutside";

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(0, 0);
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus && !hasShown) {
			hasShown = true;
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			if (contentView != null) {
				builder.setView(contentView);
			}
			builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
				@Override
				public void onDismiss(DialogInterface dialog) {
					finish();
				}
			}).show()
			.setCanceledOnTouchOutside(getIntent().getBooleanExtra(EXTRA_KEY_CANCELABLE, false));
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		contentView = null;
	}

	/**
	 * 显示弹框内容
	 * @param context
	 * @param dialogContentView
	 * @param canceledOnTouchOutside
	 */
	public static void show(Context context, View dialogContentView, boolean canceledOnTouchOutside) {
		contentView = dialogContentView;
		Intent intent = new Intent(context, MagicDialog.class);
		intent.putExtra(EXTRA_KEY_CANCELABLE, canceledOnTouchOutside);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(0, 0);
	}
}
