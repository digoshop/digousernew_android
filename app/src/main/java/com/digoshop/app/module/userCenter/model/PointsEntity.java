package com.digoshop.app.module.userCenter.model;

public class PointsEntity {
	private String uid;
	private String intg;
	private String create_time;
	private String intg_id;
	private String type;

	public PointsEntity() {
		super();
	}

	public PointsEntity(String uid, String intg, String create_time, String intg_id, String type) {
		super();
		this.uid = uid;
		this.intg = intg;
		this.create_time = create_time;
		this.intg_id = intg_id;
		this.type = type;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getIntg() {
		return intg;
	}

	public void setIntg(String intg) {
		this.intg = intg;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public String getIntg_id() {
		return intg_id;
	}

	public void setIntg_id(String intg_id) {
		this.intg_id = intg_id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
