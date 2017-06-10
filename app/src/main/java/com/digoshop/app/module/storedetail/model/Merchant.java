package com.digoshop.app.module.storedetail.model;

import java.util.ArrayList;

public class Merchant {
	// 商场名字
	private String mn;
	// 商场描述
	private String md;
	// 商场地址
	private String madd;
	// 商场id
	private String mid;
	// 营业时间
	private String mdbt;
	// 商场坐标
	private String[] gpsarray;
	// 滚动大图
	private String[] imgarray;
	// 商场电话
	private ArrayList<StoreDetailMomInfo> telinfos;
	//商场坐标
	private StoredetailMllInfo gpsinfos;
	//商场大图
	private ArrayList<StoredetailMaInfo> urlinfos;
	 

	public StoredetailMllInfo getGpsinfos() {
		return gpsinfos;
	}

	public void setGpsinfos(StoredetailMllInfo gpsinfos) {
		this.gpsinfos = gpsinfos;
	}

	public ArrayList<StoredetailMaInfo> getUrlinfos() {
		return urlinfos;
	}

	public void setUrlinfos(ArrayList<StoredetailMaInfo> urlinfos) {
		this.urlinfos = urlinfos;
	}

	public ArrayList<StoreDetailMomInfo> getTelinfos() {
		return telinfos;
	}

	public void setTelinfos(ArrayList<StoreDetailMomInfo> telinfos) {
		this.telinfos = telinfos;
	}

	private String[] momarray;

	public String[] getGpsarray() {
		return gpsarray;
	}

	public void setGpsarray(String[] gpsarray) {
		this.gpsarray = gpsarray;
	}

	public String[] getImgarray() {
		return imgarray;
	}

	public void setImgarray(String[] imgarray) {
		this.imgarray = imgarray;
	}

	public String[] getMomarray() {
		return momarray;
	}

	public void setMomarray(String[] momarray) {
		this.momarray = momarray;
	}

	public String getMn() {
		return mn;
	}

	public void setMn(String mn) {
		this.mn = mn;
	}

	public String getMd() {
		return md;
	}

	public void setMd(String md) {
		this.md = md;
	}

	public String getMadd() {
		return madd;
	}

	public void setMadd(String madd) {
		this.madd = madd;
	}

	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

	public String getMdbt() {
		return mdbt;
	}

	public void setMdbt(String mdbt) {
		this.mdbt = mdbt;
	}
}
