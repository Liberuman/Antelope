package com.sxu.basecomponent.uiwidget;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.sxu.basecomponent.R;
import com.sxu.baselibrary.commonutils.CollectionUtil;
import com.sxu.baselibrary.commonutils.DisplayUtil;

import java.util.List;

/*******************************************************************************
 * Description: 从底部弹出的对话框
 *
 * Author: Freeman
 *
 * Date: 2018/7/26
 *
 * Copyright: all rights reserved by Freeman.
 *******************************************************************************/

public class BottomDialog extends DialogFragment {

	private String title;
	private List<String> menuList;
	private OnItemClickListener listener;
	private View contentView;

	public BottomDialog setTitle(String title) {
		this.title = title;
		return this;
	}

	public BottomDialog setMenuList(List<String> menuList) {
		this.menuList = menuList;
		return this;
	}

	public BottomDialog setOnItemListener(OnItemClickListener listener) {
		this.listener = listener;
		return this;
	}

	public BottomDialog setContentView(View contentView) {
		this.contentView = contentView;
		return this;
	}

	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final BottomSheetDialog dialog = new BottomSheetDialog(requireActivity());
		if (contentView != null) {
			dialog.setContentView(contentView);
			return dialog;
		}

		dialog.setContentView(R.layout.dialog_bottom_layout);
		TextView titleText = dialog.findViewById(R.id.title_text);
		TextView cancelText = dialog.findViewById(R.id.cancel_text);
		LinearLayout menuLayout = dialog.findViewById(R.id.menu_layout);
		if (!TextUtils.isEmpty(title)) {
			titleText.setText(title);
		} else {
			titleText.setVisibility(View.GONE);
		}

		cancelText.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});

		if (!CollectionUtil.isEmpty(menuList)) {
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
					DisplayUtil.dpToPx(48));
			params.gravity = Gravity.CENTER;
			int textColor = ContextCompat.getColor(getActivity(), R.color.b1);
			for (final String menu : menuList) {
				TextView menuText = new TextView(getActivity());
				menuText.setText(menu);
				menuText.setTextSize(17);
				menuText.setTextColor(textColor);
				menuText.setGravity(Gravity.CENTER);
				menuText.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.common_button_bg));

				menuText.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if (listener != null) {
							listener.onItemClicked(menu, menuList.indexOf(menu));
						}
						dismiss();
					}
				});

				menuLayout.addView(menuText, params);
			}
		}

		return dialog;
	}

	public void show(FragmentManager fm) {
		super.show(fm, getClass().getName());
	}

	public void show(FragmentActivity context) {
		super.show(context.getSupportFragmentManager(), getClass().getName());
	}

	public interface OnItemClickListener {
		/**
		 * 对话框中每一项点击事件时被调用
		 * @param value
		 * @param position
		 */
		void onItemClicked(String value, int position);
	}
}
