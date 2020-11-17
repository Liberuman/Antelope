package com.sxu.paysdk;

/*******************************************************************************
 * 支付失败异常
 *
 * @author: Freeman
 *
 * @date: 2020/6/15
 *
 * Copyright: all rights reserved by Freeman.
 *******************************************************************************/
public class PayFailureException extends Exception {

	public PayFailureException(String message) {
		super(message);
	}
}
