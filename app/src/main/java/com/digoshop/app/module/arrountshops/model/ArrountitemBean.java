package com.digoshop.app.module.arrountshops.model;

import java.io.Serializable;

/**
 * Created by lsqbeyond on 2017/2/17.
 */

public class ArrountitemBean implements Serializable{
    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getMoid() {
        return moid;
    }

    public void setMoid(String moid) {
        this.moid = moid;
    }

    private String tagName;
    private String name;
    private String tag;
    private String moid;
}
