package com.digoshop.app.module.home.model;

public class ShopInfo {
	private String shop_name;// 名字
	private String shop_type;// 类型
	private String shop_eva;// 好评数
	private String shop_gps;// gps
	private String shop_iv;// 商铺图片
	private String shop_floor;// 商铺楼层
	private String shop_teltimes;// 电话拨打次数

	public String getShop_floor() {
		return shop_floor;
	}

	public void setShop_floor(String shop_floor) {
		this.shop_floor = shop_floor;
	}

	public String getShop_teltimes() {
		return shop_teltimes;
	}

	public void setShop_teltimes(String shop_teltimes) {
		this.shop_teltimes = shop_teltimes;
	}

	public String getShop_iv() {
		return shop_iv;
	}

	public void setShop_iv(String shop_iv) {
		this.shop_iv = shop_iv;
	}

	public String getShop_name() {
		return shop_name;
	}

	public void setShop_name(String shop_name) {
		this.shop_name = shop_name;
	}

	public String getShop_type() {
		return shop_type;
	}

	public void setShop_type(String shop_type) {
		this.shop_type = shop_type;
	}

	public String getShop_eva() {
		return shop_eva;
	}

	public void setShop_eva(String shop_eva) {
		this.shop_eva = shop_eva;
	}

	public String getShop_gps() {
		return shop_gps;
	}

	public void setShop_gps(String shop_gps) {
		this.shop_gps = shop_gps;
	}

}
