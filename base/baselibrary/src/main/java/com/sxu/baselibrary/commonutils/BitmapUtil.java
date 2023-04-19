package com.sxu.baselibrary.commonutils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

/*******************************************************************************
 * Description: Bitmap相关的操作
 *
 * Author: Freeman
 *
 * Date: 2017/6/28
 *
 * Copyright: all rights reserved by Freeman.
 *******************************************************************************/

public final class BitmapUtil {

	private BitmapUtil() {
		throw new UnsupportedOperationException();
	}

	/**
	 * 将Drawable对象转换成Bitmap对象
	 * @param drawable
	 * @return
	 */
	public static Bitmap drawableToBitmap(Drawable drawable) {
		if (drawable == null || drawable instanceof ColorDrawable) {
			return null;
		}

		if (drawable instanceof BitmapDrawable) {
			return ((BitmapDrawable) drawable).getBitmap();
		}

		Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
			Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
		drawable.draw(canvas);
		return bitmap;
	}

	/**
	 * Bitmap对象转换成Drawable对象
	 * @param bitmap
	 * @return
	 */
	public static Drawable bitmapToDrawable(Bitmap bitmap) {
		if (bitmap == null || bitmap.isRecycled()) {
			return null;
		}

		return new BitmapDrawable(Resources.getSystem(), bitmap);
	}

	/**
	 * 缩放Bitmap
	 * @param sourceBitmap
	 * @param maxSize
	 * @return
	 */
	public static Bitmap zoomBitmap(Bitmap sourceBitmap, int maxSize) {
		if (sourceBitmap == null || sourceBitmap.isRecycled()) {
			return null;
		}

		int bitmapSize = sourceBitmap.getByteCount();
		if (bitmapSize > maxSize) {
			float scale = maxSize * 1.0f / bitmapSize;
			Matrix matrix = new Matrix();
			matrix.postScale(scale, scale);
			return Bitmap.createBitmap(sourceBitmap, 0, 0, sourceBitmap.getWidth(),
				sourceBitmap.getHeight(), matrix, true);
		} else {
			return sourceBitmap;
		}
	}

	/**
	 * 根据指定宽度和高度缩放Bitmap
	 * @param sourceBitmap
	 * @param newWidth
	 * @param newHeight
	 * @return
	 */
	public static Bitmap zoomBitmap(Bitmap sourceBitmap, float newWidth, float newHeight) {
		if (sourceBitmap == null || sourceBitmap.isRecycled()) {
			return null;
		}

		float width = sourceBitmap.getWidth();
		float height = sourceBitmap.getHeight();
		Matrix matrix = new Matrix();
		// 计算宽高缩放率
		float scaleWidth = newWidth / width;
		float scaleHeight = newHeight / height;
		// 对图片进行缩放
		matrix.postScale(scaleWidth, scaleHeight);
		return Bitmap.createBitmap(sourceBitmap, 0, 0, (int) width, (int) height, matrix, true);
	}

	/**
	 * 根据指定宽度和高度缩放Drawable
	 * @param drawable
	 * @param newWidth
	 * @param newHeight
	 * @return
	 */
	public static Drawable zoomDrawable(Drawable drawable, int newWidth, int newHeight) {
		Bitmap bitmap = drawableToBitmap(drawable);
		return bitmapToDrawable(zoomBitmap(bitmap, newWidth, newHeight));
	}

	/**
	 * 获取Bitmap对象的字节流
	 * @param bitmap
	 * @param needRecycle
	 * @return
	 */
	public static byte[] bitmapToByteArray(final Bitmap bitmap, final boolean needRecycle) {
		if (bitmap == null || bitmap.isRecycled()) {
			return null;
		}

		byte[] result;
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
		if (needRecycle) {
			bitmap.recycle();
		}
		result = output.toByteArray();
		try {
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 从存储卡中加载图片
	 * @param filePath
	 * @return
	 */
	public static Bitmap readFromStorage(@NonNull String filePath) {
		return BitmapFactory.decodeFile(filePath);
	}

	/**
	 * 从drawable资源文件夹中加载图片
	 * @param context
	 * @param drawableResId
	 * @return
	 */
	public static Bitmap readFromDrawable(Context context, @DrawableRes int drawableResId) {
		Drawable drawable = ContextCompat.getDrawable(context, drawableResId);
		return drawableToBitmap(drawable);
	}

	/**
	 * 从Asset文件夹中加载图片
	 * @param context
	 * @param imageName
	 * @return
	 */
	public static Bitmap readFromAsset(Context context, @NonNull String imageName) {
		try {
			InputStream inputStream = context.getAssets().open(imageName);
			if (inputStream != null) {
				return BitmapFactory.decodeStream(inputStream);
			}
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}

		return null;
	}
}
