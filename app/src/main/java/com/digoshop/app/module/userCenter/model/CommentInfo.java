package com.digoshop.app.module.userCenter.model;

import com.digoshop.app.module.home.model.ShopDetailInfo;

import java.util.ArrayList;

public class CommentInfo {
	private String shopnamelin;
	private ShopDetailInfo shop_info; // 被评论店铺的
	private String type;// 0好评 1中评 2 差评
	private String text;// 评论的内容取这个值
	private String create_time;// 用户评论时间
	private ArrayList<String> imgs;// 图片列表
	private String reply_time;//商家回复时间
	private String reply_text;//商家回复内容
    private String nick;//用户昵称
	private String avatar;//用户头像

	public String getReply_time() {
		return reply_time;
	}

	public void setReply_time(String reply_time) {
		this.reply_time = reply_time;
	}

	public String getReply_text() {
		return reply_text;
	}

	public void setReply_text(String reply_text) {
		this.reply_text = reply_text;
	}

	public ArrayList<String> getImgs() {
		return imgs;
	}

	public void setImgs(ArrayList<String> imgs) {
		this.imgs = imgs;
	}
	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public ShopDetailInfo getShopinfo() {
		return shop_info;
	}
	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public void setShopinfo(ShopDetailInfo shop_info) {
		this.shop_info = shop_info;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getCreate_time() {
		return create_time;
	}
	public String getShopnamelin() {
		return shopnamelin;
	}

	public void setShopnamelin(String shopnamelin) {
		this.shopnamelin = shopnamelin;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
}
