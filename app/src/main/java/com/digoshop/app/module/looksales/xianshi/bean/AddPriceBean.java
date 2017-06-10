package com.digoshop.app.module.looksales.xianshi.bean;

import com.digoshop.app.module.looksales.model.AuctionBean;

import java.util.ArrayList;

/**
 * Created by lsqbeyond on 2016/11/18.
 */

public class AddPriceBean {
    public String getApsc() {
        return apsc;
    }

    public void setApsc(String apsc) {
        this.apsc = apsc;
    }

    public String getIntg() {
        return intg;
    }

    public void setIntg(String intg) {
        this.intg = intg;
    }

    public ArrayList<AuctionBean> getAuctionBeenList() {
        return auctionBeenList;
    }

    public void setAuctionBeenList(ArrayList<AuctionBean> auctionBeenList) {
        this.auctionBeenList = auctionBeenList;
    }

    private String apsc;//剩余次数
    private String intg;//最高积分
    private ArrayList<AuctionBean> auctionBeenList;
//    "auctionLogList": [
//    {
//        "ct": 1479471756,
//            "ppr": 41,
//            "nk": "15801348594"
//    }
//    ],
//            "apsc": 5,
//            "intg": 51
}
