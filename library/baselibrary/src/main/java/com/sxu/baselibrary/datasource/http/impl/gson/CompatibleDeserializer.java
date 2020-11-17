package com.sxu.baselibrary.datasource.http.impl.gson;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;

/*******************************************************************************
 * 过滤json中的空字符串、空数组、对对象
 *
 * @author: Freeman
 *
 * @date: 2020/6/5
 *
 * Copyright: all rights reserved by Freeman.
 *******************************************************************************/
public class CompatibleDeserializer implements JsonDeserializer {

	@Override
	public Object deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		if (json instanceof JsonObject) {

			Set<Map.Entry<String, JsonElement>> elementSet = ((JsonObject) json).entrySet();
			for (Map.Entry<String, JsonElement> entry : elementSet) {
				JsonElement value = entry.getValue();
				if (value == null) {
					continue;
				}

				String realValue = value.toString();
				if ("\"\"".equals(realValue) || "[]".equals(realValue) || "{}".equals(realValue)) {
					entry.setValue(JsonNull.INSTANCE);
				}
			}
		}

		return GsonManager.newInstance().fromJson(json, typeOfT);
	}
}
