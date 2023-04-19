package com.sxu.basecomponent.application;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Process;

import androidx.annotation.CallSuper;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;
import androidx.multidex.MultiDex;

import java.util.List;

/*******************************************************************************
 * Description: Application基类
 *
 * Author: Freeman
 *
 * Date: 2018/8/20
 *
 * Copyright: all rights reserved by Freeman.
 *******************************************************************************/
public abstract class BaseApplication extends Application implements LifecycleObserver {

	public static Context context;

	@Override
	public void onCreate() {
		super.onCreate();
		context = this;
		//监控应用生命周期
		ProcessLifecycleOwner.get().getLifecycle().addObserver(this);

		// 如果不需延迟初始化，则直接初始化
		if (!delayInit()) {
			initApp();
		}
	}

	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		MultiDex.install(this);
	}

	/**
	 * 是否需要延迟初始化
	 * @return
	 */
	protected boolean delayInit() {
		return false;
	}

	/**
	 * 初始化APP
	 * 说明：如果需要延迟初始化，则在合适的时机调用此方法进行初始化
	 */
	public final void initApp() {
		if (isMainProcess()) {
			initMainProcess();
		} else {
			initChildProcess();
		}
	}

	/**
	 * 获取Module中实现了IApplication接口的类的路径
	 * @return
	 */
	protected List<String> getInitComponentList() {
		return null;
	}

	/**
	 * 在主进程中初始化
	 */
	@CallSuper
	protected void initMainProcess() {
		List<String> initComponentList = getInitComponentList();
		if (initComponentList == null || initComponentList.isEmpty()) {
			return;
		}

		try {
			// 初始化各Module中的组件
			for (String className: initComponentList) {
				Class component = Class.forName(className);
				Object target = component.newInstance();
				if (target instanceof IApplication) {
					((IApplication) target).init(this);
				}
			}
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
	}

	/**
	 * 在子进程中初始化
	 */
	protected void initChildProcess() {

	}

	@OnLifecycleEvent(Lifecycle.Event.ON_START)
	public void onForeground() {

	}

	@OnLifecycleEvent(Lifecycle.Event.ON_STOP)
	public void onBackground() {

	}

	/**
	 * 判断是否在主进程
	 */
	private boolean isMainProcess() {
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		if (am == null) {
			return false;
		}

		List<ActivityManager.RunningAppProcessInfo> appList = am.getRunningAppProcesses();
		if (appList == null || appList.isEmpty()) {
			return false;
		}

		int currentProcessId = Process.myPid();
		String packageName = getPackageName();
		for (ActivityManager.RunningAppProcessInfo appInfo: appList) {
			if (currentProcessId == appInfo.pid && packageName.equals(appInfo.processName)) {
				return true;
			}
		}

		return false;
	}
}
