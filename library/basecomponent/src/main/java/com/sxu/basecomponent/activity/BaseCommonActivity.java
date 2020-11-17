package com.sxu.basecomponent.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.sxu.basecomponent.interfaces.IComponentProcess;
import com.sxu.basecomponent.interfaces.IContainerLayoutStyle;
import com.sxu.basecomponent.interfaces.impl.ContainerLayoutStyleImpl;
import com.sxu.basecomponent.uiwidget.ToolbarEx;
import com.sxu.basecomponent.utils.Event;
import com.sxu.basecomponent.utils.SafeHandler;
import com.sxu.baselibrary.commonutils.CollectionUtil;
import com.sxu.baselibrary.commonutils.InputMethodUtil;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

/*******************************************************************************
 * Description: Activity最基础的类, 为BaseActivity和BaseProgressActivity提供公共逻辑
 *
 * Author: Freeman
 *
 * Date: 2018/7/17
 *
 * Copyright: all rights reserved by Freeman.
 *******************************************************************************/

public abstract class BaseCommonActivity extends AppCompatActivity implements IComponentProcess, IContainerLayoutStyle, Handler.Callback, View.OnClickListener {

	protected ToolbarEx toolbar;
	protected ViewGroup containerLayout;

	protected Activity context;
	protected FragmentManager fragmentManager;
	protected ContainerLayoutStyleImpl layoutStyle;

	protected Handler commonHandler;

	/**
	 * 是否是沉浸式状态栏
	 */
	private boolean immersionStatus = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		commonHandler = new SafeHandler(this);
		fragmentManager = getSupportFragmentManager();
		// 设置沉浸式状态栏
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			immersionStatus = true;
			Window window = getWindow();
			window.getDecorView().setSystemUiVisibility(
				View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//				window.setStatusBarColor(Color.TRANSPARENT);
//			}
		}

		// 设置Activity的切换动画
//		overridePendingTransition(R.anim.anim_translate_right_in_300, R.anim.anim_translate_left_out_300);
		if (savedInstanceState != null) {
			restoreFragment(null);
			recreateActivity(savedInstanceState);
		}

		layoutStyle = new ContainerLayoutStyleImpl(this);
		pretreatment();
		initContainerLayout();
		if (containerLayout != null) {
			setContentView(containerLayout);
		}

		// 注册EventBus
		if (needEventBus()) {
			Event.register(this);
		}
	}

	@Override
	public void initContainerLayout() {
		layoutStyle.initContainerLayout();
		toolbar = layoutStyle.getToolbar();
		containerLayout = layoutStyle.getContainerLayout();
	}

	@Override
	public void pretreatment() {

	}

	/**
	 * 设置toolbar的样式
	 * @param style
	 */
	@Override
	public void setToolbarStyle(int style) {
		layoutStyle.setToolbarStyle(style);
	}

	@Override
	public boolean needEventBus() {
		return false;
	}

	protected void recreateActivity(Bundle savedInstanceState) {

	}

	@Override
	public void onClick(View v) {

	}

	public boolean isImmersionStatus() {
		return immersionStatus;
	}

	/**
	 * 当Activity重建时恢复Fragment
	 */
	public void restoreFragment(Fragment currentFragment) {
		FragmentManager fm = currentFragment == null
			? getSupportFragmentManager()
			: currentFragment.getChildFragmentManager();
		List<Fragment> fragmentList = fm != null ? fm.getFragments() : null;
		if (CollectionUtil.isEmpty(fragmentList)) {
			return;
		}

		for (Fragment fragment : fragmentList) {
			fragment.onAttach((Context)context);
			// 恢复嵌套的Fragment
			restoreFragment(fragment);
		}
	}

	@Override
	public boolean handleMessage(Message msg) {
		return false;
	}

	@Override
	protected void onDestroy() {
		commonHandler.removeCallbacksAndMessages(null);
		if (needEventBus()) {
			Event.unregister(this);
		}
		super.onDestroy();
	}

	@Override
	public void finish() {
		super.finish();
		InputMethodUtil.hideKeyboard(this);
		//overridePendingTransition(R.anim.anim_translate_left_in_300, R.anim.anim_translate_right_out_300);
	}
}
