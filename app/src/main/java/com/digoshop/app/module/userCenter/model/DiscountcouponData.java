package com.digoshop.app.module.userCenter.model;

import java.util.ArrayList;

public class DiscountcouponData {


    private ArrayList<NumBean> numBeens;

    public ArrayList<NumBean> getNumBeens() {
        return numBeens;
    }

    public void setNumBeens(ArrayList<NumBean> numBeens) {
        this.numBeens = numBeens;
    }

    private String total;
    private ArrayList<Discountcoupons> arrayList;

    public void setOperates(ArrayList<String> operates) {
        this.operates = operates;
    }

    private ArrayList<String> operates;

    public ArrayList<String> getOperates() {
        return operates;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public ArrayList<Discountcoupons> getArrayList() {
        return arrayList;
    }

    public void setArrayList(ArrayList<Discountcoupons> arrayList) {
        this.arrayList = arrayList;
    }
}
