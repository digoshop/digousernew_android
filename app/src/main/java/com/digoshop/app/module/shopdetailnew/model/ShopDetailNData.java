package com.digoshop.app.module.shopdetailnew.model;

import com.digoshop.app.module.arrountshops.model.ShopInfoBean;
import com.digoshop.app.module.shopdetail.model.PointBean;
import com.digoshop.app.module.userCenter.model.Discountcoupons;

import java.util.ArrayList;

/**
 * Created by lsqbeyond on 2017/3/9.
 */

public class ShopDetailNData {


    // 判断sign 1是签约  其他的是认证
    private String sign;
    //关注状态
    private String  relation;
    //店铺环境图片列表
    private ArrayList<String> shopenvs;
    //商品总数
    private String total;
    //店铺id
    private String sid;
    //店铺名字
    private String name;
    //店铺封面
    private String cover;
    //营业时间
    private String open_time;
    //联系方式
    private String contact;
    //店铺地址
    private String address;
    //店铺介绍
    private String description;
    //活动名称
    private String mnti;
    //活动id
    private String mnid;
    //优惠券列表
    private ArrayList<Discountcoupons> discountcouponses;
    //商品列表
    private ArrayList<ShopProduct> shopProducts;
    //评论列表
    private ArrayList<ShopComment> shopComments;
    //商家联盟店铺
    private ArrayList<ShopInfoBean> shopinfoDatas;


    //坐标经纬度
    private PointBean pointBean;

    public PointBean getPointBean() {
        return pointBean;
    }

    public void setPointBean(PointBean pointBean) {
        this.pointBean = pointBean;
    }
    public String getAddress() {
        return address;
    }

    public ArrayList<String> getShopenvs() {
        return shopenvs;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public void setShopenvs(ArrayList<String> shopenvs) {
        this.shopenvs = shopenvs;
    }

    public void setAddress(String address) {
        this.address = address;
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
    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
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

    public String getOpen_time() {
        return open_time;
    }

    public void setOpen_time(String open_time) {
        this.open_time = open_time;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMnti() {
        return mnti;
    }
    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
    public void setMnti(String mnti) {
        this.mnti = mnti;
    }

    public String getMnid() {
        return mnid;
    }

    public void setMnid(String mnid) {
        this.mnid = mnid;
    }

    public ArrayList<Discountcoupons> getDiscountcouponses() {
        return discountcouponses;
    }

    public void setDiscountcouponses(ArrayList<Discountcoupons> discountcouponses) {
        this.discountcouponses = discountcouponses;
    }

    public ArrayList<ShopProduct> getShopProducts() {
        return shopProducts;
    }

    public void setShopProducts(ArrayList<ShopProduct> shopProducts) {
        this.shopProducts = shopProducts;
    }

    public ArrayList<ShopComment> getShopComments() {
        return shopComments;
    }

    public void setShopComments(ArrayList<ShopComment> shopComments) {
        this.shopComments = shopComments;
    }

    public ArrayList<ShopInfoBean> getShopinfoDatas() {
        return shopinfoDatas;
    }

    public void setShopinfoDatas(ArrayList<ShopInfoBean> shopinfoDatas) {
        this.shopinfoDatas = shopinfoDatas;
    }
}
