package com.sxu.loginsdk.instance;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.sxu.loginsdk.LoginConstants;
import com.sxu.loginsdk.listener.AuthListener;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/*******************************************************************************
 * Description: 微信登录
 *
 * Author: Freeman
 *
 * Date: 2018/9/1
 *******************************************************************************/
public class WXLoginInstance extends BaseLoginInstance {

	private IWXAPI wxApi;
	private Context context;
	private static AuthListener authListener;

	public WXLoginInstance(Context context, AuthListener listener) {
		this(context, LoginConstants.APP_WECHAT_KEY, listener);
	}

	private WXLoginInstance(Context context, String appKey, AuthListener listener) {
		this.context = context;
		authListener = listener;
		wxApi = WXAPIFactory.createWXAPI(context, appKey);
		wxApi.registerApp(appKey);
	}

	@Override
	public void onLogin() {
		if (wxApi.isWXAppInstalled()) {
			SendAuth.Req request = new SendAuth.Req();
			request.scope = "snsapi_userinfo";
			request.state = context.getPackageName();
			wxApi.sendReq(request);
		} else {
			Toast.makeText(context, "请先安装微信~", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 微信登录的回调处理逻辑
	 * @param activity WXEntryActivity实例
	 * @param resp
	 */
	public static void onResp(Activity activity, BaseResp resp) {
		if (authListener == null) {
			return;
		}

		if (resp.errCode == BaseResp.ErrCode.ERR_OK) {
			getToken(activity, ((SendAuth.Resp)resp).code);
		} else {
			authListener.onAuthFailed(new Exception(resp.errStr));
			activity.finish();
		}
	}

	private static void getToken(final Activity activity, String code) {
		OkHttpClient httpClient = new OkHttpClient();
		String url = "https://api.weixin.qq.com/sns/oauth2/access_token"
				+ "?appid=" + LoginConstants.APP_WECHAT_KEY
				+ "&secret=" + LoginConstants.APP_WECHAT_SECRET
				+ "&code=" + code
				+ "&grant_type=authorization_code";
		Request request = new Request.Builder().url(url).build();
		httpClient.newCall(request).enqueue(new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {
				authListener.onAuthFailed(e);
				activity.finish();
			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				if (response.isSuccessful()) {
					try {
						JSONObject result = new JSONObject(response.body().string());
						authListener.onAuthSucceed(result.getString("openid"));
					} catch (Exception e) {
						authListener.onAuthFailed(e);
					}
				} else {
					authListener.onAuthFailed(new Exception("response is failed"));
				}

				activity.finish();
			}
		});
	}
}