package com.sxu.common.base.dialog;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;

import com.sxu.basecomponent.adapter.BaseListAdapter;
import com.sxu.basecomponent.adapter.ViewHolder;
import com.sxu.basecomponent.dialog.BaseDialog;
import com.sxu.basecomponent.interfaces.IViewInflateListener;
import com.sxu.basecomponent.utils.PrivateScreenUtil;
import com.sxu.baselibrary.commonutils.ScreenUtil;
import com.sxu.baselibrary.commonutils.ViewBgUtil;
import com.sxu.common.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/*******************************************************************************
 * Description: 选择类的对话框
 *
 * Author: Freeman
 *
 * Date: 2018/7/23
 *
 * Copyright: all rights reserved by Freeman.
 *******************************************************************************/

public class ChooseDialog extends BaseDialog {

	/**
	 * 是否是单选
	 */
	private boolean isSingle = true;
	/**
	 * 是否是点击直接选择
	 */
	private boolean isClickSelect = false;
	/**
	 * 弹框宽度
	 */
	private int width = 0;
	/**
	 * 弹框高度
	 */
	private int height = 0;
	/**
	 * 弹框位置
	 */
	private int gravity = 0;
	/**
	 * 弹框动画
	 */
	private int windowAnimation = 0;
	/**
	 * 弹框标题
	 */
	private String title;
	/**
	 * 取消按钮的文案
	 */
	private String cancelText = "取消";
	/**
	 * 确认按钮的文案
	 */
	private String okText = "确认";
	/**
	 * 取消按钮的文字颜色
	 */
	private int cancelTextColor = 0;
	/**
	 * 确认按钮的文字颜色
	 */
	private int okTextColor = 0;
	/**
	 * 取消按钮的点击事件
	 */
	private View.OnClickListener cancelListener;
	/**
	 * 确认按钮的点击事件
	 */
	private View.OnClickListener okListener;
	/**
	 * View的加载监听，用于处理开放接口不能满足UI的情况：如设置文字大小，间距等
	 */
	private IViewInflateListener viewInflateListener;
	/**
	 * 选项数据
	 */
	private String[] items;
	/**
	 * 单选被中的选项索引
	 */
	private int checkedItemIndex = -1;
	/**
	 * 上次被选中的RadioButton
	 */
	private RadioButton selectedRadioButton;
	/**
	 * 多选被选中的选项索引
	 */
	private Set<Integer> checkedIndexList =  new TreeSet<>();

	public ChooseDialog setIsSingle(boolean isSingle) {
		this.isSingle = isSingle;
		// 多选弹框不支持点击直接选择
		if (!isSingle) {
			isClickSelect = false;
		}
		return this;
	}

	public ChooseDialog setIsClickSelect(boolean isClickSelect) {
		this.isClickSelect = isClickSelect;
		// 如果是点击选择，说明是单选，且无需确认按钮
		if (isClickSelect) {
			isSingle = true;
		}
		return this;
	}

	public ChooseDialog setWidth(int width) {
		this.width = width;
		return this;
	}

	public ChooseDialog setHeight(int height) {
		this.height = height;
		return this;
	}

	public ChooseDialog setGravity(int gravity) {
		this.gravity = gravity;
		// 如果弹框从底部弹出，默认宽度和屏幕宽度一致，同时设置动画
		if (gravity == Gravity.BOTTOM) {
			windowAnimation = R.style.BottomDialogAnimation;
			width = PrivateScreenUtil.getScreenWidth();
		}
		return this;
	}

	public ChooseDialog setWindowAnimation(int windowAnimation) {
		this.windowAnimation = windowAnimation;
		return this;
	}

	@Override
	public int getWindowAnimation() {
		return windowAnimation;
	}

	public ChooseDialog setListData(String[] itemList) {
		return setListData(Arrays.asList(itemList));
	}

	public ChooseDialog setListData(List<String> itemList) {
		if (itemList != null) {
			this.items = itemList.toArray(new String[0]);
		}
		return this;
	}

	public ChooseDialog setCheckedItemIndex(int itemIndex) {
		this.checkedItemIndex = itemIndex;
		return this;
	}

	public ChooseDialog setDefaultMultiIndex(boolean[] checkedItems) {
		if (checkedItems == null || checkedItems.length == 0) {
			return this;
		}

		for (int i = 0; i < checkedItems.length; i++) {
			if (checkedItems[i]) {
				checkedIndexList.add(i);
			}
		}

		return this;
	}

	public int getCheckedItemIndex() {
		return checkedItemIndex;
	}

	public Set<Integer> getCheckedIndexList() {
		return checkedIndexList;
	}

	public ChooseDialog setTitle(String title) {
		this.title = title;
		return this;
	}

