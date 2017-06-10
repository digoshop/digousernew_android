package com.digoshop.app.module.looksales.model;

import java.io.Serializable;

/**
 * Created by zzr on 16/10/28.
 */
public class AuctionBean implements Serializable {

    /**
     * ct : 1477132617
     * ppr : 120
     * nk : 劲爆
     */

    private String ct;
    private String ppr;
    private String nk;

    public String getCt() {
        return ct;
    }

    public void setCt(String ct) {
        this.ct = ct;
    }

    public String getPpr() {
        return ppr;
    }

    public void setPpr(String ppr) {
        this.ppr = ppr;
    }

    public String getNk() {
        return nk;
    }

    public void setNk(String nk) {
        this.nk = nk;
    }
}
