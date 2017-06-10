package com.digoshop.app.module.looksales.model;

import java.util.ArrayList;

public class ProductInfoDto {
	public String getPppr() {
		return pppr;
	}

	public void setPppr(String pppr) {
		this.pppr = pppr;
	}

	private String pppr;
	public String getPasa() {
		return pasa;
	}

	public void setPasa(String pasa) {
		this.pasa = pasa;
	}

	private String pasa;
	public String getEnu() {
		return enu;
	}

	public void setEnu(String enu) {
		this.enu = enu;
	}

	private String enu;
	public long getNte() {
		return nte;
	}

	public void setNte(long nte) {
		this.nte = nte;
	}

	public long getEped() {
		return eped;
	}

	public void setEped(long eped) {
		this.eped = eped;
	}

	public long getEpsd() {
		return epsd;
	}

	public void setEpsd(long epsd) {
		this.epsd = epsd;
	}

	private long nte;
	public String getEpm() {
		return epm;
	}

	public void setEpm(String epm) {
		this.epm = epm;
	}

	private String epm;
	public String getPattr() {
		return pattr;
	}

	public void setPattr(String pattr) {
		this.pattr = pattr;
	}

	private String pattr;
	public String getElnu() {
		return elnu;
	}

	public void setElnu(String elnu) {
		this.elnu = elnu;
	}

	private String elnu;
	public String getApp() {
		return app;
	}

	public void setApp(String app) {
		this.app = app;
	}

	private String app;
	public String getEps() {
		return eps;
	}

	public void setEps(String eps) {
		this.eps = eps;
	}

	private String eps;
	public ArrayList<String> getUrllist() {
		return urllist;
	}

	public void setUrllist(ArrayList<String> urllist) {
		this.urllist = urllist;
	}

	private ArrayList<String>  urllist;


	public String getLimit() {
		return limit;
	}

	public void setLimit(String limit) {
		this.limit = limit;
	}

	public String getEpnu() {
		return epnu;
	}

	public void setEpnu(String epnu) {
		this.epnu = epnu;
	}

	public String getEplnu() {
		return eplnu;
	}

	public void setEplnu(String eplnu) {
		this.eplnu = eplnu;
	}

	public String getPno() {
		return pno;
	}

	public void setPno(String pno) {
		this.pno = pno;
	}

	public String getBn() {
		return bn;
	}

	public void setBn(String bn) {
		this.bn = bn;
	}

	private String pno;
	private String bn;
	//限量的剩余数量
	private String eplnu;
	//2为限量 1不限量
	private String limit;
    //每天可兑换的数量
	private String epnu;
	// 兑换规则
	private String epd;
	//兑换价格
	private String epp;

	public String getAps() {
		return aps;
	}

	public void setAps(String aps) {
		this.aps = aps;
	}

	private String aps;
	public String getEpp() {
		return epp;
	}

	public void setEpp(String epp) {
		this.epp = epp;
	}

	// 商品类型
	private String mon;
	// 商品图片
	private String ppi;
	// 兑换商品所需币
	private String epg;
	// 商品类型 （1.竞拍 2.换购 3.销售）
	private String pt;
	// 商品id
	private String pid;
	// 商品价格
	private String ppr;
	// 兑换商品价格
	private String epr;
	// 商品名称
	private String pna;
	// 描述
	private String pd;
	// 结束时间 天
	private long eped;


	// 结束时间 天
	private long epsd;
	// 剩余数量
	private String plnu;
	// 商品属性列表
	private ArrayList<Pat> patlist;

	public String getEpd() {
		return epd;
	}

	public void setEpd(String epd) {
		this.epd = epd;
	}

	public String getMon() {
		return mon;
	}

	public void setMon(String mon) {
		this.mon = mon;
	}

	public ArrayList<Pat> getPatlist() {
		return patlist;
	}

	public void setPatlist(ArrayList<Pat> patlist) {
		this.patlist = patlist;
	}

	public String getPd() {
		return pd;
	}

	public void setPd(String pd) {
		this.pd = pd;
	}


	public String getPlnu() {
		return plnu;
	}

	public void setPlnu(String plnu) {
		this.plnu = plnu;
	}

	public String getPpi() {
		return ppi;
	}

	public void setPpi(String ppi) {
		this.ppi = ppi;
	}

	public String getEpg() {
		return epg;
	}

	public void setEpg(String epg) {
		this.epg = epg;
	}

	public String getPt() {
		return pt;
	}

	public void setPt(String pt) {
		this.pt = pt;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getPpr() {
		return ppr;
	}

	public void setPpr(String ppr) {
		this.ppr = ppr;
	}

	public String getEpr() {
		return epr;
	}

	public void setEpr(String epr) {
		this.epr = epr;
	}

	public String getPna() {
		return pna;
	}

	public void setPna(String pna) {
		this.pna = pna;
	}
}
