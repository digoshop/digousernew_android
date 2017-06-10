package com.digoshop.app.module.userCenter.model;

import com.digoshop.app.module.shopdetail.model.ShopCommentData;
import com.digoshop.app.module.shopdetailnew.model.ShopComment;

import java.util.ArrayList;

public class CommentData {
	private ArrayList<ShopCommentData> shopcomments;

	public ArrayList<ShopComment> getShopInfoBeans() {
		return shopInfoBeans;
	}

	public void setShopInfoBeans(ArrayList<ShopComment> shopInfoBeans) {
		this.shopInfoBeans = shopInfoBeans;
	}

	ArrayList<ShopComment> shopInfoBeans;
	public ArrayList<ShopCommentData> getShopcomments() {
		return shopcomments;
	}

	public void setShopcomments(ArrayList<ShopCommentData> shopcomments) {
		this.shopcomments = shopcomments;
	}

	private String total;

	public String getGood() {
		return good;
	}

	public void setGood(String good) {
		this.good = good;
	}

	public String getBad() {
		return bad;
	}

	public void setBad(String bad) {
		this.bad = bad;
	}

	public String getNormal() {
		return normal;
	}

	public void setNormal(String normal) {
		this.normal = normal;
	}

	public String getMin_id() {
		return min_id;
	}

	public void setMin_id(String min_id) {
		this.min_id = min_id;
	}

	public String getMax_id() {
		return max_id;
	}

	public void setMax_id(String max_id) {
		this.max_id = max_id;
	}

	private String good;
	private String bad;
	private String normal;
	private String min_id;
	private String max_id;
	private ArrayList<CommentInfo> commentinfos;

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public ArrayList<CommentInfo> getCommentinfos() {
		return commentinfos;
	}

	public void setCommentinfos(ArrayList<CommentInfo> commentinfos) {
		this.commentinfos = commentinfos;
	}
}
