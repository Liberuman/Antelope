package com.sxu.paysdk.wechatpay;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.sxu.paysdk.IPay;
import com.sxu.paysdk.IPayListener;
import com.sxu.paysdk.PayFailureException;
import com.tencent.mm.opensdk.constants.Build;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.List;

import androidx.annotation.NonNull;

/*******************************************************************************
 * 微信支付
 *
 * @author: Freeman
 *
 * @date: 2020/6/15
 *
 * Copyright: all rights reserved by Freeman.
 *******************************************************************************/
public class WeChatPay implements IPay {

	private PayRequestBean requestInfo;
	private static IPayListener payListener;

	private static IWXAPI wxApi;
	private PayReq payReq;

	public WeChatPay(Context context,  @NonNull PayRequestBean requestInfo, IPayListener listener) {
		this.requestInfo = requestInfo;
		payListener = listener;
		initWeChat(context);
	}

	private void initWeChat(Context context) {
		wxApi = WXAPIFactory.createWXAPI(context.getApplicationContext(), null);
		payReq = new PayReq();
		payReq.appId = requestInfo.appId;
		payReq.nonceStr = requestInfo.nonceStr;
		payReq.packageValue = requestInfo.packageValue;
		payReq.partnerId = requestInfo.partnerId;
		payReq.prepayId = requestInfo.prepayId;
		payReq.sign = requestInfo.sign;
		payReq.timeStamp = requestInfo.timeStamp;
		payReq.extData = requestInfo.extraValue;
		wxApi.registerApp(requestInfo.appId);
	}

	@Override
	public void startPay() {
		if (!wxApi.isWXAppInstalled() || wxApi.getWXAppSupportAPI() <= Build.PAY_SUPPORTED_SDK_INT) {
			if (payListener != null) {
				payListener.onFailure(new PayFailureException("微信未安装或安装的版本太低"));
			}
			return;
		}

		wxApi.sendReq(payReq);
	}

	/**
	 * 微信支付结果监听
	 * @param resp
	 */
	public static void onResp(BaseResp resp) {
		if (payListener == null) {
			return;
		}

		if (resp.errCode == BaseResp.ErrCode.ERR_OK) {
			payListener.onSuccess();
		} else {
			payListener.onFailure(new PayFailureException(resp.errStr));
		}
	}
}
