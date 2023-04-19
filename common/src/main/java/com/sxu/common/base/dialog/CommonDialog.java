package com.sxu.common.base.dialog;

import android.app.AlertDialog;
import android.graphics.Color;

import androidx.asynclayoutinflater.view.AsyncLayoutInflater;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewStub;
import android.widget.TextView;

import com.sxu.basecomponent.dialog.BaseDialog;
import com.sxu.basecomponent.interfaces.IViewInflateListener;
import com.sxu.baselibrary.commonutils.ScreenUtil;
import com.sxu.baselibrary.commonutils.ViewBgUtil;
import com.sxu.common.R;

/*******************************************************************************
 * Description: 通用的提示弹框
 *
 * Author: Freeman
 *
 * Date: 2018/7/16
 *
 * Copyright: all rights reserved by Freeman.
 *******************************************************************************/

public class CommonDialog extends BaseDialog {

    /**
     * 弹框标题
     */
    private String title;
    /**
     * 弹框内容
     */
    private String message;
    /**
     * 取消按钮的文案
     */
    private String cancelText = "取消";
    /**
     * 确认按钮的文案
     */
    private String okText = "确认";
    /**
     * 取消按钮的文字颜色
     */
    private int cancelTextColor = 0;
    /**
     * 确认按钮的文字颜色
     */
    private int okTextColor = 0;
    /**
     * 取消按钮的点击事件
     */
    private View.OnClickListener cancelListener;
    /**
     * 确认按钮的点击事件
     */
    private View.OnClickListener okListener;
    /**
     * View的加载监听，用于处理开放接口不能满足UI的情况：如设置文字大小，间距等
     */
    private IViewInflateListener viewInflateListener;

    public CommonDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    public CommonDialog setMessage(String message) {
        this.message = message;
        return this;
    }

    public CommonDialog setCancelTextColor(int textColor) {
        this.cancelTextColor = textColor;
        return this;
    }

    public CommonDialog setCancelText(String cancelText) {
        this.cancelText = cancelText;
        return this;
    }

    public CommonDialog setCancelClickListener(View.OnClickListener listener) {
        this.cancelListener = listener;
        return this;
    }

    public CommonDialog setCancelTextAndEvent(String cancelText, View.OnClickListener listener) {
        this.cancelText = cancelText;
        this.cancelListener = listener;
        return this;
    }

    public CommonDialog setOkTextColor(int textColor) {
        this.okTextColor = textColor;
        return this;
    }

    public CommonDialog setOkText(String okText) {
        this.okText = okText;
        return this;
    }

    public CommonDialog setOkClickListener(View.OnClickListener listener) {
        this.okListener = listener;
        return this;
    }

    public CommonDialog setOkTextAndEvent(String okText, View.OnClickListener listener) {
        this.okText = okText;
        this.okListener = listener;
        return this;
    }

    public CommonDialog setViewInflateListener(IViewInflateListener listener) {
        this.viewInflateListener = listener;
        return this;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.dialog_common_layout;
    }

    @Override
    public void initViews() {
        if (viewInflateListener != null) {
            viewInflateListener.onViewInflated(contentView);
        }

        TextView titleText = contentView.findViewById(R.id.title_text);
        TextView messageText = contentView.findViewById(R.id.message_text);
        View contentLayout = contentView.findViewById(R.id.content_layout);
        View horizontalLine = contentView.findViewById(R.id.horizontal_line);
        final TextView cancelText = contentView.findViewById(R.id.cancel_text);
        final TextView okText = contentView.findViewById(R.id.ok_text);
        View gapLine = contentView.findViewById(R.id.gap_line);
        View buttonLayout = contentView.findViewById(R.id.button_layout);
        titleText.setText(title);
        if (!TextUtils.isEmpty(message)) {
            messageText.setText(message);
        } else {
            messageText.setVisibility(View.GONE);
        }
        cancelText.setText(this.cancelText);
        if (cancelTextColor != 0) {
            cancelText.setTextColor(cancelTextColor);
        }
        okText.setText(this.okText);
        if (okTextColor != 0) {
            okText.setTextColor(okTextColor);
        }

        int state = android.R.attr.state_pressed;
        int radius = ScreenUtil.dpToPx(8);
        int[] bgColor = new int[]{Color.WHITE, ContextCompat.getColor(requireActivity(), R.color.g5)};
        // 根据设置的listener显示按钮，并设置相应的背景
        Drawable contentDrawable = ViewBgUtil.getDrawable(Color.WHITE, new float[] {radius, radius, radius, radius, 0f, 0f, 0f, 0f});
        if (!TextUtils.isEmpty(this.cancelText) && !TextUtils.isEmpty(this.okText)) {
            ViewBgUtil.setSelectorBg(cancelText, state, bgColor,
                    new float[]{0, 0, 0, 0, 0, 0, radius, radius});
            ViewBgUtil.setSelectorBg(okText, state, bgColor,
                    new float[]{0, 0, 0, 0, radius, radius, 0, 0});
            ViewCompat.setBackground(contentLayout, contentDrawable);
        } else if (!TextUtils.isEmpty(this.cancelText)) {
            gapLine.setVisibility(View.GONE);
            okText.setVisibility(View.GONE);
            ViewBgUtil.setSelectorBg(cancelText, state, bgColor,
                    new float[]{0, 0, 0, 0, radius, radius, radius, radius});
            ViewCompat.setBackground(contentLayout, contentDrawable);
        } else if (!TextUtils.isEmpty(this.okText)) {
            gapLine.setVisibility(View.GONE);
            cancelText.setVisibility(View.GONE);
            ViewBgUtil.setSelectorBg(okText, state, bgColor,
                    new float[]{0, 0, 0, 0, radius, radius, radius, radius});
            ViewCompat.setBackground(contentLayout, contentDrawable);
        } else {
            horizontalLine.setVisibility(View.INVISIBLE);
            buttonLayout.setVisibility(View.GONE);
            ViewBgUtil.setShapeBg(contentLayout, Color.WHITE, radius);
        }

        cancelText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cancelListener != null) {
                    cancelListener.onClick(null);
                }
                dismiss();
            }
        });
        okText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (okListener != null) {
                    okListener.onClick(null);
                }
            }
        });
    }

    @Override
    public void initListener() {

    }

    @Override
    public void bindDataForView() {

    }
}