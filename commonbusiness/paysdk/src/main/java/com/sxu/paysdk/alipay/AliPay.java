package com.sxu.paysdk.alipay;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.alipay.sdk.app.PayTask;
import com.sxu.paysdk.IPay;
import com.sxu.paysdk.IPayListener;
import com.sxu.paysdk.PayFailureException;

import java.util.Map;

import androidx.annotation.NonNull;

/*******************************************************************************
 * 支付宝支付
 *
 * @author: Freeman
 *
 * @date: 2020/6/15
 *
 * Copyright: all rights reserved by Freeman.
 *******************************************************************************/
public class AliPay implements IPay {

	private Activity context;
	private String requestStr;
	private IPayListener payListener;

	private Handler handler;

	private final static int MSG_WHAT_ALI_PAY = 1001;
	private final static String PAY_RESULT_SUCCESS = "9000";

	public AliPay(@NonNull Activity context, final String requestStr, IPayListener listener) {
		this.context = context;
		this.requestStr = requestStr;
		this.payListener = listener;
		this.handler = new Handler(context.getMainLooper()) {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				handlePayMessage(msg);
			}
		};
	}

	@Override
	public void startPay() {
		ThreadPoolManager.executeTask(new Runnable() {
			@Override
			public void run() {
				PayTask aliPay = new PayTask(context);
				Map<String, String> result = aliPay.payV2(requestStr, true);
				Message msg = Message.obtain();
				msg.what = MSG_WHAT_ALI_PAY;
				msg.obj = result;
				handler.sendMessage(msg);
			}
		});
	}

	private void handlePayMessage(Message msg) {
		if (msg.what != MSG_WHAT_ALI_PAY || payListener == null || !(msg.obj instanceof Map)) {
			return;
		}

		PayResultBean payResultBean = new PayResultBean((Map<String, String>)msg.obj);
		String resultStatus = payResultBean.getResultStatus();
		if (TextUtils.equals(resultStatus, PAY_RESULT_SUCCESS)) {
			payListener.onSuccess();
		} else {
			payListener.onFailure(new PayFailureException(resultStatus));
		}
		handler.removeCallbacksAndMessages(null);
	}
}
