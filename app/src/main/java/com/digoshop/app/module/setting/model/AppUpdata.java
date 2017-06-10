package com.digoshop.app.module.setting.model;

/**
 * Created by zzr on 16/10/20.
 */
public class AppUpdata {
    public String getTarget_size() {
        return target_size;
    }

    public void setTarget_size(String target_size) {
        this.target_size = target_size;
    }

    public String getDown_url() {
        return down_url;
    }

    public void setDown_url(String down_url) {
        this.down_url = down_url;
    }

    public String getU_log() {
        return u_log;
    }

    public void setU_log(String u_log) {
        this.u_log = u_log;
    }

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public String getNew_version() {
        return new_version;
    }

    public void setNew_version(String new_version) {
        this.new_version = new_version;
    }

    public String getNew_md5() {
        return new_md5;
    }

    public void setNew_md5(String new_md5) {
        this.new_md5 = new_md5;
    }

    private String target_size;
    private String down_url;
    private String u_log;
    private boolean update;
    private String new_version;
    private String new_md5;


}