	public ChooseDialog setCancelTextColor(int textColor) {
		this.cancelTextColor = textColor;
		return this;
	}

	public ChooseDialog setCancelText(String cancelText) {
		this.cancelText = cancelText;
		return this;
	}

	public ChooseDialog setCancelClickListener(View.OnClickListener listener) {
		this.cancelListener = listener;
		return this;
	}

	public ChooseDialog setCancelTextAndEvent(String cancelText, View.OnClickListener listener) {
		this.cancelText = cancelText;
		this.cancelListener = listener;
		return this;
	}

	public ChooseDialog setOkTextColor(int textColor) {
		this.okTextColor = textColor;
		return this;
	}

	public ChooseDialog setOkText(String okText) {
		this.okText = okText;
		return this;
	}

	public ChooseDialog setOkClickListener(View.OnClickListener listener) {
		this.okListener = listener;
		return this;
	}

	public ChooseDialog setOkTextAndEvent(String okText, View.OnClickListener listener) {
		this.okText = okText;
		this.okListener = listener;
		return this;
	}

	public ChooseDialog setViewInflateListener(IViewInflateListener listener) {
		this.viewInflateListener = listener;
		return this;
	}

	public int getChooseResult() {
		return checkedItemIndex;
	}

	public List<Integer> getMultiChooseResult() {
		return new ArrayList(checkedIndexList);
	}

	@Override
	public int getWidth() {
		if (width != 0) {
			return width;
		}
		return super.getWidth();
	}

	@Override
	public int getHeight() {
		if (height != 0) {
			return height;
		}
		return super.getHeight();
	}

	@Override
	public int getGravity() {
		if (gravity != 0) {
			return gravity;
		}
		return super.getGravity();
	}

	@Override
	public int getLayoutResId() {
		return R.layout.dialog_choose_layout;
	}

	@Override
	public void initViews() {
		if (viewInflateListener != null) {
			viewInflateListener.onViewInflated(contentView);
		}

		TextView titleText = contentView.findViewById(R.id.title_text);
		TextView cancelText = contentView.findViewById(R.id.cancel_text);
		TextView okText = contentView.findViewById(R.id.ok_text);
		View contentLayout = contentView.findViewById(R.id.content_layout);
		View horizontalView = contentView.findViewById(R.id.horizontal_line);
		View gapLine = contentView.findViewById(R.id.gap_line);
		View buttonLayout = contentView.findViewById(R.id.button_layout);
		final ListView contentList = contentView.findViewById(R.id.content_list);

		if (!TextUtils.isEmpty(title)) {
			titleText.setText(title);
		} else {
			titleText.setVisibility(View.GONE);
		}
		cancelText.setText(this.cancelText);
		if (cancelTextColor != 0) {
			cancelText.setTextColor(cancelTextColor);
		}
		okText.setText(this.okText);
		if (okTextColor != 0) {
			okText.setTextColor(okTextColor);
		}

		// 如果弹框位于底部，直接设置背景为白色
		if (getGravity() == Gravity.BOTTOM) {
			int radius = ScreenUtil.dpToPx(12);
			ViewBgUtil.setShapeBg(contentView, Color.WHITE, new float[] {radius, radius, radius, radius, 0f, 0f, 0f, 0f});
			// 点击直接选择时不需要显示底部按钮
			if (isClickSelect) {
				horizontalView.setVisibility(View.INVISIBLE);
				buttonLayout.setVisibility(View.GONE);
			} else if (TextUtils.isEmpty(this.cancelText) && TextUtils.isEmpty(this.okText)) {
				horizontalView.setVisibility(View.INVISIBLE);
				buttonLayout.setVisibility(View.GONE);
			} else if (TextUtils.isEmpty(this.cancelText)) {
				gapLine.setVisibility(View.GONE);
				cancelText.setVisibility(View.GONE);
			} else if (TextUtils.isEmpty(this.okText)) {
				gapLine.setVisibility(View.GONE);
				okText.setVisibility(View.GONE);
			} else {
				/**
				 * Nothing
				 */
			}
		} else {
			int state = android.R.attr.state_pressed;
			int radius = ScreenUtil.dpToPx(8);
			int[] bgColor = new int[] {Color.WHITE, ContextCompat.getColor(requireActivity(), R.color.g5)};
			// 根据设置的listener显示按钮，并设置相应的背景(弹框居中显示时支持圆角背景)
			Drawable contentDrawable = ViewBgUtil.getDrawable(Color.WHITE, new float[] {radius, radius, radius, radius, 0f, 0f, 0f, 0f});
			if (isClickSelect) {
				// 点击直接选择时不需要显示底部按钮
				horizontalView.setVisibility(View.INVISIBLE);
				buttonLayout.setVisibility(View.GONE);
				ViewBgUtil.setShapeBg(contentLayout, Color.WHITE, radius);
			} else if (!TextUtils.isEmpty(this.cancelText) && !TextUtils.isEmpty(this.okText)) {
				ViewBgUtil.setSelectorBg(cancelText, state, bgColor,
						new float[] {0 , 0, 0, 0, 0, 0, radius, radius});
				ViewBgUtil.setSelectorBg(okText, state, bgColor,
						new float[] {0 , 0, 0, 0, radius, radius, 0, 0});
				ViewCompat.setBackground(contentLayout, contentDrawable);
			} else if (!TextUtils.isEmpty(this.cancelText)) {
				gapLine.setVisibility(View.GONE);
				okText.setVisibility(View.GONE);
				ViewBgUtil.setSelectorBg(cancelText, state, bgColor,
						new float[] {0 , 0, 0, 0, radius, radius, radius, radius});
				ViewCompat.setBackground(contentLayout, contentDrawable);
			} else if (!TextUtils.isEmpty(this.okText)) {
				gapLine.setVisibility(View.GONE);
				cancelText.setVisibility(View.GONE);
				ViewBgUtil.setSelectorBg(okText, state, bgColor,
						new float[] {0 , 0, 0, 0, radius, radius, radius, radius});
				ViewCompat.setBackground(contentLayout, contentDrawable);
			} else {
				horizontalView.setVisibility(View.INVISIBLE);
				buttonLayout.setVisibility(View.GONE);
				ViewBgUtil.setShapeBg(contentLayout, Color.WHITE, radius);
			}
		}

		if (items != null && items.length > 0) {
			int visibleItemMaxCount = 4;
			if (items.length > visibleItemMaxCount) {
				LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) contentList.getLayoutParams();
				int itemHeight = 56;
				params.height = ScreenUtil.dpToPx(itemHeight) * visibleItemMaxCount;
				contentList.setLayoutParams(params);
			}
			BaseAdapter adapter;
			if (isSingle) {
				adapter = createSingleChooseAdapter();
			} else {
				adapter = createMultiChooseDataAdapter();
			}
			contentList.setAdapter(adapter);
		}

