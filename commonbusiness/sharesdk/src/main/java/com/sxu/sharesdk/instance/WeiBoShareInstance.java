package com.sxu.sharesdk.instance;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.MultiImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.auth.AccessTokenKeeper;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WbAuthListener;
import com.sina.weibo.sdk.auth.WbConnectErrorMessage;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.share.WbShareHandler;
import com.sina.weibo.sdk.utils.LogUtil;
import com.sxu.sharesdk.R;
import com.sxu.sharesdk.ShareConstants;
import com.sxu.sharesdk.ShareListener;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/*******************************************************************************
 * Description: 微博分享的实现
 *
 * Author: Freeman
 *
 * Date: 2018/07/12
 *
 * Copyright: all rights reserved by Freeman.
 *******************************************************************************/
public class WeiBoShareInstance extends BaseShareInstance {

	private Oauth2AccessToken accessToken;
	private SsoHandler ssoHandler;
	private WbShareHandler shareHandler;
	private Activity activity;
	private ShareListener listener;

	public WeiBoShareInstance(Activity activity, ShareListener listener) {
		this.activity = activity;
		this.listener = listener;
		AuthInfo authInfo = new AuthInfo(activity, ShareConstants.APP_WEIBO_KEY, ShareConstants.REDIRECT_URL,
				ShareConstants.SCOPE);
 		WbSdk.install(activity, authInfo);

		shareHandler = new WbShareHandler(activity);
		shareHandler.registerApp();
		accessToken = AccessTokenKeeper.readAccessToken(activity);
	}

	@Override
	public void onShare(final String title, final String desc, final String iconUrl, final String url) {
		if (accessToken == null || !accessToken.isSessionValid()) {
			ssoHandler = new SsoHandler(activity);
			ssoHandler.authorize(new AuthListener(title, desc, iconUrl, url));
			return;
		}

		if (!TextUtils.isEmpty(iconUrl)) {
			Glide.with(activity).asBitmap().load(iconUrl).listener(new RequestListener<Bitmap>() {
				@Override
				public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
					shareMediaByWeibo(activity, desc, url,
						BitmapUtil.drawableToBitmap(ContextCompat.getDrawable(activity, R.mipmap.ic_launcher)));
					return false;
				}

				@Override
				public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
					shareMediaByWeibo(activity, desc, url, resource);
					return false;
				}
			}).submit();
		} else {
			shareMediaByWeibo(activity, desc, url,
					BitmapUtil.drawableToBitmap(ContextCompat.getDrawable(activity, R.mipmap.ic_launcher)));
		}
	}

	/**
	 * 普通分享，支持文字，链接，图片  注意：微博分享设置title无效，链接应追加在desc后面
	 * @param activity
	 * @param desc
	 * @param url
	 * @param bitmap
	 */
	private void shareMediaByWeibo(Activity activity, String desc, String url, Bitmap bitmap) {
		WeiboMultiMessage multiMessage = new WeiboMultiMessage();
		if (!TextUtils.isEmpty(desc)) {
			TextObject textObject = new TextObject();
			textObject.text = desc + " " + url;
			multiMessage.textObject = textObject;
		}
		if (bitmap != null && !bitmap.isRecycled()) {
			ImageObject imageObject = new ImageObject();
			imageObject.setImageObject(bitmap);
			multiMessage.imageObject = imageObject;
		}

		shareHandler.shareMessage(multiMessage, false);
	}

	/**
	 * 多图分享
	 * @param activity
	 * @param desc
	 * @param url
	 * @param imageList
	 */
	private void shareMediaByWeibo(Activity activity, String desc, String url, ArrayList<Uri> imageList) {
		WeiboMultiMessage multiMessage = new WeiboMultiMessage();
		if (!TextUtils.isEmpty(desc)) {
			TextObject textObject = new TextObject();
			textObject.text = desc + " " + url;
			multiMessage.textObject = textObject;
		}
		if (imageList != null && !imageList.isEmpty()) {
			MultiImageObject multiImageObj = new MultiImageObject();
			multiImageObj.setImageList(imageList);
			multiMessage.multiImageObject = multiImageObj;
		}

		shareHandler.shareMessage(multiMessage, false);
	}

	@Override
	public void handleResult(int requestCode, int resultCode, Intent intent) {
		if (ssoHandler != null) {
			ssoHandler.authorizeCallBack(requestCode, resultCode, intent);
		}
	}

	@Override
	public void shareSuccess() {
		if (listener != null) {
			listener.onShareSucceed();
		}
		activity.finish();
	}

	@Override
	public void shareFailure(Exception e) {
		if (listener != null) {
			listener.onShareFailed(e);
		}
		activity.finish();
	}

	@Override
	public void shareCancel() {
		if (listener != null) {
			listener.onShareFailed(new Exception("Share is canceled"));
		}
		activity.finish();
	}

	/***
	 * 授权监听
	 */
	class AuthListener implements WbAuthListener {

		private String desc;
		private String url;
		private String iconUrl;

		public AuthListener(String title, String desc, String url, String iconUrl) {
			this.desc = desc;
			this.url = url;
			this.iconUrl = iconUrl;
		}

		@Override
		public void cancel() {
			Log.i("Share", "微博授权取消");
			activity.finish();
		}

		@Override
		public void onSuccess(Oauth2AccessToken oauth2AccessToken) {
			accessToken = oauth2AccessToken;
			if (accessToken.isSessionValid()) {
				AccessTokenKeeper.writeAccessToken(activity.getApplicationContext(), accessToken);
				onShare(null, desc, url, iconUrl);
			} else {
				Log.i("Share", "微博开发账号签名错误");
				activity.finish();
			}
		}

		@Override
		public void onFailure(WbConnectErrorMessage wbConnectErrorMessage) {
			Log.i("Share", "微博授权失败" + wbConnectErrorMessage.getErrorCode());
			activity.finish();
		}
	}
}
