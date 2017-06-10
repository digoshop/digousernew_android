package com.digoshop.app.module.userCenter.model;

/**
 * @author Administrator
 *
 */
public class MybagEntity {
	private String uid;
	private String id;
	private String pat;
	private String intg;
	private String epg;
	private String pt;
	private String pid;
	private String ppr;
	private String epp;
	private String ct;
	private String pna;
	private String sas;
	private String ppi;

	public MybagEntity() {
		super();
	}

	public MybagEntity(String uid, String id, String pat, String intg, String epg, String pt, String pid, String ppr,
			String epp, String ct, String pna, String sas, String ppi) {
		super();
		this.uid = uid;
		this.id = id;
		this.pat = pat;
		this.intg = intg;
		this.epg = epg;
		this.pt = pt;
		this.pid = pid;
		this.ppr = ppr;
		this.epp = epp;
		this.ct = ct;
		this.pna = pna;
		this.sas = sas;
		this.ppi = ppi;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPat() {
		return pat;
	}

	public void setPat(String pat) {
		this.pat = pat;
	}

	public String getIntg() {
		return intg;
	}

	public void setIntg(String intg) {
		this.intg = intg;
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

	public String getEpp() {
		return epp;
	}

	public void setEpp(String epp) {
		this.epp = epp;
	}

	public String getCt() {
		return ct;
	}

	public void setCt(String ct) {
		this.ct = ct;
	}

	public String getPna() {
		return pna;
	}

	public void setPna(String pna) {
		this.pna = pna;
	}

	public String getSas() {
		return sas;
	}

	public void setSas(String sas) {
		this.sas = sas;
	}

	public String getPpi() {
		return ppi;
	}

	public void setPpi(String ppi) {
		this.ppi = ppi;
	}
}
