package com.sxu.common.base.dialog;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.sxu.common.R;

/*******************************************************************************
 * Description: 加载对话框，用于执行异步任务时显示
 *
 * Author: Freeman
 *
 * Date: 2018/7/26
 *
 * Copyright: all rights reserved by Freeman.
 *******************************************************************************/

public class ProgressDialog extends DialogFragment {

	private static ProgressDialog dialog;

	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity(), R.style.ProgressDialogStyle);
		builder.setView(R.layout.dialog_progress_layout);
		Dialog dialog = builder.create();
		dialog.setCanceledOnTouchOutside(false);
		return dialog;
	}

	public void show(FragmentManager manager) {
		super.show(manager, getClass().getName());
	}

	public static void showDialog(FragmentManager fm) {
		if (dialog != null) {
			dialog.dismiss();
		}

		dialog = new ProgressDialog();
		dialog.show(fm);
	}

	public static void showDialog(FragmentActivity context) {
		showDialog(context.getSupportFragmentManager());
	}

	public static void closeDialog() {
		if (dialog == null) {
			return;
		}

		dialog.dismiss();
	}
}
