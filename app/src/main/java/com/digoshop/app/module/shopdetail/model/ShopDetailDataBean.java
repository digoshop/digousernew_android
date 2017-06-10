package com.digoshop.app.module.shopdetail.model;

import com.digoshop.app.module.storedetail.model.StoreDetailMomInfo;

import java.util.ArrayList;

public class ShopDetailDataBean {
	public ArrayList<String> getUrls() {
		return urls;
	}

	public void setUrls(ArrayList<String> urls) {
		this.urls = urls;
	}

	private ArrayList<String> urls;
	public PointBean getPointBean() {
		return pointBean;
	}

	public void setPointBean(PointBean pointBean) {
		this.pointBean = pointBean;
	}

	private PointBean pointBean;
	// 店铺封面图
	private String scov;
	// 店铺id
	private String sid;
	// 电话列表
	ArrayList<StoreDetailMomInfo> telinfos;
	// 店铺描述
	private String sd;
	// 店铺坐标
	private String sll;
	// 所属商场
	private String smn;
	// 营业时间
	private String sdbt;
	// 店铺地址
	private String sad;
	// 所属楼层
	private String sbna;
	// 店铺名称
	private String sn;
	// ?
	private String icas;
	//销售品类
	private ArrayList<ShopoperateBean> ShopoperateBeans;
	//经营品牌
	private ArrayList<ShopbrandBean> ShopbrandBeans;

	public String getRelation() {
		return relation;
	}

	public void setRelation(String relation) {
		this.relation = relation;
	}

	private String relation;//ture 已关注，false未关注

	public ArrayList<ShopoperateBean> getShopoperateBeans() {
		return ShopoperateBeans;
	}

	public void setShopoperateBeans(ArrayList<ShopoperateBean> shopoperateBeans) {
		ShopoperateBeans = shopoperateBeans;
	}

	public ArrayList<ShopbrandBean> getShopbrandBeans() {
		return ShopbrandBeans;
	}

	public void setShopbrandBeans(ArrayList<ShopbrandBean> shopbrandBeans) {
		ShopbrandBeans = shopbrandBeans;
	}

	public String getScov() {
		return scov;
	}

	public void setScov(String scov) {
		this.scov = scov;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public ArrayList<StoreDetailMomInfo> getTelinfos() {
		return telinfos;
	}

	public void setTelinfos(ArrayList<StoreDetailMomInfo> telinfos) {
		this.telinfos = telinfos;
	}

	public String getSd() {
		return sd;
	}

	public void setSd(String sd) {
		this.sd = sd;
	}

	public String getSll() {
		return sll;
	}

	public void setSll(String sll) {
		this.sll = sll;
	}

	public String getSmn() {
		return smn;
	}

	public void setSmn(String smn) {
		this.smn = smn;
	}

	public String getSdbt() {
		return sdbt;
	}

	public void setSdbt(String sdbt) {
		this.sdbt = sdbt;
	}

	public String getSad() {
		return sad;
	}

	public void setSad(String sad) {
		this.sad = sad;
	}

	public String getSbna() {
		return sbna;
	}

	public void setSbna(String sbna) {
		this.sbna = sbna;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getIcas() {
		return icas;
	}

	public void setIcas(String icas) {
		this.icas = icas;
	}

}
