package com.sxu.paysdk;

/*******************************************************************************
 * 支付结果监听
 *
 * @author: Freeman
 *
 * @date: 2020/6/15
 *
 * Copyright: all rights reserved by Freeman.
 *******************************************************************************/
public interface IPayListener {
	/**
	 * 支付成功
	 */
	void onSuccess();

	/**
	 * 支付失败
	 * @param e
	 */
	void onFailure(Exception e);
}
