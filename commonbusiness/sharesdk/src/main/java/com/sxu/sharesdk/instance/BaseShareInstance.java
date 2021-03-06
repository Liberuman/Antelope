package com.sxu.sharesdk.instance;


import android.content.Intent;

import com.sxu.sharesdk.ShareHandler;

/*******************************************************************************
 * Description: 分享的接口
 *
 * Author: Freeman
 *
 * Date: 2018/07/12
 *
 * Copyright: all rights reserved by Freeman.
 *******************************************************************************/
public abstract class BaseShareInstance extends BaseInnerShareListener implements ShareHandler {

	/**
	 * 分享多媒体，包括文字，图片，视频，URL等
	 * @param title
	 * @param desc
	 * @param iconUrl
	 * @param url
	 */
	public abstract void onShare(String title, String desc, String iconUrl, String url);

	@Override
	public void handleResult(int requestCode, int resultCode, Intent intent) {

	}

	@Override
	public void shareSuccess() {

	}

	@Override
	public void shareFailure(Exception e) {

	}

	@Override
	public void shareCancel() {

	}
}
