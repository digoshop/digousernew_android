package com.digoshop.app.module.userCenter.model;

import java.util.ArrayList;

public class Discountcoupons {
    public void setOperates(ArrayList<String> operates) {
        this.operates = operates;
    }

    private ArrayList<String> operates;

    public String getOperatesstr() {
        return operatesstr;
    }

    public void setOperatesstr(String operatesstr) {
        this.operatesstr = operatesstr;
    }


    public ArrayList<String> getOperates() {
        return operates;
    }

    private String cbd;// 优惠券使用规则
    private String ctid;// 类型ID
    private String ctn;// 类型名称
    private String cbid;// 批次ID

    private String cbn;// 优惠券名称
    private String cbca;// 代金券额度
    private String cbcf;// 满多少可以用
    private String cbved; // 结束时间
    private String cbvsd; // 开始时间
    private double cbr;// 折扣值
    private String cbi;// 左边logo图片
    private String cbsnu;// 剩余数量
    private String status;// 用户状态 0已抢光 1 未领取 3已领取 5已过期
    private String cbea;// 需要多少币
    private String tname;// 发行店铺的名字
    private String tcover;// 店铺封面图
    private String cbtid;// 发行店铺ID
    private String cbtt;// 1 商户优惠券 2店铺优惠券

    public String getCbd() {
        return cbd;
    }

    private String operatesstr;

    public void setCbd(String cbd) {
        this.cbd = cbd;
    }

    public Discountcoupons() {
        super();
    }

    public Discountcoupons(String ctid, String ctn, String cbid, String cbn,
                           String cbca, String cbcf, String cbved, String cbvsd, double cbr,
                           String cbi, String cbsnu, String status, String cbea, String tname,
                           String tcover, String cbtid, String cbtt) {
        super();
        this.ctid = ctid;
        this.ctn = ctn;
        this.cbid = cbid;
        this.cbn = cbn;
        this.cbca = cbca;
        this.cbcf = cbcf;
        this.cbved = cbved;
        this.cbvsd = cbvsd;
        this.cbr = cbr;
        this.cbi = cbi;
        this.cbsnu = cbsnu;
        this.status = status;
        this.cbea = cbea;
        this.tname = tname;
        this.tcover = tcover;
        this.cbtid = cbtid;
        this.cbtt = cbtt;
    }

    public String getCtid() {
        return ctid;
    }

    public void setCtid(String ctid) {
        this.ctid = ctid;
    }

    public String getCtn() {
        return ctn;
    }

    public void setCtn(String ctn) {
        this.ctn = ctn;
    }

    public String getCbid() {
        return cbid;
    }

    public void setCbid(String cbid) {
        this.cbid = cbid;
    }

    public String getCbn() {
        return cbn;
    }

    public void setCbn(String cbn) {
        this.cbn = cbn;
    }

    public String getCbca() {
        return cbca;
    }

    public void setCbca(String cbca) {
        this.cbca = cbca;
    }

    public String getCbcf() {
        return cbcf;
    }

    public void setCbcf(String cbcf) {
        this.cbcf = cbcf;
    }

    public String getCbved() {
        return cbved;
    }

    public void setCbved(String cbved) {
        this.cbved = cbved;
    }

    public String getCbvsd() {
        return cbvsd;
    }

    public void setCbvsd(String cbvsd) {
        this.cbvsd = cbvsd;
    }

    public double getCbr() {
        return cbr;
    }

    public void setCbr(double cbr) {
        this.cbr = cbr;
    }

    public String getCbi() {
        return cbi;
    }

    public void setCbi(String cbi) {
        this.cbi = cbi;
    }

    public String getCbsnu() {
        return cbsnu;
    }

    public void setCbsnu(String cbsnu) {
        this.cbsnu = cbsnu;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCbea() {
        return cbea;
    }

    public void setCbea(String cbea) {
        this.cbea = cbea;
    }

    public String getTname() {
        return tname;
    }

    public void setTname(String tname) {
        this.tname = tname;
    }

    public String getTcover() {
        return tcover;
    }

    public void setTcover(String tcover) {
        this.tcover = tcover;
    }

    public String getCbtid() {
        return cbtid;
    }

    public void setCbtid(String cbtid) {
        this.cbtid = cbtid;
    }

    public String getCbtt() {
        return cbtt;
    }

    public void setCbtt(String cbtt) {
        this.cbtt = cbtt;
    }

}
