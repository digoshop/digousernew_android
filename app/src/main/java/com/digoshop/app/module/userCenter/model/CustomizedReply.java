package com.digoshop.app.module.userCenter.model;

import java.util.ArrayList;

/**
 * @author Administrator
 */
public class CustomizedReply {
    public ArrayList<String> getUsergoodimgs() {
        return usergoodimgs;
    }

    public void setUsergoodimgs(ArrayList<String> usergoodimgs) {
        this.usergoodimgs = usergoodimgs;
    }

    private ArrayList<String> usergoodimgs;
    private ArrayList<CurlistEntity> arrayList;
    private String uid;
    private String desc;
    private String mc;
    private String tp;
    private String iurls;
    private String st;
    private String tn;
    private String et;
    private String cid;
    private String rc;
    private String ct;

    public ArrayList<CurlistEntity> getArrayList() {
        return arrayList;
    }

    public void setArrayList(ArrayList<CurlistEntity> arrayList) {
        this.arrayList = arrayList;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getMc() {
        return mc;
    }

    public void setMc(String mc) {
        this.mc = mc;
    }

    public String getTp() {
        return tp;
    }

    public void setTp(String tp) {
        this.tp = tp;
    }

    public String getIurls() {
        return iurls;
    }

    public void setIurls(String iurls) {
        this.iurls = iurls;
    }

    public String getSt() {
        return st;
    }

    public void setSt(String st) {
        this.st = st;
    }

    public String getTn() {
        return tn;
    }

    public void setTn(String tn) {
        this.tn = tn;
    }

    public String getEt() {
        return et;
    }

    public void setEt(String et) {
        this.et = et;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getRc() {
        return rc;
    }

    public void setRc(String rc) {
        this.rc = rc;
    }

    public String getCt() {
        return ct;
    }

    public void setCt(String ct) {
        this.ct = ct;
    }
}