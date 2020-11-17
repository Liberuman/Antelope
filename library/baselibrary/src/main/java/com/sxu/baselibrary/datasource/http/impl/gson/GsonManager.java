package com.sxu.baselibrary.datasource.http.impl.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Modifier;

/*******************************************************************************
 *
 *
 * @author: Freeman
 *
 * @date: 2020/6/5
 *
 * Copyright: all rights reserved by Freeman.
 *******************************************************************************/
public class GsonManager {

	public static Gson defaultInstance() {
		Gson gson = new GsonBuilder()
			.registerTypeHierarchyAdapter(Object.class, new CompatibleDeserializer())
			.create();
		return gson;
	}

	public static Gson newInstance() {
		Gson gson = new GsonBuilder()
			.registerTypeAdapter(String.class, new StringTypeAdapter())
			.excludeFieldsWithModifiers(Modifier.TRANSIENT, Modifier.STATIC, Modifier.FINAL)
			.create();
		return gson;
	}
}
