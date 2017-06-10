package com.digoshop.app.module.looksales.model;

import java.io.Serializable;

/**
 * Created by lsqbeyond on 2016/10/28.
 */

public class SaleHistoryBean implements Serializable {
    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getExchange_id() {
        return exchange_id;
    }

    public void setExchange_id(String exchange_id) {
        this.exchange_id = exchange_id;
    }

    public String getExchange_time() {
        return exchange_time;
    }

    public void setExchange_time(String exchange_time) {
        this.exchange_time = exchange_time;
    }

    //    "nick": "社区居民25",
//            "exchange_time": "1970-01-18 09:09:44",
//            "exchange_id": 1000003
    private String nick;
    private String exchange_time;
    private String exchange_id;
}
