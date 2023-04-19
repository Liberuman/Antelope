package com.sxu.basecomponent.uiwidget;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;

import com.sxu.basecomponent.R;
import com.sxu.basecomponent.utils.PrivateScreenUtil;

/*******************************************************************************
 * Description: 验证码输入框
 *
 * Author: Freeman
 *
 * Date: 2018/8/14
 *
 * Copyright: all rights reserved by Freeman.
 *******************************************************************************/
public class VerificationCodeLayout extends LinearLayout {

	private final int itemWidth;
	private final int itemHeight;
	private final int itemCount;
	private final int itemGap;
	private final int itemDefaultGap;
	private final int itemDefaultGapColor;
	private final int itemBgColor;
	private final int itemBorderWidth;
	private final int itemBorderColor;
	private final int itemRadius;
	private final int textSize;
	private final int textColor;
	private final boolean textIsBold;
	private final boolean isPassword;

	private OnInputListener listener;

	public VerificationCodeLayout(Context context) {
		this(context, null, 0);
	}

	public VerificationCodeLayout(Context context, @Nullable AttributeSet attrs) {
		this(context, null, 0);
	}

	public VerificationCodeLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.VerificationCodeLayout, defStyleAttr, 0);
		itemWidth = typedArray.getDimensionPixelSize(R.styleable.VerificationCodeLayout_itemWidth, PrivateScreenUtil.dpToPx(45));
		itemHeight = typedArray.getDimensionPixelSize(R.styleable.VerificationCodeLayout_itemHeight, PrivateScreenUtil.dpToPx(45));
		itemCount = typedArray.getInt(R.styleable.VerificationCodeLayout_itemCount, 6);
		itemGap = typedArray.getDimensionPixelSize(R.styleable.VerificationCodeLayout_itemGap, 0);
		itemDefaultGap = typedArray.getDimensionPixelSize(R.styleable.VerificationCodeLayout_itemDefaultGap, 2);
		itemDefaultGapColor = typedArray.getColor(R.styleable.VerificationCodeLayout_itemDefaultGapColor, Color.GRAY);
		itemBgColor = typedArray.getColor(R.styleable.VerificationCodeLayout_itemBgColor, Color.TRANSPARENT);
		itemBorderWidth = typedArray.getDimensionPixelSize(R.styleable.VerificationCodeLayout_itemBorderWidth, PrivateScreenUtil.dpToPx(1));
		itemBorderColor = typedArray.getColor(R.styleable.VerificationCodeLayout_itemBorderColor, Color.GRAY);
		itemRadius = typedArray.getDimensionPixelSize(R.styleable.VerificationCodeLayout_itemRadius, PrivateScreenUtil.dpToPx(4));
		textSize = typedArray.getDimensionPixelSize(R.styleable.VerificationCodeLayout_itemTextSize, 18);
		textColor = typedArray.getColor(R.styleable.VerificationCodeLayout_itemTextColor, Color.BLACK);
		textIsBold = typedArray.getBoolean(R.styleable.VerificationCodeLayout_itemTextIsBold, true);
		isPassword = typedArray.getBoolean(R.styleable.VerificationCodeLayout_isPassword, false);
		typedArray.recycle();
		initView();
	}

	private void initView() {
		setGravity(Gravity.CENTER_HORIZONTAL);
		setOrientation(HORIZONTAL);
		setShowDividers(SHOW_DIVIDER_MIDDLE);
		Drawable divider = createDivider();
		setDividerDrawable(divider);
		addItemLayout();
	}

	private void addItemLayout() {
		Drawable itemBg = null;
		if (itemGap == 0) {
			ViewBgUtil.setShapeBg(this, itemBgColor, itemBorderColor, itemBorderWidth, itemRadius);
		} else {
			itemBg = ViewBgUtil.getDrawable(itemBgColor, itemBorderColor, itemBorderWidth, itemRadius);
		}
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(itemWidth, itemHeight);
		for (int i = 0; i < itemCount; i++) {
			final PasteEditText itemEdit = new PasteEditText(getContext());
			itemEdit.setGravity(Gravity.CENTER);
			itemEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)});
			itemEdit.setTextSize(textSize);
			itemEdit.setTextColor(textColor);
			if (textIsBold) {
				itemEdit.setTypeface(Typeface.DEFAULT_BOLD);
			}
			if (itemBg != null) {
				itemEdit.setBackground(itemBg);
			} else {
				itemEdit.setBackgroundColor(Color.TRANSPARENT);
			}
			if (isPassword) {
				itemEdit.setTransformationMethod(PasswordTransformationMethod.getInstance());
			} else {
				itemEdit.setInputType(InputType.TYPE_CLASS_NUMBER);
			}
			addView(itemEdit, params);

			itemEdit.addTextChangedListener(new TextWatcher() {
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {

				}

				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {

				}

				@Override
				public void afterTextChanged(Editable s) {
					if (s.length() == 1) {
						View nextView = getChildAt(indexOfChild(itemEdit) + 1);
						if (nextView != null) {
							nextView.setFocusable(true);
							nextView.requestFocus();
						} else {
							if (listener != null) {
								listener.onInputComplete();
							}
						}
					}
				}
			});

			itemEdit.setOnKeyListener(new OnKeyListener() {
				@Override
				public boolean onKey(View v, int keyCode, KeyEvent event) {
					if (event.getKeyCode() == KeyEvent.KEYCODE_DEL) {
						itemEdit.setText(null);
						EditText nextView = (EditText) getChildAt(indexOfChild(itemEdit) - 1);
						if (nextView != null) {
							nextView.requestFocus();
							nextView.setSelection(nextView.getText().length());
						}
					}
					return false;
				}
			});

			itemEdit.setOnPasteListener(new PasteEditText.OnPasteListener() {
				@Override
				public void onPaste(String content) {
					setText(content);
				}
			});
		}
	}

	private Drawable createDivider() {
		Drawable divider;
		if (itemGap == 0) {
			divider = new ColorDrawable(itemDefaultGapColor) {
				@Override
				public int getIntrinsicWidth() {
					return itemDefaultGap;
				}
			};
		} else {
			divider = new ColorDrawable() {
				@Override
				public int getIntrinsicWidth() {
					return itemGap;
				}
			};
		}

		return divider;
	}

	@Override
	public void setOrientation(int orientation) {
		if (orientation == HORIZONTAL) {
			super.setOrientation(orientation);
		}
	}

	public void clearText() {
		if (itemCount <= 0) {
			return;
		}

		for (int i = 0; i < itemCount; i++) {
			((EditText)getChildAt(i)).setText("");
		}
		(getChildAt(0)).requestFocus();
	}

	private void setText(String text) {
		if (TextUtils.isEmpty(text) || text.length() != itemCount) {
			return;
		}

		for (int i = 0; i < itemCount; i++) {
			((EditText)getChildAt(i)).setText(text.substring(i, i+1));
		}
	}

	public void setOnInputListener(OnInputListener listener) {
		this.listener = listener;
	}

	@Nullable
	@Override
	protected Parcelable onSaveInstanceState() {
		Parcelable parcelable =  super.onSaveInstanceState();
		SaveState saveState = new SaveState(parcelable);
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < itemCount; i++) {
			builder.append(((EditText)getChildAt(i)).getText().toString());
		}
		saveState.setContent(builder.toString());
		return saveState;
	}

	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		SaveState saveState = (SaveState) state;
		super.onRestoreInstanceState(((SaveState) state).getSuperState());
		setText(saveState.getContent());
	}

	public interface OnInputListener {

		/**
		 * 验证码被输入完成时被调用
		 */
		void onInputComplete();
	}

	public static class PasteEditText extends AppCompatEditText {

		private OnPasteListener listener;

		public PasteEditText(Context context) {
			super(context);
		}

		public PasteEditText(Context context, AttributeSet attrs) {
			super(context, attrs);
		}

		public PasteEditText(Context context, AttributeSet attrs, int defStyleAttr) {
			super(context, attrs, defStyleAttr);
		}

		@Override
		public boolean onTextContextMenuItem(int id) {
			if (id == android.R.id.paste) {
				ClipboardManager cm = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
				if (cm == null) {
					return super.onTextContextMenuItem(id);
				}

				ClipData clipData = cm.getPrimaryClip();
				if (clipData != null && clipData.getItemCount() > 0) {
					String content =  clipData.getItemAt(0).coerceToText(getContext()).toString();
					if (listener != null) {
						listener.onPaste(content);
					}
				}
			}

			return super.onTextContextMenuItem(id);
		}

		public void setOnPasteListener(OnPasteListener listener) {
			this.listener = listener;
		}

		@Override
		public Parcelable onSaveInstanceState() {
			return super.onSaveInstanceState();
		}

		@Override
		public void onRestoreInstanceState(Parcelable state) {
			super.onRestoreInstanceState(state);
		}

		public interface OnPasteListener {

			/**
			 * 验证码被复制时被调用
			 * @param content
			 */
			void onPaste(String content);
		}
	}

	public static class SaveState extends BaseSavedState {

		private String content;

		public SaveState(Parcelable state) {
			super(state);
		}

		@Override
		public void writeToParcel(Parcel out, int flags) {
			super.writeToParcel(out, flags);
			out.writeString(content);
		}

		public void setContent(String content) {
			this.content = content;
		}

		public String getContent() {
			return content;
		}
	}
}
