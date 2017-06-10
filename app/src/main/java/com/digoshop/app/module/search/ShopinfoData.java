package com.digoshop.app.module.search;

import com.digoshop.app.module.arrountshops.model.ShopInfoBean;

import java.util.ArrayList;

/**
 * Created by lsqbeyond on 2016/10/23.
 */

public class ShopinfoData {
    public ArrayList<ShopInfoBean> getShopInfoBeans() {
        return shopInfoBeans;
    }

    public String getMarkstr() {
        return markstr;
    }

    public void setShopInfoBeans(ArrayList<ShopInfoBean> shopInfoBeans) {
        this.shopInfoBeans = shopInfoBeans;
    }

    public void setMarkstr(String markstr) {
        this.markstr = markstr;
    }

    ArrayList<ShopInfoBean> shopInfoBeans ;
    String markstr;

}
