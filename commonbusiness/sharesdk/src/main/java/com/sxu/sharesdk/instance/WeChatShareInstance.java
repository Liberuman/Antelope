package com.sxu.sharesdk.instance;

import android.app.Activity;
import android.graphics.Bitmap;
import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.sxu.sharesdk.R;
import com.sxu.sharesdk.ShareConstants;
import com.sxu.sharesdk.ShareListener;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXMiniProgramObject;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/*******************************************************************************
 * Description: 微信分享的实现
 *
 * Author: Freeman
 *
 * Date: 2018/07/12
 *
 * Copyright: all rights reserved by Freeman.
 *******************************************************************************/
public class WeChatShareInstance extends BaseShareInstance {

	private int flowId;
	private Activity activity;
	private IWXAPI wxApi;

	private static ShareListener shareListener;

	public WeChatShareInstance(Activity activity, int flowId, ShareListener listener) {
		this.flowId = flowId;
		this.activity = activity;
		shareListener = listener;
		wxApi = WXAPIFactory.createWXAPI(activity, ShareConstants.APP_WECHAT_KEY, true);
		wxApi.registerApp(ShareConstants.APP_WECHAT_KEY);
	}

	@Override
	public void onShare(final String title, final String desc, final String iconUrl, final String url) {
		if (!wxApi.isWXAppInstalled()) {
			if (shareListener != null) {
				shareListener.onShareFailed(new Exception("Please install Wechat APP~"));
			}
			return;
		}

		if (!TextUtils.isEmpty(iconUrl)) {
			Glide.with(activity).asBitmap().load(iconUrl).listener(new RequestListener<Bitmap>() {
				@Override
				public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
					startShare(title, desc, url,
						BitmapUtil.drawableToBitmap(ContextCompat.getDrawable(activity, R.mipmap.ic_launcher)));
					return false;
				}

				@Override
				public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
					startShare(title, desc, url, resource);
					return false;
				}
			}).submit();
		} else {
			startShare(title, desc, url,
					BitmapUtil.drawableToBitmap(ContextCompat.getDrawable(activity, R.mipmap.ic_launcher)));
		}
	}

	private void startShare(String title, String desc, String url, Bitmap bitmap) {
		if (flowId == ShareConstants.SHARE_BY_MINI_PROGRAM) {
			shareByMiniProgram(title, desc, url, bitmap);
		} else {
			shareByWeChat(title, desc, url, bitmap);
		}
	}

	private void shareByWeChat(String title, String desc, String url, Bitmap bitmap) {
		String transaction;
		WXMediaMessage msg;
		if (!TextUtils.isEmpty(url)) {
			transaction = "webpage";
			WXWebpageObject webpage = new WXWebpageObject();
			webpage.webpageUrl = url;
			msg = new WXMediaMessage(webpage);
		} else {
			msg = new WXMediaMessage();
			if (bitmap != null && !bitmap.isRecycled()) {
				transaction = "image";
				msg.mediaObject = new WXImageObject(bitmap);
			} else {
				transaction = "text";
				msg.mediaObject = new WXTextObject(title);
			}
		}

		msg.title = title;
		msg.description = desc;
		msg.thumbData = BitmapUtil.bitmapToByteArray(bitmap, true);

		SendMessageToWX.Req request = new SendMessageToWX.Req();
		request.transaction = buildTransaction(transaction);
		request.message = msg;
		request.scene = (flowId == ShareConstants.SHARE_BY_WECHAT_MOMENT) ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
		wxApi.sendReq(request);
		activity.finish();
	}

	/**
	 * 小程序分享
	 *
	 * @param title
	 * @param desc
	 * @param url
	 * @param bitmap
	 *
	 * 注意：
	 * 小程序分享分为两种情况：
	 * 1. 小程序页面：url为小程序页面路径；
	 * 2. 网页：在小程序中打开可能还需要在URL前面添加路径；
	 */
	private void shareByMiniProgram(String title, String desc, String url, Bitmap bitmap) {
		WXMiniProgramObject object = new WXMiniProgramObject();
		object.webpageUrl = ShareConstants.APP_MINI_PROGRAM_WEB_PAGE;
		object.userName = ShareConstants.APP_MINI_PROGRAM_NAME;
		StringBuilder builder = new StringBuilder();
		object.path = url;
		WXMediaMessage msg = new WXMediaMessage(object);
		msg.title = title;
		msg.description = desc;
		msg.thumbData = BitmapUtil.bitmapToByteArray(bitmap, true);
		SendMessageToWX.Req request = new SendMessageToWX.Req();
		request.transaction = buildTransaction("webpage");
		request.message = msg;
		request.scene = SendMessageToWX.Req.WXSceneSession;
		wxApi.sendReq(request);
		activity.finish();
	}

	private String buildTransaction(final String type) {
		return TextUtils.isEmpty(type) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
	}

	public static void onResp(Activity activity, BaseResp resp) {
		if (shareListener != null) {
			// 微信新版本已不能根据errCode来判断是否分享成功了，取消分享时也返回ERR_OK
			if (resp.errCode == BaseResp.ErrCode.ERR_OK) {
				shareListener.onShareSucceed();
			} else {
				shareListener.onShareFailed(new Exception(resp.errStr));
			}
		}

		activity.finish();
	}
}

