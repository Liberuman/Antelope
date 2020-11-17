package com.sxu.pushsdk;

import android.content.Context;
import android.util.Log;

import com.heytap.msp.push.HeytapPushManager;
import com.heytap.msp.push.callback.ICallBackResultService;
import com.huawei.hms.push.HmsMessaging;
import com.vivo.push.PushClient;
import com.vivo.push.ups.CodeResult;
import com.vivo.push.ups.TokenResult;
import com.vivo.push.ups.UPSRegisterCallback;
import com.vivo.push.ups.UPSTurnCallback;
import com.vivo.push.ups.VUpsManager;
import com.xiaomi.mipush.sdk.MiPushClient;

import androidx.annotation.NonNull;
import cn.jpush.android.api.JPushInterface;

/*******************************************************************************
 * Description: 推送管理器
 *
 * Author: Freeman
 *
 * Date: 2018/7/12
 *
 * Copyright: all rights reserved by Freeman.
 *******************************************************************************/

public class PushManager {

	/**
	 * 小米推送
	 */
	public final static int PUSH_TYPE_XIAOMI = 1;
	/**
	 * 华为推送
	 */
	public final static int PUSH_TYPE_HUAWEI = 2;
	/**
	 * OPPO推送
	 */
	public final static int PUSH_TYPE_OPPO = 3;
	/**
	 * VIVO推送
	 */
	public final static int PUSH_TYPE_VIVO = 4;
	/**
	 * 极光推送
	 */
	public final static int PUSH_TYPE_JPUSH = 10;

	private PushListener mListener;
	private boolean initialized = false;

	private PushManager() {

	}

	public static PushManager getInstance() {
		return SingletonHolder.INSTANCE;
	}

	/**
	 * 初始化推送 appId和appKey用于小米推送
	 * @param context
	 * @param appId
	 * @param appKey
	 */
	public void initPush(Context context, String appId, String appKey) {
		initPush(context, appId, appKey, null);
	}

	public void initPush(Context context, String appId, String appKey, PushListener listener) {
		if (initialized) {
			return;
		}

		initialized = true;
		if (RomUtil.isMiui()) {
			MiPushClient.registerPush(context.getApplicationContext(), appId, appKey);
		} else if (RomUtil.isEmui()) {
			initHWPush(context.getApplicationContext());
		} else if (RomUtil.isOppo()) {
			initOppoPush(context.getApplicationContext());
		} else if (RomUtil.isVivo()) {
			initVivo(context.getApplicationContext());
		} else {
			JPushInterface.init(context.getApplicationContext());
		}
		mListener = listener;
	}

	private void initHWPush(Context context) {
//		HuaweiApiClient client = new HuaweiApiClient.Builder(context.getApplicationContext())
//				.addApi(HuaweiPush.PUSH_API)
//				.addConnectionCallbacks(new HuaweiApiClient.ConnectionCallbacks() {
//					@Override
//					public void onConnected() {
//					}
//
//					@Override
//					public void onConnectionSuspended(int i) {
//
//					}
//				})
//				.addOnConnectionFailedListener(new HuaweiApiClient.OnConnectionFailedListener() {
//					@Override
//					public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//					}
//				})
//				.build();
//		client.connect();
//		HuaweiPush.HuaweiPushApi.enableReceiveNormalMsg(client, true);
	}

	private void initOppoPush(Context context) {
		HeytapPushManager.init(context, true);
		HeytapPushManager.register(context, "", "", new ICallBackResultService() {
			@Override
			public void onRegister(int i, String s) {
				if (i == 0) {
					Log.i("out", "注册成功 " + s);
				} else {
					Log.i("out", "注册失败 " + s);
				}
			}

			@Override
			public void onUnRegister(int i) {

			}

			@Override
			public void onSetPushTime(int i, String s) {

			}

			@Override
			public void onGetPushStatus(int i, int i1) {

			}

			@Override
			public void onGetNotificationStatus(int i, int i1) {

			}
		});

		HeytapPushManager.requestNotificationPermission();
	}

	private void initVivo(Context context) {
		PushClient.getInstance(context).initialize();
		VUpsManager.getInstance().turnOnPush(context, new UPSTurnCallback() {
			@Override
			public void onResult(CodeResult codeResult) {
				if(codeResult.getReturnCode()   == 0){
					Log.d("TAG", "初始化成功");
				}else {
					Log.d("TAG", "初始化失败");
				}
			}
		});
		VUpsManager.getInstance().registerToken(context, "", "", "",
			new UPSRegisterCallback() {
			@Override
			public void onResult(TokenResult tokenResult) {
				if(tokenResult.getReturnCode()   == 0){
					Log.d("TAG", "初始化成功" + tokenResult.getToken());
				}else {
					Log.d("TAG", "初始化失败");
				}
			}
		});
	}

	public void setPushListener(PushListener listener) {
		mListener = listener;
	}

	public PushListener getPushListener() {
		if (mListener == null) {
			throw new NullPointerException("mListener can't be null");
		}
		return mListener;
	}

	public static class SingletonHolder {
		private final static PushManager INSTANCE = new PushManager();
	}
}
