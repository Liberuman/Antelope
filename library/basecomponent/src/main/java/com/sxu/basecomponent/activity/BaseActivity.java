package com.sxu.basecomponent.activity;

import android.os.Bundle;
import android.view.View;

/*******************************************************************************
 * Description: 启动时不进行网络操作的Activity的基类
 *
 * Author: Freeman
 *
 * Date: 2018/7/13
 *
 * Copyright: all rights reserved by Freeman.
 *******************************************************************************/
public abstract class BaseActivity extends BaseCommonActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
        initComponent();
    }

    @Override
    public void initContainerLayout() {
        int layoutResId = getLayoutResId();
        if (layoutResId == 0) {
            return;
        }

        updateContentLayout(View.inflate(this, layoutResId, null));
    }

    @Override
    public void updateContentLayout(View contentLayout) {
        int toolbarStyle = layoutStyle.getToolbarStyle();
        if (toolbarStyle == TOOL_BAR_STYLE_NONE) {
            setContentView(contentLayout);
        } else {
            super.initContainerLayout();
            if (toolbarStyle == TOOL_BAR_STYLE_NORMAL) {
                containerLayout.addView(contentLayout);
            } else {
                containerLayout.addView(contentLayout, 0);
            }
        }
    }
}
