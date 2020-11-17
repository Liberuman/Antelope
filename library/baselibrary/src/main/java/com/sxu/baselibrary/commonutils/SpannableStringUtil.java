package com.sxu.baselibrary.commonutils;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.TypefaceSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Freeman on 17/4/1.
 */

public final class SpannableStringUtil {

	private SpannableStringUtil() {
		throw new UnsupportedOperationException();
	}

	/**
	 * 设置文字大小
	 */
	public static final int SPAN_TYPE_TEXT_SIZE = 1;
	/**
	 * 设置文字颜色
	 */
	public static final int SPAN_TYPE_TEXT_COLOR = 2;
	/**
	 * 设置文字样式：取值为：Typeface.BOLD(加粗)， Typeface.ITALIC(斜体)， Typeface.BOLD_ITALIC(加粗+斜体)
	 */
	public static final int SPAN_TYPE_TEXT_STYLE = 3;
	/**
	 * 为文字添加删除线
	 */
	public static final int SPAN_TYPE_STRIKE_THROUGH = 4;
	/**
	 * 为文字添加下划线
	 */
	public static final int SPAN_TYPE_UNDERLINE = 5;
	/**
	 * 设置文字的字体
	 */
	public static final int SPAN_TYPE_TYPEFACE = 6;
	/**
	 * 为文字添加点击事件
	 */
	public static final int SPAN_TYPE_CLICKABLE = 7;
	
	/**
	 * 设置单个位置的文字样式
	 * @param textView
	 * @param text
	 * @param type      要设置的类型：文字大小，文字颜色，文字样式，添加删除线
	 * @param value     设置的类型不同传不同的值, 可表示文字大小，文字颜色，文字样式等
	 * @param startPos
	 * @param len
	 * @return
	 */
	public static void setText(TextView textView, String text, int type, int value, int startPos, int len) {
		switch (type) {
			case SPAN_TYPE_TEXT_SIZE:
				textView.setText(getTextSizeSpannable(text, value, startPos, len));
				break;
			case SPAN_TYPE_TEXT_COLOR:
				textView.setText(getColorSpannable(text, value, startPos, len));
				break;
			case SPAN_TYPE_TEXT_STYLE:
				textView.setText(getStyleSpannable(text, value, startPos, len));
				break;
			case SPAN_TYPE_STRIKE_THROUGH:
				textView.setText(getStrikeThroughSpannable(text, startPos, len));
				break;
			case SPAN_TYPE_UNDERLINE:
				textView.setText(getUnderlineSpannable(text, startPos, len));
				break;
			default:
				break;
		}
	}

	/**
	 * 设置多个位置的文字样式
	 * @param textView
	 * @param text
	 * @param type      要设置的类型：文字大小，文字颜色，文字样式，添加删除线
	 * @param value     设置的类型不同传不同的值, 可表示文字大小，文字颜色，文字样式等
	 * @param startPos
	 * @param len
	 * @return
	 */
	public static void setText(TextView textView, String text, int type, int value, int[] startPos, int[] len) {
		switch (type) {
			case SPAN_TYPE_TEXT_SIZE:
				textView.setText(getTextSizeSpannable(text, value, startPos, len));
				break;
			case SPAN_TYPE_TEXT_COLOR:
				textView.setText(getColorSpannable(text, value, startPos, len));
				break;
			case SPAN_TYPE_TEXT_STYLE:
				textView.setText(getStyleSpannable(text, value, startPos, len));
				break;
			case SPAN_TYPE_STRIKE_THROUGH:
				textView.setText(getStrikeThroughSpannable(text, startPos, len));
				break;
			default:
				break;
		}
	}

