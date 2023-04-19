package com.sxu.baselibrary.datasource.http.impl.gson;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/*******************************************************************************
 * 创建列表类型的TypeAdapter
 *
 * @author: Freeman
 *
 * @date: 2020/6/5
 *
 * Copyright: all rights reserved by Freeman.
 *******************************************************************************/
public class ListTypeAdapterFactory implements TypeAdapterFactory {

	@Override
	public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
		Class<?> rawType = typeToken.getRawType();
		if (!Collection.class.isAssignableFrom(rawType)) {
			return null;
		}

		Type elementType = $Gson$Types.getCollectionElementType(typeToken.getType(), rawType);
		TypeAdapter<?> elementTypeAdapter = gson.getAdapter(TypeToken.get(elementType));
		return new ListTypeAdapter(rawType, elementTypeAdapter);
	}

	/**
	 * 列表类型的TypeAdapter
	 * @param <E>
	 */
	private static final class ListTypeAdapter<E> extends TypeAdapter<List<E>> {

		private Class<? extends List<E>> rawType;
		private TypeAdapter<E> elementTypeAdapter;
		public ListTypeAdapter(Class<? extends List<E>> rawType, TypeAdapter<E> elementTypeAdapter) {
			this.rawType = rawType;
			this.elementTypeAdapter = elementTypeAdapter;
		}

		@Override
		public List<E> read(JsonReader in) throws IOException {
			if (in.peek() != JsonToken.BEGIN_ARRAY) {
				in.skipValue();
				return Collections.EMPTY_LIST;
			}

			try {
				List<E> collection = rawType.newInstance();
				in.beginArray();
				while (in.hasNext()) {
					E instance = elementTypeAdapter.read(in);
					collection.add(instance);
				}
				in.endArray();
				return collection;
			} catch (Exception e) {
				e.printStackTrace(System.out);
			}

			return Collections.EMPTY_LIST;
		}

		@Override public void write(JsonWriter out, List<E> collection) throws IOException {
			if (collection == null) {
				out.nullValue();
				return;
			}

			out.beginArray();
			for (E element : collection) {
				elementTypeAdapter.write(out, element);
			}
			out.endArray();
		}
	}
}
