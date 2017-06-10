package com.digoshop.app.module.userCenter.model;

import java.util.ArrayList;

public class CurlistEntity {
	public String getMst() {
		return mst;
	}

	public void setMst(String mst) {
		this.mst = mst;
	}

	private String mst;//1未标记  2已标记
	public ArrayList<String> getHuifugoodimgs() {
		return huifugoodimgs;
	}

	public void setHuifugoodimgs(ArrayList<String> huifugoodimgs) {
		this.huifugoodimgs = huifugoodimgs;
	}

	private ArrayList<String>  huifugoodimgs;
	private String id;
	private String sid;
	private String sn;
	private String urls;
	private String scov;
	private String rst;
	private String rt;
	private String sms;
	private String cid;
	private String rc;

	public CurlistEntity() {
		super();
	}

	public CurlistEntity(String id, String sid, String sn, String urls, String scov, String rst, String rt, String sms,
			String cid, String rc) {
		super();
		this.id = id;
		this.sid = sid;
		this.sn = sn;
		this.urls = urls;
		this.scov = scov;
		this.rst = rst;
		this.rt = rt;
		this.sms = sms;
		this.cid = cid;
		this.rc = rc;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getUrls() {
		return urls;
	}

	public void setUrls(String urls) {
		this.urls = urls;
	}

	public String getScov() {
		return scov;
	}

	public void setScov(String scov) {
		this.scov = scov;
	}

	public String getRst() {
		return rst;
	}

	public void setRst(String rst) {
		this.rst = rst;
	}

	public String getRt() {
		return rt;
	}

	public void setRt(String rt) {
		this.rt = rt;
	}

	public String getSms() {
		return sms;
	}

	public void setSms(String sms) {
		this.sms = sms;
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
}
