package com.sxu.basecomponent.decoration;

/*
 * Copyright 2019. Bin Jing (https://github.com/youlookwhat)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.DrawableRes;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 给 LinearLayoutManager 增加分割线，可设置去除首尾分割线个数
 *
 * @author jingbin
 * https://github.com/youlookwhat/ByRecyclerView
 */
public class DefaultItemDecoration extends RecyclerView.ItemDecoration {

    public static final int HORIZONTAL = LinearLayout.HORIZONTAL;
    public static final int VERTICAL = LinearLayout.VERTICAL;
    private static final String TAG = "itemDivider";
    private Context mContext;
    private Drawable mDivider;
    private Rect mBounds = new Rect();
    /**
     * 在AppTheme里配置 android:listDivider
     */
    private static final int[] ATTRS = new int[]{android.R.attr.listDivider};
    /**
     * 头部 不显示分割线的item个数 这里应该包含刷新头，
     * 比如有一个headerView和有下拉刷新，则这里传 2
     */
    private int mHeaderNoShowSize = 0;
    /**
     * 尾部 不显示分割线的item个数 默认不显示最后一个item的分割线
     */
    private int mFooterNoShowSize = 1;
    /**
     * Current orientation. Either {@link #HORIZONTAL} or {@link #VERTICAL}.
     */
    private int mOrientation;

    private Paint mPaint;
    /**
     * 如果分割线是横向 - 高度
     * 如果分割线是横向 - 宽度
     */
    private int mDividerSpacing;
    /**
     * 如果分割线是横向 - 左边距
     * 如果分割线是纵向 - 上边距
     */
    private int mStartPadding;
    /**
     * 如果分割线是横向 - 右边距
     * 如果分割线是纵向 - 下边距
     */
    private int mEndPadding;

    public DefaultItemDecoration(Context context) {
        this(context, VERTICAL, 0, 1);
    }

    public DefaultItemDecoration(Context context, int orientation) {
        this(context, orientation, 0, 1);
    }

    public DefaultItemDecoration(Context context, int orientation, int headerNoShowSize) {
        this(context, orientation, headerNoShowSize, 1);
    }

    /**
     * Creates a divider {@link RecyclerView.ItemDecoration}
     *
     * @param context          Current context, it will be used to access resources.
     * @param orientation      Divider orientation. Should be {@link #HORIZONTAL} or {@link #VERTICAL}.
     * @param headerNoShowSize headerViewSize + RefreshViewSize
     * @param footerNoShowSize footerViewSize
     */
    public DefaultItemDecoration(Context context, int orientation, int headerNoShowSize, int footerNoShowSize) {
        mContext = context;
        mHeaderNoShowSize = headerNoShowSize;
        mFooterNoShowSize = footerNoShowSize;
        setOrientation(orientation);
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        mDivider = a.getDrawable(0);
        a.recycle();
    }

    /**
     * Sets the orientation for this divider. This should be called if
     * {@link RecyclerView.LayoutManager} changes orientation.
     *
     * @param orientation {@link #HORIZONTAL} or {@link #VERTICAL}
     */
    public DefaultItemDecoration setOrientation(int orientation) {
        if (orientation != HORIZONTAL && orientation != VERTICAL) {
            throw new IllegalArgumentException("Invalid orientation. It should be either HORIZONTAL or VERTICAL");
        }
        mOrientation = orientation;
        return this;
    }

    /**
     * Sets the {@link Drawable} for this divider.
     *
     * @param drawable Drawable that should be used as a divider.
     */
    public DefaultItemDecoration setDrawable(Drawable drawable) {
        if (drawable == null) {
            throw new IllegalArgumentException("drawable cannot be null.");
        }
        mDivider = drawable;
        return this;
    }

    public DefaultItemDecoration setDrawable(@DrawableRes int id) {
        setDrawable(ContextCompat.getDrawable(mContext, id));
        return this;
    }

