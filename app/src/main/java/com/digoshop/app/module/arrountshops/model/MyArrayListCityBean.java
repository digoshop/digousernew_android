package com.digoshop.app.module.arrountshops.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by lsqbeyond on 2017/2/17.
 */

public class MyArrayListCityBean   implements Serializable {
    private String tagName;

    public String getMoid() {
        return moid;
    }

    public void setMoid(String moid) {
        this.moid = moid;
    }

    private String moid;
    public ArrayList<ArrountitemBean> getArrountitemBeens() {
        return arrountitemBeens;
    }

    public void setArrountitemBeens(ArrayList<ArrountitemBean> arrountitemBeens) {
        this.arrountitemBeens = arrountitemBeens;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    private ArrayList<ArrountitemBean> arrountitemBeens;
}
