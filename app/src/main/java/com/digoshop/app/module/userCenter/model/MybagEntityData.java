package com.digoshop.app.module.userCenter.model;

import java.util.ArrayList;

/**
 * @author Administrator
 *
 */
public class MybagEntityData {
	public ArrayList<MybagEntity> getMybagEntities() {
		return mybagEntities;
	}

	public void setMybagEntities(ArrayList<MybagEntity> mybagEntities) {
		this.mybagEntities = mybagEntities;
	}

//	"receive":0,  已结算  "expired":10,  已过期
//
//
//	"notReceive":0  未结算
	public String getReceive() {
		return receive;
	}

	public void setReceive(String receive) {
		this.receive = receive;
	}

	public String getExpired() {
		return expired;
	}

	public void setExpired(String expired) {
		this.expired = expired;
	}

	public String getNotReceive() {
		return notReceive;
	}

	public void setNotReceive(String notReceive) {
		this.notReceive = notReceive;
	}

	private ArrayList<MybagEntity> mybagEntities;
	private String receive;
	private String expired;
	private String notReceive;

}
