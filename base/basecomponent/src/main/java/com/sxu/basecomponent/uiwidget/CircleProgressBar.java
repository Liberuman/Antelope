package com.sxu.basecomponent.uiwidget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.sxu.basecomponent.R;

/*******************************************************************************
 * Description: 圆形进度条
 *
 * Author: Freeman
 *
 * Date: 2018/7/10
 *
 * Copyright: all rights reserved by Freeman.
 *******************************************************************************/
public class CircleProgressBar extends View {

    private int mOutBorderColor;
    private int mInBorderColor;
    private int mInCircleColor;
    private int mProgressColor;
    private int mProgressDefaultColor;
    private int mOutBorderWidth;
    private int mInBorderWidth;
    private int mProgressWidth;
    private String mDescText;
    private int mDescTextSize;
    private int mDescTextColor;
    private String mProgressText;
    private int mProgressTextSize;
    private int mProgressTextColor;
    private int mProgressTextStyle;
    private int mLineGap;


    private Paint innerCirclePaint;
    private Paint inBorderPaint;
    private Paint progressPaint;
    private Paint outBorderPaint;
    private Paint textPaint;

    private int progress;

    /**
     * 进度的最大值
     */
    private final int PROGRESS_MAX_VALUE = 100;

    public CircleProgressBar(Context context) {
        super(context);
    }

    public CircleProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray arrays = context.obtainStyledAttributes(attrs, R.styleable.CircleProgressBar);
        mOutBorderColor = arrays.getColor(R.styleable.CircleProgressBar_outBorderColor, Color.WHITE);
        mInBorderColor = arrays.getColor(R.styleable.CircleProgressBar_inBorderColor, Color.WHITE);
        mInCircleColor = arrays.getColor(R.styleable.CircleProgressBar_inCircleColor, Color.WHITE);
        mProgressColor = arrays.getColor(R.styleable.CircleProgressBar_progressColor, Color.WHITE);
        mProgressDefaultColor = arrays.getColor(R.styleable.CircleProgressBar_progressDefaultColor, Color.WHITE);
        mOutBorderWidth = arrays.getDimensionPixelOffset(R.styleable.CircleProgressBar_outBorderWidth, 0);
        mInBorderWidth = arrays.getDimensionPixelOffset(R.styleable.CircleProgressBar_inBorderWidth, 0);
        mProgressWidth = arrays.getDimensionPixelOffset(R.styleable.CircleProgressBar_progressWidth, 0);
        mDescText = arrays.getString(R.styleable.CircleProgressBar_descText);
        mDescTextSize = arrays.getDimensionPixelOffset(R.styleable.CircleProgressBar_descTextSize, 0);
        mDescTextColor = arrays.getColor(R.styleable.CircleProgressBar_descTextColor, Color.WHITE);
        mProgressText = arrays.getString(R.styleable.CircleProgressBar_progressText);
        mProgressTextSize = arrays.getDimensionPixelOffset(R.styleable.CircleProgressBar_progressTextSize, 0);
        mProgressTextColor = arrays.getColor(R.styleable.CircleProgressBar_progressTextColor, Color.WHITE);
        mProgressTextStyle = arrays.getInt(R.styleable.CircleProgressBar_progressTextStyle, 0);
        mLineGap = arrays.getDimensionPixelOffset(R.styleable.CircleProgressBar_lineGap, 60);
        arrays.recycle();

