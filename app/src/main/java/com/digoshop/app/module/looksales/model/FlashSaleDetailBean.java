package com.digoshop.app.module.looksales.model;

import java.util.List;

/**
 * Created by zzr on 16/10/26.
 */
public class FlashSaleDetailBean {
    private List<AuctionBean> auctionBeenList;
    private String apsc;
    private String targetName;
    private String targetId;
    private String targetAddress;
    private String targetType;

    public List<AuctionBean> getAuctionBeenList() {
        return auctionBeenList;
    }

    public void setAuctionBeenList(List<AuctionBean> auctionBeenList) {
        this.auctionBeenList = auctionBeenList;
    }

    /**
     * apsc : 9
     * targetName : 曲美家具店
     * targetId : 1000048
     * targetAddress : 聊吧台风网站
     * targetType : 2
     * auctionProductInfo : {"app":450,"pat":["XXL"],"pna":"Auction Product 023","aped":1472968800,"pt":1,"aps":6,"aplg":150,"pid":1000028,"apsd":1472961600,"pa":["http://zksxapp.img-cn-beijing.aliyuncs.com/merchant/2016-09-04/18A534F2-5CC5-4BDA-AD47-5468806FE262.jpg"],"ppr":650,"apd":"Auction Rule ","pd":"Detail","apg":10,"nte":1477485371}
     */

    /**
     * app : 450
     * pat : ["XXL"]
     * pna : Auction Product 023
     * aped : 1472968800
     * pt : 1
     * aps : 6
     * aplg : 150
     * pid : 1000028
     * apsd : 1472961600
     * pa : ["http://zksxapp.img-cn-beijing.aliyuncs.com/merchant/2016-09-04/18A534F2-5CC5-4BDA-AD47-5468806FE262.jpg"]
     * ppr : 650
     * apd : Auction Rule
     * pd : Detail
     * apg : 10
     * nte : 1477485371
     */

    private AuctionProductInfoEntity auctionProductInfo;

    public String getApsc() {
        return apsc;
    }

    public void setApsc(String apsc) {
        this.apsc = apsc;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getTargetAddress() {
        return targetAddress;
    }

    public void setTargetAddress(String targetAddress) {
        this.targetAddress = targetAddress;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public AuctionProductInfoEntity getAuctionProductInfo() {
        return auctionProductInfo;
    }

    public void setAuctionProductInfo(AuctionProductInfoEntity auctionProductInfo) {
        this.auctionProductInfo = auctionProductInfo;
    }

    public static class AuctionProductInfoEntity {
        private String app;
        private String pna;
        private long aped;
        private String pt;
        private String aps;
        private String aplg;
        private String pid;
        private long apsd;
        private String ppr;
        private String apd;
        private String pd;
        private String apg;
        private long nte;
        private List<String> pat;
        private List<String> pa;
        public long getAped() {
            return aped;
        }

        public void setAped(long aped) {
            this.aped = aped;
        }

        public long getApsd() {
            return apsd;
        }

        public void setApsd(long apsd) {
            this.apsd = apsd;
        }

        public long getNte() {
            return nte;
        }

        public void setNte(long nte) {
            this.nte = nte;
        }
        public String getApp() {
            return app;
        }

        public void setApp(String app) {
            this.app = app;
        }

        public String getPna() {
            return pna;
        }

        public void setPna(String pna) {
            this.pna = pna;
        }


        public String getPt() {
            return pt;
        }

        public void setPt(String pt) {
            this.pt = pt;
        }

        public String getAps() {
            return aps;
        }

        public void setAps(String aps) {
            this.aps = aps;
        }

        public String getAplg() {
            return aplg;
        }

        public void setAplg(String aplg) {
            this.aplg = aplg;
        }

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }


        public String getPpr() {
            return ppr;
        }

        public void setPpr(String ppr) {
            this.ppr = ppr;
        }

        public String getApd() {
            return apd;
        }

        public void setApd(String apd) {
            this.apd = apd;
        }

        public String getPd() {
            return pd;
        }

        public void setPd(String pd) {
            this.pd = pd;
        }

        public String getApg() {
            return apg;
        }

        public void setApg(String apg) {
            this.apg = apg;
        }


        public List<String> getPat() {
            return pat;
        }

        public void setPat(List<String> pat) {
            this.pat = pat;
        }

        public List<String> getPa() {
            return pa;
        }

        public void setPa(List<String> pa) {
            this.pa = pa;
        }
    }
}