package com.sxu.basecomponent.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/*******************************************************************************
 * Description: RecyclerView的通用适配器
 *
 * Author: Freeman
 *
 * Date: 2018/9/11
 *
 * Copyright: all rights reserved by Freeman.
 *******************************************************************************/
public abstract class BaseCommonRecyclerAdapter<T>
		extends RecyclerView.Adapter<BaseCommonRecyclerAdapter.RecyclerViewHolder> {

	private List<T> data;
	private int layoutId;

	public BaseCommonRecyclerAdapter(List<T> data, @LayoutRes int layoutId) {
		this.data = data;
		this.layoutId = layoutId;
	}

	@NonNull
	@Override
	public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		if (viewType == 0) {
			return new RecyclerViewHolder(LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false));
		} else {
			return onCreateCustomViewHolder(parent, viewType);
		}
	}

	@Override
	public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
		convert(holder, data.get(position), position);
	}

	@Override
	public int getItemCount() {
		return data.size();
	}

	protected RecyclerViewHolder onCreateCustomViewHolder(@NonNull ViewGroup parent, int viewType) {
		return null;
	}

	public void addItem(T item) {
		data.add(item);
		notifyItemInserted(data.size() - 1);
	}

	public void addItem(T item, int position) {
		data.add(position, item);
		notifyItemInserted(position);
	}

	public void deleteItem(int position) {
		data.remove(position);
		notifyItemRemoved(position);
	}

	public void updateItem(T item, int position) {
		data.set(position, item);
		notifyItemChanged(position);
	}

	/**
	 *  Adapter中数据的填充过程
	 * @param viewHolder
	 * @param itemData
	 * @param position
	 */
	public abstract void convert(RecyclerViewHolder viewHolder, T itemData, int position);

	public static class RecyclerViewHolder extends RecyclerView.ViewHolder {

		private ViewHolder viewHolder;

		public RecyclerViewHolder(View itemView) {
			super(itemView);
			viewHolder = ViewHolder.getInstance(itemView);
		}

		public <T extends View> T getView(@IdRes int resId) {
			return viewHolder.getView(resId);
		}

		public void setText(@IdRes int resId, String text) {
			viewHolder.setText(resId, text);
		}

		public void setTextColor(@IdRes int resId, int textColor) {
			viewHolder.setTextColor(resId, textColor);
		}

		public void setImageResource(@IdRes int resId, @DrawableRes int drawableResId) {
			viewHolder.setImageResource(resId, drawableResId);
		}

		public View getContentView() {
			return viewHolder.getContentView();
		}

		public void setVisible(@IdRes int resId, int value) {
			viewHolder.setVisible(resId, value);
		}

		public void setVisible(@IdRes int resId, boolean visibility) {
			viewHolder.setVisible(resId, visibility);
		}

		public void setLayoutParams(@IdRes int viewId, ViewGroup.LayoutParams layoutParams){
			viewHolder.setLayoutParams(viewId, layoutParams);
		}

		public void setOnClickListener(@IdRes int resId, View.OnClickListener listener) {
			viewHolder.setOnClickListener(resId, listener);
		}

		public void setOnLongClickListener(@IdRes int resId, View.OnLongClickListener listener) {
			viewHolder.setOnLongClickListener(resId, listener);
		}
	}
}
