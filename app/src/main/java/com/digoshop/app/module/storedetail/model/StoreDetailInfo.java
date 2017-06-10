package com.digoshop.app.module.storedetail.model;

import com.digoshop.app.module.home.model.NewsBeean;

import java.util.ArrayList;

public class StoreDetailInfo {
	public String getS_mnid() {
		return s_mnid;
	}

	public void setS_mnid(String s_mnid) {
		this.s_mnid = s_mnid;
	}

	public String getS_mnti() {
		return s_mnti;
	}

	public void setS_mnti(String s_mnti) {
		this.s_mnti = s_mnti;
	}

	public String getS_mba() {
		return s_mba;
	}

	public void setS_mba(String s_mba) {
		this.s_mba = s_mba;
	}

	public String getS_open_time() {
		return s_open_time;
	}

	public void setS_open_time(String s_open_time) {
		this.s_open_time = s_open_time;
	}

	public String getS_tel() {
		return s_tel;
	}

	public void setS_tel(String s_tel) {
		this.s_tel = s_tel;
	}

	private String s_mnid;
	private String s_mnti;
	private String s_mba;
	private String s_open_time;
	private String s_tel;
	public String getS_name() {
		return s_name;
	}

	public void setS_name(String s_name) {
		this.s_name = s_name;
	}

	public String getS_mid() {
		return s_mid;
	}

	public void setS_mid(String s_mid) {
		this.s_mid = s_mid;
	}

	public String getS_logo() {
		return s_logo;
	}

	public void setS_logo(String s_logo) {
		this.s_logo = s_logo;
	}

	private String s_name;
	private String s_mid;
	private String s_logo;
	// 该商场的所有商铺数量
	private String total;
	// 楼层列表
	ArrayList<FloorBean> floorBeans;

	// 商场概况部分
	private Merchant merchant;
	// 商场活动
	private NewsBeean newsBeean;

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public NewsBeean getNewsBeean() {
		return newsBeean;
	}

	public void setNewsBeean(NewsBeean newsBeean) {
		this.newsBeean = newsBeean;
	}

	public Merchant getMerchant() {
		return merchant;
	}

	public void setMerchant(Merchant merchant) {
		this.merchant = merchant;
	}

	public ArrayList<FloorBean> getFloorBeans() {
		return floorBeans;
	}

	public void setFloorBeans(ArrayList<FloorBean> floorBeans) {
		this.floorBeans = floorBeans;
	}
}
