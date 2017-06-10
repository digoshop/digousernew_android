package com.digoshop.app.module.userCenter.model;


import java.util.ArrayList;

import com.digoshop.app.module.customServices.model.ImgeUrl;

public class MerchanRreplyEntity {
	private String uid;
	private String tn;
	private String desc;
	private String mc;
	private String tp;
	private String iurls;
	private String st;
	private String et;
	private String cid;
	private String rc;
	private String ct;
    private ArrayList<ImgeUrl> imgurls;
	public ArrayList<ImgeUrl> getImgurls() {
		return imgurls;
	}

	public void setImgurls(ArrayList<ImgeUrl> imgurls) {
		this.imgurls = imgurls;
	}

	public MerchanRreplyEntity() {
		super();
	}

	public MerchanRreplyEntity(String uid, String tn, String desc, String mc, String tp, String iurls, String st,
			String et, String cid, String rc, String ct) {
		super();
		this.uid = uid;
		this.tn = tn;
		this.desc = desc;
		this.mc = mc;
		this.tp = tp;
		this.iurls = iurls;
		this.st = st;
		this.et = et;
		this.cid = cid;
		this.rc = rc;
		this.ct = ct;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getTn() {
		return tn;
	}

	public void setTn(String tn) {
		this.tn = tn;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getMc() {
		return mc;
	}

	public void setMc(String mc) {
		this.mc = mc;
	}

	public String getTp() {
		return tp;
	}

	public void setTp(String tp) {
		this.tp = tp;
	}

	public String getIurls() {
		return iurls;
	}

	public void setIurls(String iurls) {
		this.iurls = iurls;
	}

	public String getSt() {
		return st;
	}

	public void setSt(String st) {
		this.st = st;
	}

	public String getEt() {
		return et;
	}

	public void setEt(String et) {
		this.et = et;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getRc() {
		return rc;
	}

	public void setRc(String rc) {
		this.rc = rc;
	}

	public String getCt() {
		return ct;
	}

	public void setCt(String ct) {
		this.ct = ct;
	}

}
