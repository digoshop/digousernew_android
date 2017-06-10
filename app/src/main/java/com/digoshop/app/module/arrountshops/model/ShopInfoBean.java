package com.digoshop.app.module.arrountshops.model;

public class ShopInfoBean {
	public String getSigned() {
		return signed;
	}

	public void setSigned(String signed) {
		this.signed = signed;
	}

	private String signed;
	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getNews() {
		return news;
	}

	public void setNews(String news) {
		this.news = news;
	}

	public String getCoupon() {
		return coupon;
	}

	public void setCoupon(String coupon) {
		this.coupon = coupon;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	//兑换
    private String product;
	//优惠券
	private String coupon;
	//测试活动
	private String news;
	private String time;
	// 店铺ID
	private String sid;
	// 店铺名称
	private String name;
	// 封面图
	private String cover;
	// 地址
	private String address;

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	// 距离 大于0才显示
	private double distance;
	// 好评数
	private String goods;
	// 营业时间
	private String open_time;
	// 经营类型
	private String operate;
	// 联系电话
	private String contact;
	//判断是存在优惠券或活动
	private String stag;
	
	public String getStag() {
		return stag;
	}

	public void setStag(String stag) {
		this.stag = stag;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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




	public String getGoods() {
		return goods;
	}

	public void setGoods(String goods) {
		this.goods = goods;
	}

	public String getOpen_time() {
		return open_time;
	}

	public void setOpen_time(String open_time) {
		this.open_time = open_time;
	}

	public String getOperate() {
		return operate;
	}

	public void setOperate(String operate) {
		this.operate = operate;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

}
