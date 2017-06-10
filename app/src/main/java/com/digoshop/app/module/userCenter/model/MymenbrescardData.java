package com.digoshop.app.module.userCenter.model;

import java.util.ArrayList;

public class MymenbrescardData {
	private ArrayList<MembreEntity> arrayList;
	private String total;

	public ArrayList<MembreEntity> getArrayList() {
		return arrayList;
	}

	public void setArrayList(ArrayList<MembreEntity> arrayList) {
		this.arrayList = arrayList;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getIntg() {
		return intg;
	}

	public void setIntg(String intg) {
		this.intg = intg;
	}

	private String intg;
}
