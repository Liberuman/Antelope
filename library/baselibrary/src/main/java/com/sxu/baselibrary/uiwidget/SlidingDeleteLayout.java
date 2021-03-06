package com.sxu.baselibrary.uiwidget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;

import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.sxu.baselibrary.R;
import com.sxu.baselibrary.commonutils.DisplayUtil;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;


/*******************************************************************************
 * Description: 侧滑删除控件
 *
 * Author: Freeman
 *
 * Date: 2018/1/9
 *
 * Copyright: all rights reserved by Freeman.
 *******************************************************************************/
public class SlidingDeleteLayout extends LinearLayout {

	private View mDeleteView;
	private int mDeleteViewResId;
	private int mScrollOffset;

	private float mStartX;
	private float mStartY;
	private boolean scrollToRight;
	private Scroller mScroller;
	private boolean mCanScroll = true;
	private int maxScrollDistance;
	private OnEventListener mEventListener;

	/**
	 * 侧滑删除空间的打开状态
	 */
	private final static int STATUS_CLOSED = 0;
	private final static int STATUS_OPENED = 1;

	private int mStatus = STATUS_CLOSED;

	public SlidingDeleteLayout(Context context) {
		this(context, null);
	}

	public SlidingDeleteLayout(Context context, @Nullable AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SlidingDeleteLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.bl_SlidingDeleteLayout);
		mDeleteViewResId = typedArray.getResourceId(R.styleable.bl_SlidingDeleteLayout_bl_deleteViewResId, 0);
		mScrollOffset = typedArray.getDimensionPixelOffset(R.styleable.bl_SlidingDeleteLayout_bl_scrollOffset, 120);

		typedArray.recycle();

		mScroller = new Scroller(getContext());
		setOrientation(LinearLayout.HORIZONTAL);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		if (getChildCount() > 1) {
			throw new IllegalStateException("child count more than one");
		}
		if (getChildCount() == 1) {
			getChildAt(0).setMinimumWidth(Resources.getSystem().getDisplayMetrics().widthPixels);
		}