		cancelText.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (cancelListener != null) {
					cancelListener.onClick(null);
				}
				dismiss();
			}
		});
		okText.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (okListener != null) {
					okListener.onClick(null);
				}
				dismiss();
			}
		});
	}

	@Override
	public void initListener() {

	}

	@Override
	public void bindDataForView() {

	}

	/**
	 * 创建单选适配器
	 * @return
	 */
	private BaseAdapter createSingleChooseAdapter() {
		return new BaseListAdapter<String>(getContext(), items, R.layout.dialog_item_single_choose_layout) {

			@Override
			public void convert(ViewHolder holder, String paramT, final int position) {
				holder.setText(R.id.item_text, paramT);
				final RadioButton radioButton = holder.getView(R.id.radio_button);
				if (checkedItemIndex == position) {
					radioButton.setChecked(true);
					selectedRadioButton = radioButton;
				} else {
					radioButton.setChecked(false);
				}

				View.OnClickListener listener = new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if (position != checkedItemIndex) {
							if (selectedRadioButton != null) {
								selectedRadioButton.setChecked(false);
							}
							radioButton.setChecked(true);
							selectedRadioButton = radioButton;
							checkedItemIndex = position;
							if (isClickSelect && okListener != null) {
								okListener.onClick(v);
							}
						} else {
							radioButton.setChecked(false);
						}

						if (isClickSelect) {
							if (okListener != null) {
								okListener.onClick(v);
							}
							dismiss();
						}

					}
				};
				holder.getContentView().setOnClickListener(listener);
				radioButton.setOnClickListener(listener);
			}
		};
	}

	/**
	 * 创建多选适配器
	 * @return
	 */
	private BaseAdapter createMultiChooseDataAdapter() {
		return new BaseListAdapter<String>(getContext(), items, R.layout.dialog_item_multi_choose_layout) {

			@Override
			public void convert(final ViewHolder holder, String paramT, final int position) {
				holder.setText(R.id.item_text, paramT);
				final CheckBox checkBox = holder.getView(R.id.checkbox);
				if (checkedIndexList.contains(position)) {
					checkBox.setChecked(true);
				} else {
					checkBox.setChecked(false);
				}
				holder.getContentView().setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						checkBox.setChecked(!checkBox.isChecked());
						if (checkBox.isChecked()) {
							checkedIndexList.add(position);
						} else {
							checkedIndexList.remove(position);
						}
					}
				});
				checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						if (isChecked) {
							checkedIndexList.add(position);
						} else {
							checkedIndexList.remove(position);
						}
					}
				});
			}
		};
	}
}
