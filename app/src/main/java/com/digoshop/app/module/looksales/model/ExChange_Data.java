package com.digoshop.app.module.looksales.model;

import java.util.List;

public class ExChange_Data {

	private String intg;
	private List<ExChangeBean> exchangeProductList;

	public void setIntg(String intg) {
		this.intg = intg;
	}

	public String getIntg() {
		return this.intg;
	}

	public void setExchangeProductList(List<ExChangeBean> exchangeProductList) {
		this.exchangeProductList = exchangeProductList;
	}

	public List<ExChangeBean> getExchangeProductList() {
		return this.exchangeProductList;
	}

}