		if (mDeleteViewResId != 0) {
			mDeleteView = View.inflate(getContext(), mDeleteViewResId, null);
			if (mDeleteView != null) {
				addView(mDeleteView);
			}
		} else {
			TextView deleteText = new TextView(getContext());
			deleteText.setText("删除");
			deleteText.setTextSize(16);
			deleteText.setTextColor(Color.WHITE);
			deleteText.setGravity(Gravity.CENTER);
			deleteText.setBackgroundColor(Color.RED);
			mDeleteView = deleteText;
			addView(mDeleteView, new LayoutParams(DisplayUtil.dpToPx(80), LayoutParams.MATCH_PARENT));
		}
	}

	@Override
	public void setOrientation(int orientation) {
		if (orientation == HORIZONTAL) {
			super.setOrientation(orientation);
		}
	}

	@Override
	public void computeScroll() {
		super.computeScroll();
		if (mScroller.computeScrollOffset()) {
			scrollTo(mScroller.getCurrX(), 0);
			invalidate();
		}
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				mStartX = event.getX();
				mStartY = event.getY();
				if (mEventListener != null) {
					mEventListener.onDown();
				}
				break;
			case MotionEvent.ACTION_MOVE:
				if (mCanScroll && Math.abs(event.getX() - mStartX) > Math.abs(event.getY() - mStartY)) {
					return true;
				}
				break;
			default:
				break;
		}

		return super.onInterceptTouchEvent(event);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {

			case MotionEvent.ACTION_MOVE:
				if (mCanScroll && Math.abs(ev.getX() - mStartX) > Math.abs(ev.getY() - mStartY)) {
					getParent().requestDisallowInterceptTouchEvent(true);
				}
				break;
			default:
				break;
		}

		return super.dispatchTouchEvent(ev);
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
			case MotionEvent.ACTION_MOVE:
				if (getScrollX() <= maxScrollDistance) {
					float gap = mStartX - event.getX();
					// 向左滑动
					if (event.getX() < mStartX) {
						scrollToRight = false;
						if (getScrollX() + gap >= maxScrollDistance) {
							gap = maxScrollDistance - getScrollX();
						}
					} else {
						scrollToRight = true;
						if (getScrollX() + gap <= 0) {
							gap = -getScrollX();
						}
					}
					if (gap != 0) {
						scrollTo((int) (getScrollX() +  (gap)), 0);
						mStartX = event.getX();
						if (getScrollX() == maxScrollDistance) {
							mStatus = STATUS_OPENED;
						} else if (getScrollX() == 0) {
							mStatus = STATUS_CLOSED;
						}
					}
				}
				break;
			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_UP:
				if (scrollToRight) {
					if (getScrollX() + mScrollOffset >= maxScrollDistance) {
						mStatus = STATUS_OPENED;
						mScroller.startScroll(getScrollX(), 0, maxScrollDistance - getScrollX(), 0);
						invalidate();
					} else {
						mStatus = STATUS_CLOSED;
						mScroller.startScroll(getScrollX(), 0, -getScrollX(), 0);
						invalidate();
					}
				} else {
					if (getScrollX() >= mScrollOffset && getScrollX() < maxScrollDistance) {
						mStatus = STATUS_OPENED;
						mScroller.startScroll(getScrollX(), 0, maxScrollDistance - getScrollX(), 0);
						invalidate();
					} else if (getScrollX() >= 0 && getScrollX() < mScrollOffset){
						mStatus = STATUS_CLOSED;
						mScroller.startScroll(getScrollX(), 0, -getScrollX(), 0);
						invalidate();
					}
				}
				if (mEventListener != null) {
					mEventListener.onUp(mStatus);
				}
				getParent().requestDisallowInterceptTouchEvent(false);
				break;
			default:
				break;
		}

		return true;
	}

	public void setCanScroll(boolean canScroll) {
		this.mCanScroll = canScroll;
	}

	public int getStatus() {
		return mStatus;
	}

	public void setStatus(int status) {
		this.mStatus = status;
	}

	public void closeItem() {
		if (mStatus == STATUS_OPENED) {
			mScroller.startScroll(getScrollX(), 0, -getScrollX(), 0);
			invalidate();
			mStatus = STATUS_CLOSED;
		}
	}

	public void closeItemNoAnim() {
		scrollTo(0, 0);
		mStatus = STATUS_CLOSED;
	}

	public void openItem(){
		mScroller.startScroll(0, 0, maxScrollDistance,0);
		invalidate();
	}

	public void openItemNoAnim(){
		post(new Runnable() {
			@Override
			public void run() {
				scrollTo(maxScrollDistance, 0);
				mStatus = STATUS_OPENED;
			}
		});
	}

	public void setScrollOffset(int scrollOffset) {
		this.mScrollOffset = scrollOffset;
	}

	public void setDeleteView(@LayoutRes int resId) {
		this.mDeleteViewResId = resId;
		this.mDeleteView = View.inflate(getContext(), resId, null);
		addView(mDeleteView);
	}

	public void setDeleteView(View deleteView) {
		this.mDeleteView = deleteView;
		addView(mDeleteView);
	}

	public void setDeleteViewHeight(int height) {
		this.mDeleteView.setMinimumHeight(height);
	}

	public void setOnItemClickListener(final OnMenuItemClickListener listener) {
		if (mDeleteView != null) {
			if (mDeleteView instanceof ViewGroup) {
				for (int i = 0, childCount = ((ViewGroup)mDeleteView).getChildCount(); i < childCount; i++) {
					final int index = i;
					((ViewGroup) mDeleteView).getChildAt(i).setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							listener.onItemClick(index, ((ViewGroup) mDeleteView).getChildAt(index));
						}
					});
				}
			} else {
				mDeleteView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						listener.onItemClick(0, mDeleteView);
					}
				});
			}
		}
	}

	public void setOnEventListener(OnEventListener listener) {
		this.mEventListener = listener;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int width = 0;
		int height;
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		if (heightMode == MeasureSpec.EXACTLY) {
			height = MeasureSpec.getSize(heightMeasureSpec);
		} else {
			height = Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN ? getMinimumHeight() : getSuggestedMinimumHeight();
		}
		mDeleteView.setMinimumHeight(height);
		for (int i = 0, childCount = getChildCount(); i < childCount; i++) {
			View childView = getChildAt(i);
			measureChild(childView, widthMeasureSpec, heightMeasureSpec);
			width += childView.getMeasuredWidth();
			height = Math.max(height, childView.getMeasuredHeight());
		}

		setMeasuredDimension(width, height);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		int childCount = 2;
		if (getChildCount() == childCount) {
			maxScrollDistance = getChildAt(1).getMeasuredWidth();
		}
	}

	public interface OnMenuItemClickListener {
		/**
		 * 侧滑删除菜单被点击时被调用
		 * @param index 侧滑菜单项的索引
		 * @param childView 被点击的侧滑菜单对应的View
		 */
		void onItemClick(int index, View childView);
	}

	public interface OnEventListener {

		/**
		 * 侧滑菜单手指按下时被调用
		 */
		void onDown();

		/**
		 * 侧滑菜单手指抬起时被调用
		 * @param status
		 */
		void onUp(int status);
	}
}