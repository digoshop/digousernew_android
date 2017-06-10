package com.digoshop.app.module.userCenter.model;

import com.digoshop.app.module.storedetail.model.UserInfoData;

/**
 * Created by lsqbeyond on 2016/10/27.
 */

public class ShopInviteDetailBean {
    public UserInfoData getUserInfoData() {
        return userInfoData;
    }

    public void setUserInfoData(UserInfoData userInfoData) {
        this.userInfoData = userInfoData;
    }

    public String getIs_vip() {
        return is_vip;
    }

    public void setIs_vip(String is_vip) {
        this.is_vip = is_vip;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getIntg() {
        return intg;
    }

    public void setIntg(String intg) {
        this.intg = intg;
    }

    public String getVip_level_name() {
        return vip_level_name;
    }

    public void setVip_level_name(String vip_level_name) {
        this.vip_level_name = vip_level_name;
    }

    private UserInfoData userInfoData;
    private String is_vip;
    private String mobile;
    private String intg;
    private String vip_level_name;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    private String code;

}
