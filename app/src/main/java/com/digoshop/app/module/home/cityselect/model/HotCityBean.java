package com.digoshop.app.module.home.cityselect.model;

import java.io.Serializable;

/**
 * Created by lsqbeyond on 2016/11/1.
 */

public class HotCityBean implements Serializable{
    private String city;

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    private String cityCode;

}
