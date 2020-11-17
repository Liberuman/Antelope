package com.sxu.basecomponent.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sxu.basecomponent.interfaces.IComponentProcess;
import com.sxu.basecomponent.interfaces.IContainerLayoutStyle;
import com.sxu.basecomponent.interfaces.impl.ContainerLayoutStyleImpl;
import com.sxu.basecomponent.uiwidget.ToolbarEx;

import org.greenrobot.eventbus.EventBus;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/*******************************************************************************
 * Description: 通用Fragment基类
 *
 * Author: Freeman
 *
 * Date: 2018/7/27
 *
 * Copyright: all rights reserved by Freeman.
 *******************************************************************************/

public abstract class BaseCommonFragment extends Fragment implements IComponentProcess, IContainerLayoutStyle, View.OnClickListener {

	protected ToolbarEx toolbar;
	protected View contentView;
	protected ViewGroup containerLayout;

	protected Context context;
	protected ContainerLayoutStyleImpl layoutStyle;

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		this.context = context;
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		layoutStyle = new ContainerLayoutStyleImpl(context);
		pretreatment();
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void pretreatment() {
		setToolbarStyle(TOOL_BAR_STYLE_NONE);
	}

	@Override
	public void setToolbarStyle(int style) {
		layoutStyle.setToolbarStyle(style);
	}

	@Override
	public boolean needEventBus() {
		return false;
	}

	@Override
	public void initContainerLayout() {
		layoutStyle.initContainerLayout();
		toolbar = layoutStyle.getToolbar();
		containerLayout = layoutStyle.getContainerLayout();
		if (needEventBus()) {
			EventBus.getDefault().register(this);
		}
	}

	@Override
	public void onClick(View v) {

	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		if (needEventBus()) {
			EventBus.getDefault().unregister(this);
		}
	}
}
