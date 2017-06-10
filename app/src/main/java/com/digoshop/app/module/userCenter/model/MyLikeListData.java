package com.digoshop.app.module.userCenter.model;

import java.util.ArrayList;

public class MyLikeListData {
	private String total;
	private ArrayList<MyLikesEntity> arLikesEntities;

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public ArrayList<MyLikesEntity> getArLikesEntities() {
		return arLikesEntities;
	}

	public void setArLikesEntities(ArrayList<MyLikesEntity> arLikesEntities) {
		this.arLikesEntities = arLikesEntities;
	}

}