    @Override
    public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        if (parent.getLayoutManager() == null || (mDivider == null && mPaint == null)) {
            return;
        }
        if (mOrientation == VERTICAL) {
            drawVertical(canvas, parent, state);
        } else {
            drawHorizontal(canvas, parent, state);
        }
    }

    private void drawVertical(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        canvas.save();
        final int left;
        final int right;
        //noinspection AndroidLintNewApi - NewApi lint fails to handle overrides.
        if (parent.getClipToPadding()) {
            left = parent.getPaddingLeft();
            right = parent.getWidth() - parent.getPaddingRight();
            canvas.clipRect(left, parent.getPaddingTop(), right, parent.getHeight() - parent.getPaddingBottom());
        } else {
            left = 0;
            right = parent.getWidth();
        }

        final int childCount = parent.getChildCount();
        final int lastPosition = state.getItemCount() - 1;
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final int childRealPosition = parent.getChildAdapterPosition(child);

            // 过滤到头部不显示的分割线
            if (childRealPosition < mHeaderNoShowSize) {
                continue;
            }
            // 过滤到尾部不显示的分割线
            if (childRealPosition <= lastPosition - mFooterNoShowSize) {
                if (mDivider != null) {
                    parent.getDecoratedBoundsWithMargins(child, mBounds);
                    final int bottom = mBounds.bottom + Math.round(child.getTranslationY());
                    final int top = bottom - mDivider.getIntrinsicHeight();
                    mDivider.setBounds(left, top, right, bottom);
                    mDivider.draw(canvas);
                }

                if (mPaint != null) {
                    RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
                    int left1 = left + mStartPadding;
                    int right1 = right - mEndPadding;
                    int top1 = child.getBottom() + params.bottomMargin;
                    int bottom1 = top1 + mDividerSpacing;
                    canvas.drawRect(left1, top1, right1, bottom1, mPaint);
                }
            }
        }
        canvas.restore();
    }

    private void drawHorizontal(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        canvas.save();
        final int top;
        final int bottom;
        //noinspection AndroidLintNewApi - NewApi lint fails to handle overrides.
        if (parent.getClipToPadding()) {
            top = parent.getPaddingTop();
            bottom = parent.getHeight() - parent.getPaddingBottom();
            canvas.clipRect(parent.getPaddingLeft(), top,
                    parent.getWidth() - parent.getPaddingRight(), bottom);
        } else {
            top = 0;
            bottom = parent.getHeight();
        }

        final int childCount = parent.getChildCount();
        final int lastPosition = state.getItemCount() - 1;
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final int childRealPosition = parent.getChildAdapterPosition(child);

            // 过滤到头部不显示的分割线
            if (childRealPosition < mHeaderNoShowSize) {
                continue;
            }
            // 过滤到尾部不显示的分割线
            if (childRealPosition <= lastPosition - mFooterNoShowSize) {
                if (mDivider != null) {
                    parent.getDecoratedBoundsWithMargins(child, mBounds);
                    final int right = mBounds.right + Math.round(child.getTranslationX());
                    final int left = right - mDivider.getIntrinsicWidth();
                    mDivider.setBounds(left, top, right, bottom);
                    mDivider.draw(canvas);
                }

                if (mPaint != null) {
                    RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
                    int left1 = child.getRight() + params.rightMargin;
                    int right1 = left1 + mDividerSpacing;
                    int top1 = top + mStartPadding;
                    int bottom1 = bottom - mEndPadding;
                    canvas.drawRect(left1, top1, right1, bottom1, mPaint);
                }
            }
        }
        canvas.restore();
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (mDivider == null && mPaint == null) {
            outRect.set(0, 0, 0, 0);
            return;
        }
        //parent.getChildCount() 不能拿到item的总数
        int lastPosition = state.getItemCount() - 1;
        int position = parent.getChildAdapterPosition(view);
        boolean isShowDivider = mHeaderNoShowSize <= position && position <= lastPosition - mFooterNoShowSize;

        if (mOrientation == VERTICAL) {
            if (isShowDivider) {
                outRect.set(0, 0, 0, mDivider != null ? mDivider.getIntrinsicHeight() : mDividerSpacing);
            } else {
                outRect.set(0, 0, 0, 0);
            }
        } else {
            if (isShowDivider) {
                outRect.set(0, 0, mDivider != null ? mDivider.getIntrinsicWidth() : mDividerSpacing, 0);
            } else {
                outRect.set(0, 0, 0, 0);
            }
        }
    }

    /**
     * 设置不显示分割线的item位置与个数
     *
     * @param headerNoShowSize 头部 不显示分割线的item个数
     * @param footerNoShowSize 尾部 不显示分割线的item个数，默认1，不显示最后一个,最后一个一般为加载更多view
     */
    public DefaultItemDecoration setNoShowDivider(int headerNoShowSize, int footerNoShowSize) {
        this.mHeaderNoShowSize = headerNoShowSize;
        this.mFooterNoShowSize = footerNoShowSize;
        return this;
    }

    /**
     * 设置不显示头部分割线的item个数
     *
     * @param headerNoShowSize 头部 不显示分割线的item个数
     */
    public DefaultItemDecoration setHeaderNoShowDivider(int headerNoShowSize) {
        this.mHeaderNoShowSize = headerNoShowSize;
        return this;
    }

    public DefaultItemDecoration setParam(int dividerColor, int dividerSpacing) {
        return setParam(dividerColor, dividerSpacing, 0, 0);
    }

    /**
     * 直接设置分割线颜色等，不设置drawable
     *
     * @param dividerColor         分割线颜色
     * @param dividerSpacing       分割线间距
     * @param leftTopPaddingDp     如果是横向 - 左边距
     *                             如果是纵向 - 上边距
     * @param rightBottomPaddingDp 如果是横向 - 右边距
     *                             如果是纵向 - 下边距
     */
    public DefaultItemDecoration setParam(int dividerColor, int dividerSpacing, float leftTopPaddingDp, float rightBottomPaddingDp) {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(ContextCompat.getColor(mContext, dividerColor));
        mDividerSpacing = dividerSpacing;
        mStartPadding = dip2px(leftTopPaddingDp);
        mEndPadding = dip2px(rightBottomPaddingDp);
        mDivider = null;
        return this;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public int dip2px(float dpValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


}
