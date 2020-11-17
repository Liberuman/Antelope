package com.sxu.sharesdk;

import android.app.Activity;

import com.sxu.sharesdk.instance.BaseShareInstance;
import com.sxu.sharesdk.instance.QQShareInstance;
import com.sxu.sharesdk.instance.WeChatShareInstance;
import com.sxu.sharesdk.instance.WeiBoShareInstance;

/*******************************************************************************
 * Description: 分享管理类
 *
 * Author: Freeman
 *
 * Date: 2018/07/12
 *
 * Copyright: all rights reserved by Freeman.
 *******************************************************************************/
public class ShareManager {

	private String title;
	private String desc;
	private String iconUrl;
	private String url;
	private Activity activity;

	private static ShareManager instance;
	private BaseShareInstance shareInstance = null;

	private ShareManager() {

	}

	public static ShareManager getInstance() {
		if (instance == null) {
			instance = new ShareManager();
		}

		return instance;
	}

	public void init(Activity activity, String title, String desc, String iconUrl, String url) {
		this.activity = activity;
		this.title = title;
		this.desc = desc;
		this.iconUrl = iconUrl;
		this.url = url;
	}

	public void share(int flowId, ShareListener listener) {
		switch (flowId) {
			case ShareConstants.SHARE_BY_WECHAT:
				shareInstance = new WeChatShareInstance(activity, flowId, listener);
				break;
			case ShareConstants.SHARE_BY_WECHAT_MOMENT:
				shareInstance = new WeChatShareInstance(activity, flowId, listener);
				break;
			case ShareConstants.SHARE_BY_MINI_PROGRAM:
				shareInstance = new WeChatShareInstance(activity, ShareConstants.SHARE_BY_MINI_PROGRAM, listener);
				break;
			case ShareConstants.SHARE_BY_WEIBO:
				shareInstance = new WeiBoShareInstance(activity, listener);
				break;
			case ShareConstants.SHARE_BY_QQ:
				shareInstance = new QQShareInstance(activity, ShareConstants.SHARE_BY_QQ, listener);
				break;
			case ShareConstants.SHARE_BY_QQ_ZONE:
				shareInstance = new QQShareInstance(activity, ShareConstants.SHARE_BY_QQ_ZONE, listener);
				break;
			default:
				break;
		}

		if (shareInstance != null) {
			shareInstance.onShare(title, desc, iconUrl, url);
		}
	}

	public BaseShareInstance getShareInstance() {
		return shareInstance;
	}
}
