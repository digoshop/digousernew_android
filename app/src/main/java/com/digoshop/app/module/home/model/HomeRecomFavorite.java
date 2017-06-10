package com.digoshop.app.module.home.model;

import java.util.List;

public class HomeRecomFavorite {
	private AuctionInfo exchange;

	private List<CouponInfo> coupon ;

	public AuctionInfo getAuctionInfo() {
		return exchange;
	}

	public void setAuctionInfo(AuctionInfo exchange) {
		this.exchange = exchange;
	}

	public List<CouponInfo> getCoupon() {
		return coupon;
	}

	public void setCoupon(List<CouponInfo> coupon) {
		this.coupon = coupon;
	}
	
}
