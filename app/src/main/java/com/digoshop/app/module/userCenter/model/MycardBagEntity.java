package com.digoshop.app.module.userCenter.model;

import com.digoshop.app.module.home.model.ShopDetailInfo;

public class MycardBagEntity {
	private String create_time;
	private String intg;
	private String vip_id;
	private String vip_level;
	private String vip_code;
	private String status;
	private ShopDetailInfo shopinfo;

	public ShopDetailInfo getShopinfo() {
		return shopinfo;
	}

	public void setShopinfo(ShopDetailInfo shopinfo) {
		this.shopinfo = shopinfo;
	}

	public MycardBagEntity() {
		super();
	}

	public MycardBagEntity(String name, String sava, String sid, String create_time, String intg, String vip_id,
			String vip_level, String vip_code, String status) {
		super();
		this.create_time = create_time;
		this.intg = intg;
		this.vip_id = vip_id;
		this.vip_level = vip_level;
		this.vip_code = vip_code;
		this.status = status;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public String getIntg() {
		return intg;
	}

	public void setIntg(String intg) {
		this.intg = intg;
	}

	public String getVip_id() {
		return vip_id;
	}

	public void setVip_id(String vip_id) {
		this.vip_id = vip_id;
	}

	public String getVip_level() {
		return vip_level;
	}

	public void setVip_level(String vip_level) {
		this.vip_level = vip_level;
	}

	public String getVip_code() {
		return vip_code;
	}

	public void setVip_code(String vip_code) {
		this.vip_code = vip_code;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
