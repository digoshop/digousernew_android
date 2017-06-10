package com.digoshop.app.module.introduce;


public class Product {
	private String brand;

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	private String color;
	private String size;
	private String num;

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	private String price;
	private String disprice;
	private String productname;

	public String getProductname() {
		return productname;
	}

	public void setProductname(String productname) {
		this.productname = productname;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getDisprice() {
		return disprice;
	}

	public void setDisprice(String disprice) {
		this.disprice = disprice;
	}

	/*
	 * 商场名字
	 */
	private String marketName;

	/*
	 * 倒计时间
	 */
	private String countDown;

	/*
	 * 列表图片地址
	 */
	private int itemPicUrl;

	/*
	 * 商场位置
	 */
	private String marketGps;

	/*
	 * 商场距离用户距离
	 */
	private String marketLbsUser;

	public String getMarketName() {
		return marketName;
	}

	public void setMarketName(String marketName) {
		this.marketName = marketName;
	}

	public String getCountDown() {
		return countDown;
	}

	public void setCountDown(String countDown) {
		this.countDown = countDown;
	}

	public int getItemPicUrl() {
		return itemPicUrl;
	}

	public void setItemPicUrl(int itemPicUrl) {
		this.itemPicUrl = itemPicUrl;
	}

	public String getMarketGps() {
		return marketGps;
	}

	public void setMarketGps(String marketGps) {
		this.marketGps = marketGps;
	}

	public String getMarketLbsUser() {
		return marketLbsUser;
	}

	public void setMarketLbsUser(String marketLbsUser) {
		this.marketLbsUser = marketLbsUser;
	}

}
