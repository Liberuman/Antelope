package com.sxu.basecomponent.interfaces.impl;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.sxu.basecomponent.interfaces.IComponentProcess;
import com.sxu.basecomponent.interfaces.IContainerLayoutStyle;

/*******************************************************************************
 * Description: 布局管理器，用于组装页面布局
 *
 * Author: Freeman
 *
 * Date: 2021/9/14
 *
 * Copyright: all rights reserved by Freeman.
 *******************************************************************************/
public class LayoutManager {

    /**
     * 页面的布局容器
     */
    private final IContainerLayoutStyle layoutStyle;

    /**
     * 页面的加载流程器
     */
    private final IComponentProcess componentProcess;

    public LayoutManager(@NonNull IComponentProcess componentProcess) {
        this(null, componentProcess);
    }

    public LayoutManager(IContainerLayoutStyle layoutStyle, @NonNull IComponentProcess componentProcess) {
        this.layoutStyle = layoutStyle;
        this.componentProcess = componentProcess;
    }

    /**
     * 获取用户设置的真实布局
     * @return
     */
    public static View getRealContentView(Context context, @NonNull IComponentProcess componentProcess) {
        View contentView = componentProcess.getContentView();
        if (contentView != null) {
            return contentView;
        }

        int layoutResId = componentProcess.getLayoutResId();
        if (layoutResId != 0) {
            return View.inflate(context,layoutResId, null);
        }

        return null;
    }

//    /**
//     * 更新内容布局
//     * @return
//     */
//    public void updateContentLayout(Context context) {
//        ViewGroup containerLayout = layoutStyle.getContainerLayout(context);
//        int toolbarStyle = layoutStyle.getToolbarStyle();
//        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        if (toolbarStyle == IContainerLayoutStyle.TOOL_BAR_STYLE_NORMAL) {
//            containerLayout.addView(getRealContentView(context), layoutParams);
//        } else if (toolbarStyle == IContainerLayoutStyle.TOOL_BAR_STYLE_TRANSPARENT) {
//            containerLayout.addView(getRealContentView(context), 0, layoutParams);
//        } else {
//            containerLayout.addView(getRealContentView(context), layoutParams);
//        }
//    }
}
