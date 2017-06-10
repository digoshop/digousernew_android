package com.digoshop.app.module.seekservice.newview;

/**
 * Created by lijuan on 2016/9/12.
 */
public class Model {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getCateid() {
        return cateid;
    }

    public void setCateid(String cateid) {
        this.cateid = cateid;
    }

    private String name;
    private String iconUrl;
    private String cateid;
}