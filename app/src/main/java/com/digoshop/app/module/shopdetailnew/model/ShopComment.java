package com.digoshop.app.module.shopdetailnew.model;

import java.util.ArrayList;

/**
 * Created by lsqbeyond on 2017/3/9.
 */

public class ShopComment {
    //评论人名字
    private String nick ;
    //评论人uid
    private String uid;
    //评论人头像
    private String avatar;
    //评论时间
    private String create_time;
    //评论级别 0好 1中 2差
    private String type;
    //评论内容
    private String text;
    //评论图片
    private ArrayList<String> strings;
    private String reply_time;//商家回复时间
    private String reply_text;//商家回复内容
    public String getReply_time() {
        return reply_time;
    }

    public void setReply_time(String reply_time) {
        this.reply_time = reply_time;
    }

    public String getReply_text() {
        return reply_text;
    }

    public void setReply_text(String reply_text) {
        this.reply_text = reply_text;
    }


    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public ArrayList<String> getStrings() {
        return strings;
    }

    public void setStrings(ArrayList<String> strings) {
        this.strings = strings;
    }


}
