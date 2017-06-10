package com.digoshop.app.module.home.model;

import java.util.ArrayList;

public class HomeInfo {
	private AuctionInfo auctionInfo;
	private  ArrayList<HomeRecomFavorite> homeRecomFavorites;
	public ArrayList<HomeRecomFavorite> getHomeRecomFavorites() {
		return homeRecomFavorites;
	}
	public void setHomeRecomFavorites(ArrayList<HomeRecomFavorite> homeRecomFavorites) {
		this.homeRecomFavorites = homeRecomFavorites;
	}
	public AuctionInfo getAuctionInfo() {
		return auctionInfo;
	}
	public void setAuctionInfo(AuctionInfo auctionInfo) {
		this.auctionInfo = auctionInfo;
	}
	private ArrayList<ShopDetailInfo> shopProInfos;
	private ArrayList<ShopDetailInfo> shopSerInfos;

	public ArrayList<ShopDetailInfo> getShopsaleInfos() {
		return shopsaleInfos;
	}

	public void setShopsaleInfos(ArrayList<ShopDetailInfo> shopsaleInfos) {
		this.shopsaleInfos = shopsaleInfos;
	}

	private ArrayList<ShopDetailInfo> shopsaleInfos;

	public ArrayList<ShopDetailInfo> getShopRebate() {
		return shopRebate;
	}

	public void setShopRebate(ArrayList<ShopDetailInfo> shopRebate) {
		this.shopRebate = shopRebate;
	}

	public ArrayList<ShopDetailInfo> getShopGoos() {
		return shopGoos;
	}

	public void setShopGoos(ArrayList<ShopDetailInfo> shopGoos) {
		this.shopGoos = shopGoos;
	}

	private ArrayList<ShopDetailInfo> shopRebate;
	private ArrayList<ShopDetailInfo> shopGoos;
	public ArrayList<ShopDetailInfo> getShopProInfos() {
		return shopProInfos;
	}
	public void setShopProInfos(ArrayList<ShopDetailInfo> shopProInfos) {
		this.shopProInfos = shopProInfos;
	}
	public ArrayList<ShopDetailInfo> getShopSerInfos() {
		return shopSerInfos;
	}
	public void setShopSerInfos(ArrayList<ShopDetailInfo> shopSerInfos) {
		this.shopSerInfos = shopSerInfos;
	}
 
	
}
