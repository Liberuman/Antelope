package com.sxu.pushsdk.oppo;

import android.content.Context;

import com.heytap.msp.push.HeytapPushManager;
import com.heytap.msp.push.mode.DataMessage;
import com.heytap.msp.push.service.CompatibleDataMessageCallbackService;

/*******************************************************************************
 *
 *
 * @author: Freeman
 *
 * @date: 2020/6/18
 *
 * Copyright: all rights reserved by Freeman.
 *******************************************************************************/
public class OppoPushService extends CompatibleDataMessageCallbackService {

	@Override
	public void processMessage(Context context, DataMessage dataMessage) {
		super.processMessage(context, dataMessage);
	}
}
