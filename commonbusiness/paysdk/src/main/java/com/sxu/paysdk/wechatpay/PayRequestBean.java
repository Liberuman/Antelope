package com.sxu.paysdk.wechatpay;

/*******************************************************************************
 * Description: 微信支付数据对象
 *
 * Author: Freeman
 *
 * Date: 2018/07/10
 *
 * Copyright: all rights reserved by Freeman.
 *******************************************************************************/
public class PayRequestBean {
	public String appId;
	public String partnerId;
	public String prepayId;
	public String nonceStr;
	public String timeStamp;
	public String packageValue;
	public String sign;
	public String extraValue;

	public PayRequestBean() {

	}

	public PayRequestBean(String appId, String partnerId, String prepayId, String nonceStr, String timeStamp, String packageValue, String sign) {
		this(appId, partnerId, prepayId, nonceStr, timeStamp, packageValue, sign, null);
	}

	public PayRequestBean(String appId, String partnerId, String prepayId, String nonceStr, String timeStamp,
	                      String packageValue, String sign, String extraValue) {
		this.appId = appId;
		this.partnerId = partnerId;
		this.prepayId = prepayId;
		this.nonceStr = nonceStr;
		this.timeStamp = timeStamp;
		this.packageValue = packageValue;
		this.sign = sign;
		this.extraValue = extraValue;
	}

	public PayRequestBean setAppId(String appId) {
		this.appId = appId;
		return this;
	}

	public PayRequestBean setPartnerId(String partnerId) {
		this.partnerId = partnerId;
		return this;
	}

	public PayRequestBean setPrepayId(String prepayId) {
		this.prepayId = prepayId;
		return this;
	}

	public PayRequestBean setNonceStr(String nonceStr) {
		this.nonceStr = nonceStr;
		return this;
	}

	public PayRequestBean setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
		return this;
	}

	public PayRequestBean setPackageValue(String packageValue) {
		this.packageValue = packageValue;
		return this;
	}

	public PayRequestBean setSign(String sign) {
		this.sign = sign;
		return this;
	}

	public PayRequestBean setExtraValue(String extraValue) {
		this.extraValue = extraValue;
		return this;
	}
}
