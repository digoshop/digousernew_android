package com.digoshop.app.module.userCenter.model;

import java.util.ArrayList;

/**
 * Created by lsqbeyond on 2016/11/14.
 */

public class PointsData {
    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getMin_id() {
        return min_id;
    }

    public void setMin_id(String min_id) {
        this.min_id = min_id;
    }

    public String getMax_id() {
        return max_id;
    }

    public void setMax_id(String max_id) {
        this.max_id = max_id;
    }

    private String total;
    private String min_id;
    private String max_id;

    public ArrayList<PointsEntity> getPointsEntities() {
        return pointsEntities;
    }

    public void setPointsEntities(ArrayList<PointsEntity> pointsEntities) {
        this.pointsEntities = pointsEntities;
    }

    private ArrayList<PointsEntity> pointsEntities;
}
