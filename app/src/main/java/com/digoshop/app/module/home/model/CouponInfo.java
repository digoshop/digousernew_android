package com.digoshop.app.module.home.model;

import java.util.List;

public class CouponInfo {
	private String full;
	//开始时间
	private String startTime;
	 //代金金额
	private String amount;

	public String getTname() {
		return tname;
	}

	public void setTname(String tname) {
		this.tname = tname;
	}

	private String tname;//店铺名字
	public double getRebate() {
		return rebate;
	}

	public void setRebate(double rebate) {
		this.rebate = rebate;
	}

	public String getFull() {
		return full;
	}

	public void setFull(String full) {
		this.full = full;
	}

	//折扣券折扣率rebate
	private double rebate;
    //优惠券id
	private String couponBatchId;
    //优惠券类型名称
	private String couponName;
    //优惠券背景图
	private String couponPic;
    //优惠券适用类型
	private List<OperatesInfo> operates;
    //店铺名字
	private String couponDesc;

	public String getCtid() {
		return ctid;
	}

	public void setCtid(String ctid) {
		this.ctid = ctid;
	}

	//结束时间
	private String endTime;
	private String ctid;
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getCouponBatchId() {
		return couponBatchId;
	}
	public void setCouponBatchId(String couponBatchId) {
		this.couponBatchId = couponBatchId;
	}
	public String getCouponName() {
		return couponName;
	}
	public void setCouponName(String couponName) {
		this.couponName = couponName;
	}
	public String getCouponPic() {
		return couponPic;
	}
	public void setCouponPic(String couponPic) {
		this.couponPic = couponPic;
	}
	public List<OperatesInfo> getOperates() {
		return operates;
	}
	public void setOperates(List<OperatesInfo> operates) {
		this.operates = operates;
	}
	public String getCouponDesc() {
		return couponDesc;
	}
	public void setCouponDesc(String couponDesc) {
		this.couponDesc = couponDesc;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
}
