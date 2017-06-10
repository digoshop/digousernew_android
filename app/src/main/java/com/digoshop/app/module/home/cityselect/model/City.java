package com.digoshop.app.module.home.cityselect.model;

/**
 * author zaaach on 2016/1/26.
 */
public class City {
    private String name;
    private String pinyin;
    private String citycode;

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String cid;
    public String getCitycode() {
        return citycode;
    }

    public void setCitycode(String citycode) {
        this.citycode = citycode;
    }

    public City() {
    }

    public City(String name, String pinyin,String citycode) {
        this.name = name;
        this.pinyin = pinyin;
        this.citycode = citycode;
    }
    public City(String name, String pinyin,String citycode,String cid) {
        this.name = name;
        this.pinyin = pinyin;
        this.citycode = citycode;
        this.cid = cid;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }
}
