package com.sxu.basecomponent.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.text.TextUtils;
import android.webkit.DownloadListener;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebHistoryItem;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.CallSuper;

import com.sxu.basecomponent.BuildConfig;


/*******************************************************************************
 * FileName: WebViewActivity
 *
 * Description: 通用的网页加载页面
 *
 * Author: Freeman
 *
 * Version: v1.0
 *
 * Date: 2018/1/24
 *
 * Copyright: all rights reserved by Freeman.
 *******************************************************************************/
public class BaseWebViewActivity extends BaseActivity {

	protected WebView webView;

	protected String title;
	protected String url;

	/**
	 * 是否是首次加载，为了解决onPageFinished在重定向时多次调用的问题
	 */
	private boolean isFirstLoad = true;

	@Override
	public int getLayoutResId() {
		return 0;
	}

	@Override
	public void initViews() {
		webView = new WebView(this);
		initWebView();
	}

	@Override
	public void initListener() {
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return super.shouldOverrideUrlLoading(view, url);
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				// 页面开始加载时调用，如显示加载圈
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				// 页面加载完成时调用，如隐藏加载圈
				// 由于goBack返回到上一页时不会触发onReceivedTitle方法，所以需要在此处重置标题
				if (toolbar != null && TextUtils.isEmpty(title) && TextUtils.isEmpty(view.getTitle())) {
					toolbar.setTitle(view.getTitle());
				}
				// 如果需要展示Loading，则页面加载完成后需要删除Loading
				if (needRequest() && isFirstLoad) {
					loadFinish();
				}
				isFirstLoad = false;
			}