	/**
	 * 设置单个位置的文字颜色
	 * @param text
	 * @param textColor
	 * @param startPos
	 * @param len
	 * @return
	 */
	public static SpannableString getColorSpannable(String text, int textColor, int startPos, int len) {
		SpannableString span = new SpannableString(text);
		span.setSpan(new ForegroundColorSpan(textColor), startPos, startPos+len, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
		return span;
	}

	/**
	 * 设置多个位置的文字颜色
	 * @param text
	 * @param textColor
	 * @param startPos
	 * @param len
	 * @return
	 */
	public static SpannableString getColorSpannable(String text, int textColor, int[] startPos, int[] len) {
		SpannableString span = new SpannableString(text);
		if (startPos != null && len != null && startPos.length > 0 && len.length > 0 && startPos.length == len.length) {
			for (int i = 0; i < startPos.length; i++) {
				span.setSpan(new ForegroundColorSpan(textColor), startPos[i], startPos[i]+len[i], Spanned.SPAN_INCLUSIVE_INCLUSIVE);
			}
		}

		return span;
	}

	/**
	 * 设置单个位置的文字样式（加粗，斜体）
	 * @param text
	 * @param textStyle Typeface.BOLD(加粗)， Typeface.ITALIC(斜体)， Typeface.BOLD_ITALIC(加粗+斜体)
	 * @param startPos
	 * @param len
	 * @return
	 */
	public static SpannableString getStyleSpannable(String text, int textStyle, int startPos, int len) {
		SpannableString span = new SpannableString(text);
		span.setSpan(new StyleSpan(textStyle), startPos, startPos+len, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
		return span;
	}

	/**
	 * 设置多个位置的文字样式（加粗，斜体）
	 * @param text
	 * @param textStyle Typeface.BOLD(加粗)， Typeface.ITALIC(斜体)， Typeface.BOLD_ITALIC(加粗+斜体)
	 * @param startPos
	 * @param len
	 * @return
	 */
	public static SpannableString getStyleSpannable(String text, int textStyle, int[] startPos, int[] len) {
		SpannableString span = new SpannableString(text);
		if (startPos != null && len != null && startPos.length > 0 && len.length > 0 && startPos.length == len.length) {
			for (int i = 0; i < startPos.length; i++) {
				span.setSpan(new StyleSpan(textStyle), startPos[i], startPos[i]+len[i], Spanned.SPAN_INCLUSIVE_INCLUSIVE);
			}
		}

		return span;
	}


	/**
	 * 设置单个位置的文字大小
	 * @param text
	 * @param textSize Typeface.BOLD(加粗)， Typeface.ITALIC(斜体)， Typeface.BOLD_ITALIC(加粗+斜体)
	 * @param startPos
	 * @param len
	 * @return
	 */
	public static SpannableString getTextSizeSpannable(String text, int textSize, int startPos, int len) {
		SpannableString span = new SpannableString(text);
		span.setSpan(new AbsoluteSizeSpan(textSize), startPos, startPos+len, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
		return span;
	}

	/**
	 * 设置单个位置的文字大小
	 * @param span
	 * @param textSize Typeface.BOLD(加粗)， Typeface.ITALIC(斜体)， Typeface.BOLD_ITALIC(加粗+斜体)
	 * @param startPos
	 * @param len
	 * @return
	 */
	public static SpannableString getTextSizeSpannable(SpannableString span, int textSize, int startPos, int len) {
		span.setSpan(new AbsoluteSizeSpan(textSize), startPos, startPos+len, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
		return span;
	}

	/**
	 * 设置多个位置的文字大小
	 * @param text
	 * @param textSize
	 * @param startPos
	 * @param len
	 * @return
	 */
	public static SpannableString getTextSizeSpannable(String text, int textSize, int[] startPos, int[] len) {
		SpannableString span = new SpannableString(text);
		if (startPos != null && len != null && startPos.length > 0 && len.length > 0 && startPos.length == len.length) {
			for (int i = 0; i < startPos.length; i++) {
				span.setSpan(new AbsoluteSizeSpan(textSize), startPos[i], startPos[i]+len[i], Spanned.SPAN_INCLUSIVE_INCLUSIVE);
			}
		}

		return span;
	}


	/**
	 * 为单个位置的文字添加删除线
	 * @param text
	 * @param startPos
	 * @param len
	 * @return
	 */
	public static SpannableString getStrikeThroughSpannable(String text, int startPos, int len) {
		SpannableString span = new SpannableString(text);
		span.setSpan(new StrikethroughSpan(), startPos, startPos+len, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
		return span;
	}

	/**
	 * 为多个位置的文字添加删除线
	 * @param text
	 * @param startPos
	 * @param len
	 * @return
	 */
	public static SpannableString getStrikeThroughSpannable(String text, int[] startPos, int[] len) {
		SpannableString span = new SpannableString(text);
		if (startPos != null && len != null && startPos.length > 0 && len.length > 0 && startPos.length == len.length) {
			for (int i = 0; i < startPos.length; i++) {
				span.setSpan(new StrikethroughSpan(), startPos[i], startPos[i]+len[i], Spanned.SPAN_INCLUSIVE_INCLUSIVE);
			}
		}

		return span;
	}

	/**
	 * 为一段文字添加下划线
	 * @param text
	 * @param startPos
	 * @param len
	 * @return
	 */
	public static SpannableString getUnderlineSpannable(String text, int startPos, int len) {
		SpannableString span = new SpannableString(text);
		span.setSpan(new UnderlineSpan(), startPos, startPos+len, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
		return span;
	}

	/**
	 * 为多段文字添加下划线
	 * @param text
	 * @param startPos
	 * @param len
	 * @return
	 */
	public static SpannableString getUnderlineSpannable(String text, int[] startPos, int[] len) {
		SpannableString span = new SpannableString(text);
		if (startPos != null && len != null && startPos.length > 0 && len.length > 0 && startPos.length == len.length) {
			for (int i = 0; i < startPos.length; i++) {
				span.setSpan(new UnderlineSpan(), startPos[i], startPos[i]+len[i], Spanned.SPAN_INCLUSIVE_INCLUSIVE);
			}
		}

		return span;
	}

	/**
	 * 为文字设置下划线效果
	 * @param textView
	 * @param text
	 * @param startPos
	 * @param endPos
	 */
	public static void setUnderlineSpan(TextView textView, String text, int startPos, int endPos) {
		SpannableString span = new SpannableString(text);
		span.setSpan(new UnderlineSpan(), startPos, endPos, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
		textView.setText(span);
	}

	/**
	 * 上面的方法都是针对单一样式，如果需要多样式，可使用下面的类进行多次setStyle实现
	 */
	public static class SpanStyle {

		private int startPos;
		private int len;
		private SpannableString span;

		public SpanStyle createSpan(String text) {
			span = new SpannableString(text);
			return this;
		}

		public SpanStyle createSpan(String text, int startPos, int len) {
			this.startPos = startPos;
			this.len = len;
			span = new SpannableString(text);
			return this;
		}

		public SpanStyle setStyle(int style, int value) {
			return setStyle(style, value, startPos, len);
		}

		public SpanStyle setStyle(int style, int value, int startPos, int len) {
			Object object;
			switch (style) {
				case SPAN_TYPE_TEXT_SIZE:
					object = new AbsoluteSizeSpan(value);
					break;
				case SPAN_TYPE_TEXT_COLOR:
					object = new ForegroundColorSpan(value);
					break;
				case SPAN_TYPE_TEXT_STYLE:
					object = new StyleSpan(value);
					break;
				case SPAN_TYPE_STRIKE_THROUGH:
					object = new StrikethroughSpan();
					break;
				default:
					throw new IllegalArgumentException("The style is not supported");
			}
			span.setSpan(object, startPos, startPos+len, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
			return this;
		}

		/**
		 * 设置文字的字体
		 * @param typeface 所需字体
		 * @param style 字体风格
		 * @param startPos
		 * @param len
		 * @return
		 */
		public SpanStyle setStyle(Typeface typeface, int style, int startPos, int len) {
			Object object = new CustomTypefaceSpan(typeface, style);
			span.setSpan(object, startPos, startPos+len, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
			return this;
		}

		/**
		 * 设置文字为可点击样式
		 * @param startPos
		 * @param len
		 * @param listener
		 * @return
		 */
		public SpanStyle setStyle(int startPos, int len, final View.OnClickListener listener) {
			Object object = new ClickableSpan() {
				@Override
				public void onClick(View widget) {
					if (listener != null) {
						listener.onClick(widget);
					}
				}
			};
			span.setSpan(object, startPos, startPos+len, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
			return this;
		}

		public SpannableString getSpan() {
			return span;
		}

		public void apply(TextView textView) {
			textView.setText(span);
		}
	}

	public static class CustomTypefaceSpan extends TypefaceSpan {

		private Typeface typeface;
		private int style;

		public CustomTypefaceSpan(Typeface typeface, int style) {
			super("");
			this.typeface = typeface;
			this.style = style;
		}

		@Override
		public void updateDrawState(TextPaint ds) {
			super.updateDrawState(ds);
			applyCustomTypeface(ds);
		}

		@Override
		public void updateMeasureState(TextPaint paint) {
			super.updateMeasureState(paint);
			applyCustomTypeface(paint);
		}

		private void applyCustomTypeface(Paint paint) {
			if (style == Typeface.BOLD) {
				paint.setFakeBoldText(true);
			} else if (style == Typeface.ITALIC) {
				paint.setTextSkewX(-0.25f);
			}
			paint.setTypeface(typeface);
		}
	}
}
