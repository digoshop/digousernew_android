package com.digoshop.app.module.home.model;

public class AuctionInfo {
	//竞拍物品名字
	private String pna;
	//物品id
	private String pid;
	//竞拍商品图片
    private String ppi;
    //竞拍开始时间
    private long apsd;

	public long getNte() {
		return nte;
	}

	public void setNte(long nte) {
		this.nte = nte;
	}

	public long getApsd() {
		return apsd;
	}

	public void setApsd(long apsd) {
		this.apsd = apsd;
	}

	public long getAped() {
		return aped;
	}

	public void setAped(long aped) {
		this.aped = aped;
	}

	//结束时间
	private long aped;
	//系统当前时间
	private long nte;




    
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getPpi() {
		return ppi;
	}
	public void setPpi(String ppi) {
		this.ppi = ppi;
	}
	public String getPna() {
		return pna;
	}
	public void setPna(String pna) {
		this.pna = pna;
	}
    
}
