package com.sxu.baselibrary.datasource.http.impl.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/*******************************************************************************
 * 将null转换为空字符串
 *
 * @author: Freeman
 *
 * @date: 2020/6/5
 *
 * Copyright: all rights reserved by Freeman.
 *******************************************************************************/
public class StringTypeAdapter extends TypeAdapter<String> {

	@Override
	public String read(JsonReader in) throws IOException {
		if (in.peek() != JsonToken.STRING) {
			in.skipValue();
			return "";
		}

		return in.nextString();
	}
	@Override
	public void write(JsonWriter out, String value) throws IOException {
		out.value(value);
	}
}
