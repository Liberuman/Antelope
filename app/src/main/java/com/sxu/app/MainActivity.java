package com.sxu.app;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/*******************************************************************************
 *
 *
 * @author: Freeman
 *
 * @date: 2020/9/22
 *
 * Copyright: all rights reserved by Freeman.
 *******************************************************************************/
public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TextView textView = new TextView(this);
		textView.setText("Hello");
		setContentView(textView);
	}
}
