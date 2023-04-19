package com.sxu.basecomponent.manager

import android.os.Build
import android.os.DeadSystemException
import android.os.Handler
import android.os.Looper
import android.system.ErrnoException
import com.sxu.basecomponent.BuildConfig
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

/*******************************************************************************
 * Description: 过滤系统异常
 *
 * Author: Freeman
 *
 * Date: 2021/9/3
 *
 * Copyright: all rights reserved by Freeman.
 *******************************************************************************/
object CrashManager {

    /**
     * 过滤系统Crash
     */
    fun filterSystemCrash(uploadAction: ((e: Throwable) -> Unit)? = null) {
        if (BuildConfig.DEBUG) {
            return
        }

        // Release环境开启系统Crash过滤
        filterMainThreadException(uploadAction)
        filterChildThreadException(uploadAction)
    }

    /**
     * 过滤主线程的异常
     */
    private fun filterMainThreadException(uploadAction: ((e: Throwable) -> Unit)? = null) {
        Handler(Looper.getMainLooper()).post {
            while (true) {
                try {
                    Looper.loop()
                } catch (e: Exception) {
                    val exceptionInfo = e.toString()
                    if (exceptionInfo.contains("DeadSystemException") || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && e is DeadSystemException)) {
                        // 过滤RuntimeException("android.os.DeadSystemException")异常，APP将被杀死时启动Service会出现
                        return@post
                    } else if (e is IllegalArgumentException && exceptionInfo.contains("reportSizeConfigurations")) {
                        // 过滤IllegalArgumentException("reportSizeConfigurations: ActivityRecord not found for...")异常，
                        // APP被切到后台，Activity会回收，导致ActivityRecord找不到。
                        return@post
                    } else if (e is UnknownHostException
                        || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && e is ErrnoException)) {
                        // 过滤UndeliverableException、CompositeException、UnknownHostException、ErrnoException
                        return@post
                    }
                    // 上报Crash信息
                    uploadAction?.invoke(e)
                }
            }

        }
    }

    /**
     * 过滤子线程的异常
     */
    private fun filterChildThreadException(uploadAction: ((e: Throwable) -> Unit)? = null) {
        Thread.setDefaultUncaughtExceptionHandler { t, e ->
            if (e is TimeoutException) {
                // 过滤FinalizerWatchdogDaemon线程报TimeoutException异常
                return@setDefaultUncaughtExceptionHandler
            }

            // 上报Crash信息
            uploadAction?.invoke(e)
        }
    }
}