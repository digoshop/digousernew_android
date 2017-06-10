package com.digoshop.app.module.home.model;

public class ShopDetailInfo {
	public String getSigned() {
		return signed;
	}

	public void setSigned(String signed) {
		this.signed = signed;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getCoupon() {
		return coupon;
	}

	public void setCoupon(String coupon) {
		this.coupon = coupon;
	}

	public String getNews() {
		return news;
	}

	public void setNews(String news) {
		this.news = news;
	}

	private String signed;
	//兑换
	private String product;
	//优惠券
	private String coupon;
	//测试活动
	private String news;
	// 营业时间
	private String open_time;
	// 店铺ID
	private String sid;

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	// 距离 大于0才显示
	private double distance;
	// 封面图
	private String cover;
	// 地址
	private String address;
	// 经营类型
	private String operate;
	// 店铺名称
	private String name;
	// 好评数
	private String goods;
	// 联系电话
	private String contact;
	//会员卡图片
	private String sava;
	public String getSava() {
		return sava;
	}

	public void setSava(String sava) {
		this.sava = sava;
	}

	public String getOpen_time() {
		return open_time;
	}

	public void setOpen_time(String open_time) {
		this.open_time = open_time;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}


	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getOperate() {
		return operate;
	}

	public void setOperate(String operate) {
		this.operate = operate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGoods() {
		return goods;
	}

	public void setGoods(String goods) {
		this.goods = goods;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

}
