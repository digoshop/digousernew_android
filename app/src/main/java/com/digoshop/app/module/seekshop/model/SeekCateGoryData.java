package com.digoshop.app.module.seekshop.model;

import java.util.ArrayList;

import com.digoshop.app.module.customServices.model.CategoryChooseBean;
import com.digoshop.app.module.home.model.ActivityDetailBean;

public class SeekCateGoryData {
	// 三级类别list
	private ArrayList<CategoryChooseBean> categorychoosbeans;
	// 二级类别名字
	private String name;
	// 二级类别id
	private String moid;
	private ArrayList<ActivityDetailBean> atydetails;

	public ArrayList<ActivityDetailBean> getAtydetails() {
		return atydetails;
	}

	public void setAtydetails(ArrayList<ActivityDetailBean> atydetails) {
		this.atydetails = atydetails;
	}

	public ArrayList<CategoryChooseBean> getCategorychoosbeans() {
		return categorychoosbeans;
	}

	public void setCategorychoosbeans(ArrayList<CategoryChooseBean> categorychoosbeans) {
		this.categorychoosbeans = categorychoosbeans;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMoid() {
		return moid;
	}

	public void setMoid(String moid) {
		this.moid = moid;
	}
}
