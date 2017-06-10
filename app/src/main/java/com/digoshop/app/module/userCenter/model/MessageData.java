package com.digoshop.app.module.userCenter.model;

import java.util.ArrayList;

public class MessageData {
	private ArrayList<MessageInfo> messageinfos;
	private String totalstr;

	public ArrayList<MessageInfo> getMessageinfos() {
		return messageinfos;
	}

	public void setMessageinfos(ArrayList<MessageInfo> messageinfos) {
		this.messageinfos = messageinfos;
	}

	public String getTotalstr() {
		return totalstr;
	}

	public void setTotalstr(String totalstr) {
		this.totalstr = totalstr;
	}
}
