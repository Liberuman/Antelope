package com.sxu.paysdk.wechatpay;

import android.content.Intent;
import android.os.Bundle;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * 需在主项目的AndroidManifest.xml中引用此Activity。定义方式如下：
 *
 *  <activity-alias
 *             android:name=".wxapi.WXPayEntryActivity"
 *             android:configChanges="keyboardHidden|orientation|screenSize"
 *             android:exported="true"
 *             android:launchMode="singleTop"
 *             android:targetActivity="com.sxu.paysdk.wechatpay.WeChatPayCallbackActivity"/>
 */

/*******************************************************************************
 * 微信支付回调
 *
 * @author: Freeman
 *
 * @date: 2020/6/15
 *
 * Copyright: all rights reserved by Freeman.
 *******************************************************************************/
public class WeChatPayCallbackActivity extends AppCompatActivity implements IWXAPIEventHandler {

	private IWXAPI iwxapi;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		iwxapi = WXAPIFactory.createWXAPI(this, null);
		iwxapi.handleIntent(getIntent(), this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		iwxapi.handleIntent(getIntent(), this);
	}

	@Override
	public void onReq(BaseReq baseReq) {

	}

	@Override
	public void onResp(BaseResp baseResp) {
		WeChatPay.onResp(baseResp);
	}
}
