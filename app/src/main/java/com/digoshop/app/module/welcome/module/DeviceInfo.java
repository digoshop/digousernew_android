package com.digoshop.app.module.welcome.module;

public class DeviceInfo {
	

	/*
	 * 安装渠道
	 */
	public String channel;
	/*
	 * 程序版本
	 */
	public String app_version;
	/*
	 * 设备类型，(0-phone,1-pad,2-ott)
	 */
	public int type;
	/*
	 * 操作系统，例如Android.4.4.4
	 */
	public String os;
	/*
	 * 设备id，ios从系统获取，android尝试从系统生成，没有则后端分配
	 */
	public String did;
	/*
	 * 手机品牌（英文），比如Huawei等
	 */
	public String brand;
	/*
	 * 型号，比如HUAWEI+MT7-CL00等
	 */
	public String model;
	/*
	 * 屏幕宽度像素个数
	 */
	public int sw;
	/*
	 * 屏幕高度像素个数
	 */
	public int sh;



	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getApp_version() {
		return app_version;
	}

	public void setApp_version(String app_version) {
		this.app_version = app_version;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getOs() {
		return os;
	}

	public void setOs(String os) {
		this.os = os;
	}

	public String getDid() {
		return did;
	}

	public void setDid(String did) {
		this.did = did;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public int getSw() {
		return sw;
	}

	public void setSw(int sw) {
		this.sw = sw;
	}

	public int getSh() {
		return sh;
	}

	public void setSh(int sh) {
		this.sh = sh;
	}

}
