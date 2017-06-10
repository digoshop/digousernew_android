package com.digoshop.app.module.looksales.model;

/**
 * Created by zzr on 16/10/24.
 */
public class FlashSaleListItemBean {


    /**
     * app : 450
     * pna : Auction Product 023
     * aped : 1472968800
     * pt : 1
     * aps : 4
     * aplg : 150
     * ppi : http://zksxapp.img-cn-beijing.aliyuncs.com/merchant/2016-09-04/18A534F2-5CC5-4BDA-AD47-5468806FE262.jpg
     * pid : 1000028
     * ptn : 曲美家具店
     * apsd : 1472961600
     * mon :
     * ppr : 650
     * nte : 1477379793
     */

    private String app;//交易金额
    private String pna;//商品名称

    public long getAped() {
        return aped;
    }

    public void setAped(long aped) {
        this.aped = aped;
    }

    public long getApsd() {
        return apsd;
    }

    public void setApsd(long apsd) {
        this.apsd = apsd;
    }

    public long getNte() {
        return nte;
    }

    public void setNte(long nte) {
        this.nte = nte;
    }

    private long aped;
    private String pt;//商品类型(1竞拍,2换购,3销售)
    private String aps;//竞拍状态（4未开始 5竞拍中 6已结束)
    private String aplg;//竞拍底价
    private String ppi;//图片
    private String pid;//商品id
    private String ptn;//店铺名称
    private long apsd;//竞拍时间
    private String mon;
    private String ppr;//商品原价
    private long nte;

    public String getApc() {
        return apc;
    }

    public void setApc(String apc) {
        this.apc = apc;
    }

    private String apc;//出价次数

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public String getPna() {
        return pna;
    }

    public void setPna(String pna) {
        this.pna = pna;
    }


    public String getPt() {
        return pt;
    }

    public void setPt(String pt) {
        this.pt = pt;
    }

    public String getAps() {
        return aps;
    }

    public void setAps(String aps) {
        this.aps = aps;
    }

    public String getAplg() {
        return aplg;
    }

    public void setAplg(String aplg) {
        this.aplg = aplg;
    }

    public String getPpi() {
        return ppi;
    }

    public void setPpi(String ppi) {
        this.ppi = ppi;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPtn() {
        return ptn;
    }

    public void setPtn(String ptn) {
        this.ptn = ptn;
    }


    public String getMon() {
        return mon;
    }

    public void setMon(String mon) {
        this.mon = mon;
    }

    public String getPpr() {
        return ppr;
    }

    public void setPpr(String ppr) {
        this.ppr = ppr;
    }


}