        init();
    }

    public CircleProgressBar(Context context, AttributeSet attrs, int theme) {
        super(context, attrs, theme);
    }

    private void init() {

        innerCirclePaint = new Paint();
        innerCirclePaint.setAntiAlias(true);
        innerCirclePaint.setColor(mInCircleColor);

        if (mInBorderWidth != 0) {
            inBorderPaint = new Paint();
            inBorderPaint.setAntiAlias(true);
            inBorderPaint.setStyle(Paint.Style.STROKE);
            inBorderPaint.setColor(mInBorderColor);
            inBorderPaint.setStrokeWidth(mInBorderWidth);
        }
        if (mProgressWidth != 0) {
            progressPaint = new Paint();
            progressPaint.setAntiAlias(true);
            progressPaint.setColor(mProgressColor);
            progressPaint.setStyle(Paint.Style.STROKE);
            progressPaint.setStrokeWidth(mProgressWidth);
        }
        if (mOutBorderWidth != 0) {
            outBorderPaint = new Paint();
            outBorderPaint.setAntiAlias(true);
            outBorderPaint.setStyle(Paint.Style.STROKE);
            outBorderPaint.setColor(mOutBorderColor);
            outBorderPaint.setStrokeWidth(mOutBorderWidth);
        }

        if (!TextUtils.isEmpty(mProgressText) || !TextUtils.isEmpty(mDescText)) {
            textPaint = new TextPaint();
            textPaint.setAntiAlias(true);
            textPaint.setTextAlign(Paint.Align.CENTER);
        }
    }



    @Override
    protected void onDraw(Canvas canvas) {
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int radius = Math.min(centerX, centerY);
        if (outBorderPaint != null) {
            canvas.drawCircle(centerX, centerY, radius - mOutBorderWidth / 2, outBorderPaint);
        }
        if (progressPaint != null) {
            Paint defaultProgressPaint = progressPaint;
            defaultProgressPaint.setColor(mProgressDefaultColor);
            canvas.drawCircle(centerX, centerY, radius - mOutBorderWidth - mProgressWidth / 2, defaultProgressPaint);
            float angle = progress * 3.6f;
            int leftTop = mOutBorderWidth + mProgressWidth / 2;
            int rightBottom = radius * 2 - mOutBorderWidth - mProgressWidth / 2;
            RectF rect = new RectF(leftTop, leftTop, rightBottom, rightBottom);
            progressPaint.setColor(mProgressColor);
            canvas.drawArc(rect, -90, angle, false, progressPaint);
        }
        if (inBorderPaint != null) {
            canvas.drawCircle(centerX, centerY, radius - mOutBorderWidth - mProgressWidth - mInBorderWidth / 2, inBorderPaint);
        }
        if (innerCirclePaint != null) {
            canvas.drawCircle(centerX, centerY, radius - mOutBorderWidth - mProgressWidth - mInBorderWidth, innerCirclePaint);
        }

        if (textPaint != null) {
            Paint.FontMetricsInt fontMetrics = textPaint.getFontMetricsInt();
            int height = getLayoutParams().height;
            int baseLine = (getHeight() - fontMetrics.bottom - fontMetrics.top) / 2;
            int y = (height + fontMetrics.bottom - fontMetrics.top) / 2;
            if (!TextUtils.isEmpty(mProgressText)) {
                textPaint.setTextSize(mProgressTextSize);
                textPaint.setColor(mProgressTextColor);
                int textStyleBold = 1;
                if (mProgressTextStyle == textStyleBold) {
                    textPaint.setTypeface(Typeface.DEFAULT_BOLD);
                }
                canvas.drawText(mProgressText, centerX, baseLine, textPaint);
            }
            if (!TextUtils.isEmpty(mDescText)) {
                textPaint.setTextSize(mDescTextSize);
                textPaint.setColor(mDescTextColor);
                canvas.drawText(mDescText, centerX, centerY + mLineGap/2, textPaint);
            }
        }
    }

    public void setProgress(int progress) {
        if (progress >=0 && progress <= PROGRESS_MAX_VALUE) {
            this.progress = progress;
            this.mProgressText = progress + "%";
            invalidate();
        }
    }

    public void setProgress(int progress, int textColor) {
        if (progress >=0 && progress <= PROGRESS_MAX_VALUE) {
            this.progress = progress;
            this.mProgressText = progress + "%";
            this.mProgressTextColor = textColor;
            invalidate();
        }
    }

    public void setDescText(String desc) {
        this.mDescText = desc;
        invalidate();
    }

}
