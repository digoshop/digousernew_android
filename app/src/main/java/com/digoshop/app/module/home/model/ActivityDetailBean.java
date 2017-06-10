package com.digoshop.app.module.home.model;

import com.digoshop.app.module.shopdetail.model.PointBean;

import java.io.Serializable;

public class ActivityDetailBean implements Serializable{

	public PointBean getPointBean() {
		return pointBean;
	}

	public void setPointBean(PointBean pointBean) {
		this.pointBean = pointBean;
	}

	PointBean pointBean;
	private String mnid ; //活动ID
	private String mnti ; //活动名称
	private String mnp ;   //活动图片
	private String mntt ;// 1 商户活动  2店铺活动
	private String mntid;   // 店铺ID或者商场ID
	private String tname ;//发行店铺的名字
	private String tcover ;//店铺封面图
	private String mnved ;// 活动结束时间
	private String mnvsd ; //活动开始时间


	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	private double distance;// 距离
	private String mnad  ; //活动地点
	private String mnc  ;  //活动介绍
	public String getMnid() {
		return mnid;
	}
	public void setMnid(String mnid) {
		this.mnid = mnid;
	}
	public String getMnti() {
		return mnti;
	}
	public void setMnti(String mnti) {
		this.mnti = mnti;
	}
	public String getMnp() {
		return mnp;
	}
	public void setMnp(String mnp) {
		this.mnp = mnp;
	}
	public String getMntt() {
		return mntt;
	}
	public void setMntt(String mntt) {
		this.mntt = mntt;
	}
	public String getMntid() {
		return mntid;
	}
	public void setMntid(String mntid) {
		this.mntid = mntid;
	}
	public String getTname() {
		return tname;
	}
	public void setTname(String tname) {
		this.tname = tname;
	}
	public String getTcover() {
		return tcover;
	}
	public void setTcover(String tcover) {
		this.tcover = tcover;
	}
	public String getMnved() {
		return mnved;
	}
	public void setMnved(String mnved) {
		this.mnved = mnved;
	}
	public String getMnvsd() {
		return mnvsd;
	}
	public void setMnvsd(String mnvsd) {
		this.mnvsd = mnvsd;
	}
	public String getMnad() {
		return mnad;
	}
	public void setMnad(String mnad) {
		this.mnad = mnad;
	}
	public String getMnc() {
		return mnc;
	}
	public void setMnc(String mnc) {
		this.mnc = mnc;
	}
	 
}