			@Override
			public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
				super.onReceivedError(view, request, error);
				// 页面出错时调用，可在此时加载失败时的页面
			}

			@Override
			public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
				// 取消证书的校验
				handler.proceed();
			}
		});

		webView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onReceivedTitle(WebView view, String webTitle) {
				super.onReceivedTitle(view, webTitle);
				if (TextUtils.isEmpty(title) && !TextUtils.isEmpty(webTitle)) {
					toolbar.setTitle(webTitle);
				} else {
					toolbar.setTitle(title);
				}
			}

			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				super.onProgressChanged(view, newProgress);
				// 获取页面加载的进度
			}

			// Android 4.1-4.3 (API level 18)版本选择文件时会触发，该方法为隐藏方法
			@SuppressWarnings("unused")
			public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
				openFile(uploadMsg, null, false);
			}

			// Android 5.0以上版本会触发该方法，该方法为公开方法
			@SuppressWarnings("all")
			public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
				if (Build.VERSION.SDK_INT >= 21) {
					//是否支持多选
					final boolean allowMultipleChoose = fileChooserParams.getMode() == FileChooserParams.MODE_OPEN_MULTIPLE;
					openFile(null, filePathCallback, allowMultipleChoose);
					return true;
				} else {
					return false;
				}
			}
		});

		// 下载文件监听
		webView.setDownloadListener(new DownloadListener() {
			@Override
			public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
				downloadFile(url);
			}
		});
	}

	@Override
	public void bindDataForView() {
		Intent intent = getIntent();
		if (intent != null) {
			url = intent.getStringExtra("url");
			title = intent.getStringExtra("title");
		}

		loadUrl(url);
	}

	@Override
	public void requestData() {

	}

	protected void loadUrl(String url) {
		webView.loadUrl(url);
	}

	@CallSuper
	protected void initWebView() {
		// Debug状态下开启调试模式
		if (BuildConfig.DEBUG && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			WebView.setWebContentsDebuggingEnabled(true);
		}

		WebSettings settings = webView.getSettings();
		// 开启Https和Http的混合加载
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
		}
		// 支持JS
		settings.setJavaScriptEnabled(true);
		// 自适应屏幕
		settings.setUseWideViewPort(true);
		settings.setLoadWithOverviewMode(true);
		// 支持缩放
		settings.setSupportZoom(true);
		settings.setBuiltInZoomControls(true);
		// 隐藏缩放控件
		settings.setDisplayZoomControls(false);
		// 开启Dom存储功能
		settings.setDomStorageEnabled(true);
		// 启用缓存
		settings.setAppCacheEnabled(true);
		settings.setDatabaseEnabled(true);
		// 开启文件访问
		settings.setAllowFileAccess(true);
		settings.setLoadsImagesAutomatically(true);
		settings.setJavaScriptCanOpenWindowsAutomatically(true);

		// 设置缓存策略
		if (isConnected(this)) {
			settings.setCacheMode(WebSettings.LOAD_DEFAULT);
		} else {
			settings.setCacheMode(WebSettings.LOAD_CACHE_ONLY);
		}
	}

	/**
	 * 处理文件下载
	 * @param url
	 */
	protected void downloadFile(String url) {

	}

	/**
	 * 调用JS方法
	 * @param methodName JS定义的方法名
	 * @param paramsValue JS定义的方法参数，如果是对象则传JSON格式
	 */
	protected final void callJsMethod(String methodName, String paramsValue) {
		String methodSuffix = !TextUtils.isEmpty(paramsValue) ? "(" + paramsValue + ")" : "()";
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
			webView.loadUrl("javascript:" + methodName + methodSuffix);
		} else {
			webView.evaluateJavascript("javascript:" + methodName + methodSuffix, null);
		}
	}

	/** Android 5.0以下版本的文件选择回调 */
	protected ValueCallback<Uri> mFileUploadCallbackFirst;
	/** Android 5.0及以上版本的文件选择回调 */
	protected ValueCallback<Uri[]> mFileUploadCallbackSecond;

	protected static final int REQUEST_CODE_FILE_PICKER = 51426;

	protected String uploadFileType = "image/*";

	@SuppressLint("NewApi")
	private void openFile(final ValueCallback<Uri> fileUploadCallbackFirst, final ValueCallback<Uri[]> fileUploadCallbackSecond, final boolean allowMultiple) {
		//Android 5.0以下版本
		if (fileUploadCallbackFirst != null) {
			fileUploadCallbackFirst.onReceiveValue(null);
		}
		mFileUploadCallbackFirst = fileUploadCallbackFirst;

		//Android 5.0及以上版本
		if (fileUploadCallbackSecond != null) {
			fileUploadCallbackSecond.onReceiveValue(null);
		}
		mFileUploadCallbackSecond = fileUploadCallbackSecond;

		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		if (allowMultiple && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
			intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
		}
		intent.setType(uploadFileType);
		if (intent.resolveActivity(getPackageManager()) != null) {
			startActivityForResult(Intent.createChooser(intent, "选择文件"), 0);
		}
	}

	@Override
	public void onActivityResult(final int requestCode, final int resultCode, final Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if (requestCode == REQUEST_CODE_FILE_PICKER) {
			if (resultCode == Activity.RESULT_OK && intent != null) {
				//Android 5.0以下版本
				if (mFileUploadCallbackFirst != null) {
					mFileUploadCallbackFirst.onReceiveValue(intent.getData());
					mFileUploadCallbackFirst = null;
				} else if (mFileUploadCallbackSecond != null) {
					//Android 5.0及以上版本
					Uri[] dataUris = null;
					try {
						if (intent.getDataString() != null) {
							dataUris = new Uri[] { Uri.parse(intent.getDataString()) };
						} else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN && intent.getClipData() != null) {
							final int selectedFileCount = intent.getClipData().getItemCount();
							dataUris = new Uri[selectedFileCount];
							for (int i = 0; i < selectedFileCount; i++) {
								dataUris[i] = intent.getClipData().getItemAt(i).getUri();
							}
						}
					} catch (Exception ignored) {

					}
					mFileUploadCallbackSecond.onReceiveValue(dataUris);
					mFileUploadCallbackSecond = null;
				}
			}
			else {
				//这里mFileUploadCallbackFirst跟mFileUploadCallbackSecond在不同系统版本下分别持有了
				//WebView对象，在用户取消文件选择器的情况下，需给onReceiveValue传null返回值
				//否则WebView在未收到返回值的情况下，无法进行任何操作，文件选择器会失效
				if (mFileUploadCallbackFirst != null) {
					mFileUploadCallbackFirst.onReceiveValue(null);
					mFileUploadCallbackFirst = null;
				}
				else if (mFileUploadCallbackSecond != null) {
					mFileUploadCallbackSecond.onReceiveValue(null);
					mFileUploadCallbackSecond = null;
				}
			}
		}
	}

	public static void enter(Context context, String title, String url) {
		Intent intent  = new Intent(context, BaseWebViewActivity.class);
		intent.putExtra("title", title);
		intent.putExtra("url", url);
		context.startActivity(intent);
	}

	@Override
	protected void onResume() {
		super.onResume();
		webView.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		webView.onPause();
	}

	@Override
	public void onBackPressed() {
		// canGoBack状态可能不准确，所以使用页面地址的方式判断
		WebHistoryItem currentPage = webView.copyBackForwardList().getCurrentItem();
		String currentUrl = currentPage != null ? currentPage.getOriginalUrl() : "";
		if (url != null && !url.equals(currentUrl)) {
			webView.goBack();
		} else {
			super.onBackPressed();
		}
	}

	@Override
	protected void onDestroy() {
		webView.removeAllViews();
		webView.destroy();
		webView = null;
		super.onDestroy();
	}

	/**
	 * 网络是否已连接
	 * @param context
	 * @return
	 */
	private boolean isConnected(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm == null) {
			return false;
		}

		NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		return (networkInfo != null && networkInfo.isConnected()
				&& (networkInfo.getType() == ConnectivityManager.TYPE_WIFI
				|| networkInfo.getType() == ConnectivityManager.TYPE_MOBILE));
	}
}
