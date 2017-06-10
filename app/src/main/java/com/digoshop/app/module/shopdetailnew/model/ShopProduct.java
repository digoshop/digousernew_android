package com.digoshop.app.module.shopdetailnew.model;

/**
 * Created by lsqbeyond on 2017/3/9.
 */

public class ShopProduct {

    //现价
    private String price;
    //商品名称
    private String name;
    //商品ID
    private String pid;
    //2兑换 3普通商品
    private String type;
    //图片
    private String picture;
    //商品图片
    private String ppi;
    //商品名称
    private String pna;
    //商品原价
    private String ppr;
    //商品优惠价
    private String pppr;
    //商品类型（1竞拍 2兑换 3普通）
    private  String pt;

    public String getPpi() {
        return ppi;
    }

    public void setPpi(String ppi) {
        this.ppi = ppi;
    }

    public String getPna() {
        return pna;
    }

    public void setPna(String pna) {
        this.pna = pna;
    }

    public String getPpr() {
        return ppr;
    }

    public void setPpr(String ppr) {
        this.ppr = ppr;
    }

    public String getPppr() {
        return pppr;
    }

    public void setPppr(String pppr) {
        this.pppr = pppr;
    }

    public String getPt() {
        return pt;
    }

    public void setPt(String pt) {
        this.pt = pt;
    }

    public String getEpp() {
        return epp;
    }

    public void setEpp(String epp) {
        this.epp = epp;
    }

    //兑换价格（兑换商品）
    private String epp;


    public String getEpg() {
        return epg;
    }

    public void setEpg(String epg) {
        this.epg = epg;
    }

    public String getR_price() {
        return r_price;
    }

    public void setR_price(String r_price) {
        this.r_price = r_price;
    }

    //迪币
    private String epg;
    //市场价
    private String r_price;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

}
