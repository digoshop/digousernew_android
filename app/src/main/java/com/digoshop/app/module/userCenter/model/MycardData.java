package com.digoshop.app.module.userCenter.model;

import java.util.ArrayList;

public class MycardData {
	private ArrayList<MycardBagEntity> arrayList;
	private String total;
	private String eintg;
	private String data;
	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
	
	public ArrayList<MycardBagEntity> getArrayList() {
		return arrayList;
	}

	public void setArrayList(ArrayList<MycardBagEntity> arrayList) {
		this.arrayList = arrayList;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getEintg() {
		return eintg;
	}

	public void setEintg(String eintg) {
		this.eintg = eintg;
	}
}
