package com.sxu.basecomponent.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/*******************************************************************************
 * Description: 不需要网络请求的Fragment
 *
 * Author: Freeman
 *
 * Date: 2018/7/27
 *
 * Copyright: all rights reserved by Freeman.
 *******************************************************************************/
public abstract class BaseFragment extends BaseCommonFragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        initContainerLayout();

        return contentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
        initComponent();
    }

    @Override
    public void initContainerLayout() {
        int layoutResId = getLayoutResId();
        if (layoutResId == 0) {
            return;
        }

        updateContentLayout(View.inflate(context, layoutResId, null));
    }

    @Override
    public void updateContentLayout(View contentLayout) {
        int toolbarStyle = layoutStyle.getToolbarStyle();
        if (toolbarStyle == TOOL_BAR_STYLE_NONE) {
            contentView = contentLayout;
        } else {
            super.initContainerLayout();
            if (toolbarStyle == TOOL_BAR_STYLE_NORMAL) {
                containerLayout.addView(contentLayout);
            } else {
                containerLayout.addView(contentLayout, 0);
            }
            contentView = containerLayout;
        }
    }
}
