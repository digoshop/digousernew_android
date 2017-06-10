package com.digoshop.app.module.home.model;

/**
 * Created by lsqbeyond on 2016/10/21.
 */

public class AdsData {
    public String title;
    public String getInfourl() {
        return infourl;
    }

    public void setInfourl(String infourl) {
        this.infourl = infourl;
    }

    //大图
    public String infourl;
    // 图片路径
    public String imgurl;
    // 所属id
    public String imgid;
    // 所属类型
    public String imgtype;
    public String url;
    public int resId;
}
