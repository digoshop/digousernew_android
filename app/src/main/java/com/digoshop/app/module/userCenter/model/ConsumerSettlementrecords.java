package com.digoshop.app.module.userCenter.model;


import com.digoshop.app.module.looksales.model.Pat;

import java.util.ArrayList;

public class ConsumerSettlementrecords {
	private String amount;
	private String sid;
	private String is_comment;
	private String create_time;
	private String eid;
	private String shop_info;
	private String name;

	public ArrayList<Pat> getPats() {
		return pats;
	}

	public void setPats(ArrayList<Pat> pats) {
		this.pats = pats;
	}

	private ArrayList<Pat> pats;
	public String getS_pna() {
		return s_pna;
	}

	public void setS_pna(String s_pna) {
		this.s_pna = s_pna;
	}

	public String getS_ppr() {
		return s_ppr;
	}

	public void setS_ppr(String s_ppr) {
		this.s_ppr = s_ppr;
	}

	public String getS_epp() {
		return s_epp;
	}

	public void setS_epp(String s_epp) {
		this.s_epp = s_epp;
	}

	public String getS_ppi() {
		return s_ppi;
	}

	public void setS_ppi(String s_ppi) {
		this.s_ppi = s_ppi;
	}

	public String getS_pat() {
		return s_pat;
	}

	public void setS_pat(String s_pat) {
		this.s_pat = s_pat;
	}

	private String s_pna;
	private String s_ppr;
	private String s_epp;
	private String s_ppi;
	private String s_pat;
	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	private String logo;

	public ConsumerSettlementrecords() {
		super();
	}

	public ConsumerSettlementrecords(String amount, String sid, String is_comment, String create_time, String eid,
			String shop_info, String name) {
		super();
		this.amount = amount;
		this.sid = sid;
		this.is_comment = is_comment;
		this.create_time = create_time;
		this.eid = eid;
		this.shop_info = shop_info;
		this.name = name;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getIs_comment() {
		return is_comment;
	}

	public void setIs_comment(String is_comment) {
		this.is_comment = is_comment;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public String getEid() {
		return eid;
	}

	public void setEid(String eid) {
		this.eid = eid;
	}

	public String getShop_info() {
		return shop_info;
	}

	public void setShop_info(String shop_info) {
		this.shop_info = shop_info;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Student [amount=" + amount + ", sid=" + sid + ", is_comment=" + is_comment + ", create_time="
				+ create_time + ", eid=" + eid + ", shop_info=" + shop_info + "]";
	}
}
