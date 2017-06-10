package com.digoshop.app.module.looksales.model;

/**
 * Created by lsqbeyond on 2016/10/22.
 */

public class PoitGoodBean {

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    private String price;
    private String cost;
    private String pid;//	商品id
    private String pna;//	商品名称
    private String pt;//	商品类型(1竞拍,2换购,3销售)
    private String ppi;//	图片
    private String epp;//	兑换价格
    private String epg;//	所需币
    private String eprd;//	剩余天数（小于一天时返回0）
    private String  eps ;//4兑换中 5已结束 6已兑换
    public String getEps() {
        return eps;
    }

    public void setEps(String eps) {
        this.eps = eps;
    }


    public String getPid() {
        return pid;
    }

    public String getPna() {
        return pna;
    }

    public String getPt() {
        return pt;
    }

    public String getPpi() {
        return ppi;
    }

    public String getEpp() {
        return epp;
    }

    public String getEpg() {
        return epg;
    }

    public String getEprd() {
        return eprd;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public void setPna(String pna) {
        this.pna = pna;
    }

    public void setPt(String pt) {
        this.pt = pt;
    }

    public void setPpi(String ppi) {
        this.ppi = ppi;
    }

    public void setEpp(String epp) {
        this.epp = epp;
    }

    public void setEpg(String epg) {
        this.epg = epg;
    }

    public void setEprd(String eprd) {
        this.eprd = eprd;
    }
}
