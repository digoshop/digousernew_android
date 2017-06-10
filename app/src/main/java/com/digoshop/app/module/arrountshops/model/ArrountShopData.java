package com.digoshop.app.module.arrountshops.model;

import java.util.List;

public class ArrountShopData {
	private String goodCount;
	private ShopInfoBean shopInfo;
	public ShopInfoBean getShopInfo() {
		return shopInfo;
	}

	public void setShopInfo(ShopInfoBean shopInfo) {
		this.shopInfo = shopInfo;
	}

	private List<OperateInfoBean> operateInfo;

	public List<OperateInfoBean> getOperateInfo() {
		return operateInfo;
	}

	public void setOperateInfo(List<OperateInfoBean> operateInfo) {
		this.operateInfo = operateInfo;
	}

	public String getGoodCount() {
		return goodCount;
	}

	public void setGoodCount(String goodCount) {
		this.goodCount = goodCount;
	}

 


}
