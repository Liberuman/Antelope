package com.sxu.basecomponent.uiwidget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.sxu.basecomponent.R;

/*******************************************************************************
 * Description: 可修改形状的ImageView
 *
 * Author: Freeman
 *
 * Date: 2018/7/11
 *
 * Copyright: all rights reserved by Freeman.
 *******************************************************************************/

public class ShapeImageView extends AppCompatImageView {

	private final int mBorderColor;
	private final int mBorderWidth;
	private final int mRadius;
	private final int mShape;
	private Paint bitmapPaint;
	private Paint borderPaint;

	private final int SHAPE_TYPE_CIRCLE = 1;
	private final int SHAPE_TYPE_ROUND = 2;

	public ShapeImageView(Context context) {
		this(context, null, 0);
	}

	public ShapeImageView(Context context, @Nullable AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ShapeImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleImage);
		mBorderColor = typedArray.getColor(R.styleable.ShapeImageView_borderColor, Color.TRANSPARENT);
		mBorderWidth = typedArray.getDimensionPixelSize(R.styleable.ShapeImageView_borderWidth, 0);
		mRadius = typedArray.getDimensionPixelSize(R.styleable.ShapeImageView_radius, 0);
		mShape = typedArray.getInt(R.styleable.ShapeImageView_shape, SHAPE_TYPE_ROUND);
		typedArray.recycle();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		Drawable drawable = getDrawable();
		if (drawable == null) {
			super.onDraw(canvas);
		}

		Bitmap bitmap = drawableToBitmap(drawable);
		if (bitmap == null) {
			return;
		}

		BitmapShader bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
		bitmapPaint.setShader(bitmapShader);
		RectF rect = new RectF(0, 0, getWidth(), getHeight());
		int radius = Math.min(getWidth(), getHeight()) / 2;
		if (mShape == SHAPE_TYPE_CIRCLE) {
			canvas.drawCircle(radius, radius, radius, bitmapPaint);
			canvas.drawCircle(radius, radius, radius + mBorderWidth/2, borderPaint);
		} else if (mShape == SHAPE_TYPE_ROUND) {
			canvas.drawRoundRect(rect, mRadius, mRadius, bitmapPaint);
		}
	}

	private void initPaint() {
		bitmapPaint = new Paint();
		bitmapPaint.setAntiAlias(true);

		borderPaint = new Paint();
		borderPaint.setAntiAlias(true);
		borderPaint.setColor(mBorderColor);
	}

	/**
	 * Convert drawable to bitmap.
	 * @param drawable
	 * @return
	 */
	private Bitmap drawableToBitmap(Drawable drawable) {
		Bitmap bitmap = null;
		if (drawable != null) {
			if (drawable instanceof BitmapDrawable) {
				bitmap = ((BitmapDrawable) drawable).getBitmap();
			} else {
				try {
					if (drawable instanceof ColorDrawable) {
						bitmap = Bitmap.createBitmap(50, 50, Bitmap.Config.RGB_565);
					} else {
						bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.RGB_565);
					}
					Canvas canvas = new Canvas(bitmap);
					drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
					drawable.draw(canvas);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		return bitmap;
	}

}
