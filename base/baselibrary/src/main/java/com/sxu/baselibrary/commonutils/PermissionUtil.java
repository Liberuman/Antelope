/*
* Copyright 2015 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.sxu.baselibrary.commonutils;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/*******************************************************************************
 * Description: 动态申请权限
 *
 * Author: Freeman
 *
 * Date: 2018/6/10
 *
 * Copyright: all rights reserved by Freeman.
 *******************************************************************************/
public final class PermissionUtil {

    private PermissionUtil() {
        throw new UnsupportedOperationException();
    }

    private final static int PERMISSION_REQUEST_CODE = 1000;
    private static String permissionDesc;
    private static String permissionSettingDesc;
    private static OnPermissionRequestListener requestListener;

    public static boolean checkPermission(Activity context, String permission) {
        if (ContextCompat.checkSelfPermission(context, permission)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context, new String[] {permission}, PERMISSION_REQUEST_CODE);
            return false;
        }

        return true;
    }

    /**
     * 检查所需权限是否已获取
     * @param context
     * @param permission
     */
    public static void checkPermission(Activity context, String[] permission) {
        if (permission == null || permission.length == 0) {
            return;
        }

        List<String> refusedPermission = new ArrayList<>();
        for (String item : permission) {
            if (ContextCompat.checkSelfPermission(context, item) != PackageManager.PERMISSION_GRANTED) {
                refusedPermission.add(item);
            }
        }
        int refusedPermissionSize = refusedPermission.size();
        if (refusedPermissionSize > 0) {
            ActivityCompat.requestPermissions(context, refusedPermission.toArray(new String[refusedPermissionSize]),
                    PERMISSION_REQUEST_CODE);
        } else {
            if (requestListener != null) {
                requestListener.onGranted();
                context.finish();
            }
        }
    }

    public static void requestCallback(final Activity context, int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode != PERMISSION_REQUEST_CODE) {
            return;
        }

        int refusedPermissionIndex = -1;
        for (int i = 0; i < permissions.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                refusedPermissionIndex = i;
                break;
            }
        }
        if (refusedPermissionIndex != -1) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(context, permissions[refusedPermissionIndex])) {
                ToastUtil.showShort(permissionDesc);
                if (requestListener != null) {
                    requestListener.onCanceled();
                }
                context.finish();
            } else {
                if (requestListener != null) {
                    requestListener.onCanceled();
                }
                showSettingPermissionDialog(context);
            }
        } else {
            if (requestListener != null) {
                requestListener.onGranted();
            }
            context.finish();
        }
    }

    private static void showSettingPermissionDialog(final Activity context) {
        new AlertDialog.Builder(context)
                .setMessage(permissionSettingDesc)
                .setNegativeButton("去设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.setData(Uri.fromParts("package", context.getPackageName(), null));
                        if (intent.resolveActivity(context.getPackageManager()) != null) {
                            context.startActivity(intent);
                        } else {
                            Toast.makeText(context, "无法打开应用详情，请手动开启权限~", Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        context.finish();
                    }
                })
                .show();
    }

    public static void setPermissionRequestListener(String desc, String settingDesc, OnPermissionRequestListener listener) {
        permissionDesc = desc;
        permissionSettingDesc = settingDesc;
        requestListener = listener;
    }

    public interface OnPermissionRequestListener {

        /**
         * 权限获得时被调用
         */
        void onGranted();

        /**
         * 权限被拒绝时调用
         */
        void onCanceled();

        /**
         * 权限被调用且勾选不再提示时调用
         */
        void onDenied();
    }
}