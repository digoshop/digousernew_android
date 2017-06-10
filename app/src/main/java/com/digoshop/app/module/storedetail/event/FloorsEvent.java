package com.digoshop.app.module.storedetail.event;

import com.digoshop.app.module.storedetail.model.FloorBean;

import java.util.ArrayList;

/**
 * Created by lsqbeyond on 2016/10/27.
 */

public class FloorsEvent {
    private ArrayList<FloorBean> floorBeans;
    private String totalstr;

    public FloorsEvent(ArrayList<FloorBean> floorBeans, String totalstr) {
        this.floorBeans = floorBeans;
        this.totalstr = totalstr;
    }
}
