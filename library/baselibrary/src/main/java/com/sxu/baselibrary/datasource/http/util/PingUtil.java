package com.sxu.baselibrary.datasource.http.util;

import android.util.SparseArray;

import com.sxu.baselibrary.commonutils.ConvertUtil;
import com.sxu.baselibrary.commonutils.LogUtil;
import com.sxu.baselibrary.commonutils.ThreadPoolManager;

import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.StringBufferInputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import okhttp3.OkHttpClient;

/*******************************************************************************
 *
 *
 * @author: Freeman
 *
 * @date: 2020/5/18
 *
 * Copyright: all rights reserved by Freeman.
 *******************************************************************************/
public class PingUtil {

	private static int bestIpIndex = 0;
	private static float shortestTime = Byte.MAX_VALUE;

	/**
	 * 获取速度最快的IP地址
	 * @param ipList
	 * @return
	 */
	public static void getBestIP(final List<String> ipList, final OnGetBestIpListener listener) {
		for (final String ip : ipList) {
			ThreadPoolManager.executeTask(new Runnable() {
				@Override
				public void run() {
					int index = ipList.indexOf(ip);
					float roundTripTime = startPing(ip);
					if (roundTripTime < shortestTime) {
						shortestTime = roundTripTime;
						bestIpIndex = index;
					}
					if (index == ipList.size()-1) {
						listener.onGetBestIP(ipList.get(bestIpIndex), bestIpIndex);
					}
				}
			});
		}
	}

	private static float startPing(String ip) {
		try {
			Process process = Runtime.getRuntime().exec("ping -i 0.4 -c 5 " + ip);
			InputStreamReader isr = new InputStreamReader(process.getInputStream());
			LineNumberReader reader = new LineNumberReader(isr);
			String lineStr;
			String pingResult = null;
			while ((lineStr = reader.readLine()) != null) {
				pingResult = lineStr;
			}

			if (pingResult.contains("tt min/avg/max/mdev")) {
				String[] result = pingResult.split("=");
				LogUtil.i("result[1]==" + result[1]);
				return Float.valueOf(result[1].split("/")[1]);
			}
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}

		return Byte.MAX_VALUE;
	}

	/**
	 * 网络是否有效
	 * @return
	 */
	public static void networkIsValid(@NonNull final OnCheckNetworkListener listener) {
		ThreadPoolManager.executeTask(new Runnable() {
			@Override
			public void run() {
				try {
					Process process = Runtime.getRuntime().exec("ping -i 0.3 -c 3 www.baidu.com");
					InputStreamReader isr = new InputStreamReader(process.getInputStream());
					LineNumberReader reader = new LineNumberReader(isr);
					String lineStr;
					while ((lineStr = reader.readLine()) != null) {
						if (lineStr.contains(" 3 received")) {
							listener.onCheckNetwork(true);
							return;
						}
					}
				} catch (Exception e) {
					e.printStackTrace(System.out);
				}

				listener.onCheckNetwork(false);
			}
		});
	}

	/**
	 * 获取最快的IP地址
	 */
	public interface OnGetBestIpListener {
		void onGetBestIP(String bestIP, int index);
	}

	/**
	 * 检查网络是否连接
	 */
	public interface OnCheckNetworkListener {
		void onCheckNetwork(boolean isConnected);
	}
}
