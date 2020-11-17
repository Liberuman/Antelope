package com.sxu.basecomponent.bean;

import com.sxu.baselibrary.datasource.http.bean.BaseBean;

/*******************************************************************************
 *
 *
 * @author: Freeman
 *
 * @date: 2020/5/26
 *
 * Copyright: all rights reserved by Freeman.
 *******************************************************************************/
public class ItemBaseBean extends BaseBean {

	public int itemType = 0;

	public ItemBaseBean() {

	}

	public ItemBaseBean(int itemType) {
		this.itemType = itemType;
	}
}
