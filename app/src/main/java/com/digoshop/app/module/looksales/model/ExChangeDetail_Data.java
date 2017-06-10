package com.digoshop.app.module.looksales.model;

import com.digoshop.app.module.userCenter.model.Discountcoupons;

import java.util.ArrayList;

public class ExChangeDetail_Data {
	public ArrayList<Discountcoupons> getDiscountcouponses() {
		return discountcouponses;
	}

	public void setDiscountcouponses(ArrayList<Discountcoupons> discountcouponses) {
		this.discountcouponses = discountcouponses;
	}

	private ArrayList<Discountcoupons> discountcouponses;
	private ProductInfoDto productInfoDto;

	public ProductInfoDto getProductInfoDto() {
		return productInfoDto;
	}

	public void setProductInfoDto(ProductInfoDto productInfoDto) {
		this.productInfoDto = productInfoDto;
	}

	public String getTargetSm() {
		return targetSm;
	}

	public void setTargetSm(String targetSm) {
		this.targetSm = targetSm;
	}

	private String targetSm;
	public String getTargetSava() {
		return targetSava;
	}

	public void setTargetSava(String targetSava) {
		this.targetSava = targetSava;
	}

	private String targetSava;
	//商铺
	private String targetId;
	//店铺名字
	private String targetName;
	//商户或店铺id，目标类型：1表示商户，2表示店铺
	private String targetType;
	//商铺地址
	private String targetAddress;
	//兑换商品相关信息
	private ExChangeBean exChangeBean;
	private String couponCount;

	public String getCouponCount() {
		return couponCount;
	}

	public void setCouponCount(String couponCount) {
		this.couponCount = couponCount;
	}

	public String getProductCount() {
		return productCount;
	}

	public void setProductCount(String productCount) {
		this.productCount = productCount;
	}

	private String productCount;
	public ExChangeBean getExChangeBean() {
		return exChangeBean;
	}

	public void setExChangeBean(ExChangeBean exChangeBean) {
		this.exChangeBean = exChangeBean;
	}

	public String getTargetId() {
		return targetId;
	}

	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}

	public String getTargetName() {
		return targetName;
	}

	public void setTargetName(String targetName) {
		this.targetName = targetName;
	}

	public String getTargetType() {
		return targetType;
	}

	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}

	public String getTargetAddress() {
		return targetAddress;
	}

	public void setTargetAddress(String targetAddress) {
		this.targetAddress = targetAddress;
	}
}
