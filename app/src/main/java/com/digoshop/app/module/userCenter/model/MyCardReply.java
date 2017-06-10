package com.digoshop.app.module.userCenter.model;

public class MyCardReply {
	private String total;
	private String eintg;

	public MyCardReply() {
		super();
	}

	public MyCardReply(String total, String eintg) {
		super();
		this.total = total;
		this.eintg = eintg;
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
