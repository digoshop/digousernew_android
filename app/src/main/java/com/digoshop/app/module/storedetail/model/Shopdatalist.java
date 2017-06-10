package com.digoshop.app.module.storedetail.model;

import java.util.ArrayList;

import com.digoshop.app.module.arrountshops.model.ShopInfoBean;

public class Shopdatalist {
    private String total;
    ArrayList<ShopInfoBean> shops;
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	public ArrayList<ShopInfoBean> getShops() {
		return shops;
	}
	public void setShops(ArrayList<ShopInfoBean> shops) {
		this.shops = shops;
	}
}
