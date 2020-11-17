package com.sxu.basecomponent.adapter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.sxu.baselibrary.commonutils.LogUtil;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

/*******************************************************************************
 *
 *
 * @author: Freeman
 *
 * @date: 2020-02-23
 *
 * Copyright: all rights reserved by Freeman.
 *******************************************************************************/
public class CustomViewDivider extends RecyclerView.ItemDecoration {

	private Drawable mDivider;

	public CustomViewDivider(Drawable drawable) {
		this.mDivider = drawable;
	}

	@Override
	public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
		super.getItemOffsets(outRect, view, parent, state);
		outRect.set(0, 0, mDivider.getIntrinsicWidth(), mDivider.getIntrinsicHeight());
	}
}
