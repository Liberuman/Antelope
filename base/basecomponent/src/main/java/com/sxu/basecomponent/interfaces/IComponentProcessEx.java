package com.sxu.basecomponent.interfaces;

/*******************************************************************************
 * 页面的逻辑流程
 *
 * @author: Freeman
 *
 * @date: 2020/5/20
 *
 * Copyright: all rights reserved by Freeman.
 *******************************************************************************/
public interface IComponentProcessEx extends IComponentProcess {

	/**
	 * 预处理，加载布局之前添加相关逻辑，如设置window样式，重置切换动画等
	 */
	void pretreatment();
}
