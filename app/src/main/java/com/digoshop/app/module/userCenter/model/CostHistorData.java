package com.digoshop.app.module.userCenter.model;

import java.util.ArrayList;

public class CostHistorData {
	public ArrayList<NumBean> getNumbeans() {
		return numbeans;
	}

	public void setNumbeans(ArrayList<NumBean> numbeans) {
		this.numbeans = numbeans;
	}

	private ArrayList<NumBean> numbeans;
	private ArrayList<ConsumerSettlementrecords> arrayList;
	private String total;

	public String getMin_id() {
		return min_id;
	}

	public void setMin_id(String min_id) {
		this.min_id = min_id;
	}

	private String min_id;

	public String getMax_id() {
		return max_id;
	}

	public void setMax_id(String max_id) {
		this.max_id = max_id;
	}

	private String max_id;
	public ArrayList<ConsumerSettlementrecords> getArrayList() {
		return arrayList;
	}

	public void setArrayList(ArrayList<ConsumerSettlementrecords> arrayList) {
		this.arrayList = arrayList;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

}
