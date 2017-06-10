package com.digoshop.app.module.userCenter.model;

public class MyLikesEntity {
	private String total;
	private String invite;
	private String uid;
	private String relation_id;
	private String target_info;
	private String open_time;
	private String sid;
	private String news;
	private String product;

	public String getNews() {
		return news;
	}

	public void setNews(String news) {
		this.news = news;
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

	private String coupon;

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	private double distance;
	private String cover;
	private String address;
	private String operate;
	private String name;
	private String goods;
	private String contact;
	private String create_time;
	private String tid;
    private String isdetele;

	public String getStag() {
		return stag;
	}

	public void setStag(String stag) {
		this.stag = stag;
	}

	private String stag;
	public String getIsdetele() {
		return isdetele;
	}

	public void setIsdetele(String isdetele) {
		this.isdetele = isdetele;
	}

	public MyLikesEntity() {
		super();
	}

	public MyLikesEntity(String total, String invite, String uid, String relation_id, String target_info,
			String open_time, String sid, double distance, String cover, String address, String operate, String name,
			String goods, String contact, String create_time, String tid) {
		super();
		this.total = total;
		this.invite = invite;
		this.uid = uid;
		this.relation_id = relation_id;
		this.target_info = target_info;
		this.open_time = open_time;
		this.sid = sid;
		this.distance = distance;
		this.cover = cover;
		this.address = address;
		this.operate = operate;
		this.name = name;
		this.goods = goods;
		this.contact = contact;
		this.create_time = create_time;
		this.tid = tid;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getInvite() {
		return invite;
	}

	public void setInvite(String invite) {
		this.invite = invite;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getRelation_id() {
		return relation_id;
	}

	public void setRelation_id(String relation_id) {
		this.relation_id = relation_id;
	}

	public String getTarget_info() {
		return target_info;
	}

	public void setTarget_info(String target_info) {
		this.target_info = target_info;
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

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}
}
