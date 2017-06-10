package com.digoshop.app.module.looksales.model;

import java.util.ArrayList;

/**
 * Created by zzr on 16/10/23.
 */
public class TimeTitleBean {
    private String time;//几年几月几日

    private long nte;//服务器当前时间
    private ArrayList<String> values;//该日内所包含的小时时间段
    private String valuesstr;//小时的时间段
    private String typestr;//改小时时间段状态
    private String dateandhourstr;//当前日期下的小时组合

    public String getTypestr() {
        return typestr;
    }

    public long getNte() {
        return nte;
    }

    public void setNte(long nte) {
        this.nte = nte;
    }

    public String getDateandhourstr() {
        return dateandhourstr;
    }

    public void setDateandhourstr(String dateandhourstr) {
        this.dateandhourstr = dateandhourstr;
    }


    public void setTypestr(String typestr) {
        this.typestr = typestr;
    }


    public String getValuesstr() {
        return valuesstr;
    }

    public void setValuesstr(String valuesstr) {
        this.valuesstr = valuesstr;
    }


    public ArrayList<String> getValues() {
        return values;
    }

    public void setValues(ArrayList<String> values) {
        this.values = values;
    }


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


}
