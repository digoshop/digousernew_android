package com.digoshop.app.module.userCenter.model;

public class MyGiftBean {

	public MyGiftBean(String gifttime, String gifttime_type, String giftstate,
			String imgurl, String giftname, String giftpoint, String giftmoney,
			String giftvalue, String gifttype) {
		super();
		this.gifttime = gifttime;
		this.gifttime_type = gifttime_type;
		this.giftstate = giftstate;
		this.imgurl = imgurl;
		this.giftname = giftname;
		this.giftpoint = giftpoint;
		this.giftmoney = giftmoney;
		this.giftvalue = giftvalue;
		this.gifttype = gifttype;
	}

	public String getGifttime() {
		return gifttime;
	}

	public void setGifttime(String gifttime) {
		this.gifttime = gifttime;
	}

	public String getGifttime_type() {
		return gifttime_type;
	}

	public void setGifttime_type(String gifttime_type) {
		this.gifttime_type = gifttime_type;
	}

	public String getGiftstate() {
		return giftstate;
	}

	public void setGiftstate(String giftstate) {
		this.giftstate = giftstate;
	}

	public String getImgurl() {
		return imgurl;
	}

	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}

	public String getGiftname() {
		return giftname;
	}

	public void setGiftname(String giftname) {
		this.giftname = giftname;
	}

	public String getGiftpoint() {
		return giftpoint;
	}

	public void setGiftpoint(String giftpoint) {
		this.giftpoint = giftpoint;
	}

	public String getGiftmoney() {
		return giftmoney;
	}

	public void setGiftmoney(String giftmoney) {
		this.giftmoney = giftmoney;
	}

	public String getGiftvalue() {
		return giftvalue;
	}

	public void setGiftvalue(String giftvalue) {
		this.giftvalue = giftvalue;
	}

	public String getGifttype() {
		return gifttype;
	}

	public void setGifttype(String gifttype) {
		this.gifttype = gifttype;
	}

	// 时间
	public String gifttime;
	// 领取时间类型
	public String gifttime_type;
	// 状态
	public String giftstate;
	// 图片路径
	public String imgurl;
	// 名字
	public String giftname;
	// 积分
	public String giftpoint;
	// 认购金额
	public String giftmoney;
	// 价值
	public String giftvalue;
	// 卡类型
	public String gifttype;

}
