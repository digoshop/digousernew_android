package com.digoshop.app.module.customServices.model;

import java.io.Serializable;
import java.util.ArrayList;

public class CategoryChooseBean implements Serializable{
	// 类别图片
	private String icon;
	// 类别名字
	private String name;
	private String pid;
	// 类别id
	private String moid;

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	private String sid;
	private ArrayList<ChildernBean> childderlis;

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getMoid() {
		return moid;
	}

	public void setMoid(String moid) {
		this.moid = moid;
	}

	public ArrayList<ChildernBean> getChildderlis() {
		return childderlis;
	}

	public void setChildderlis(ArrayList<ChildernBean> childderlis) {
		this.childderlis = childderlis;
	}



}
