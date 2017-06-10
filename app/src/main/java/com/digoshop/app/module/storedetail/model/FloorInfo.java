package com.digoshop.app.module.storedetail.model;

public class FloorInfo {
	// 层数
	private String mfln;
	// "化妆品", 每层的类型
	private String mfn;
	// 楼层符号
	private String mfltt;
	// 楼层代码
	private String mflid;

	public String getMfln() {
		return mfln;
	}

	public void setMfln(String mfln) {
		this.mfln = mfln;
	}

	public String getMfn() {
		return mfn;
	}

	public void setMfn(String mfn) {
		this.mfn = mfn;
	}

	public String getMfltt() {
		return mfltt;
	}

	public void setMfltt(String mfltt) {
		this.mfltt = mfltt;
	}

	public String getMflid() {
		return mflid;
	}

	public void setMflid(String mflid) {
		this.mflid = mflid;
	}
}
