package com.digoshop.app.module.userCenter.model;

public class MessageInfo {
	
	// {
			// "content": "2222", 内容
			// "model": 2,
			// "title": "系统通知1111", 标题
			// "dest": "569a43ef0cf2c59e816e5df5",
			// "status": 0, 0是未读1是已读
			// "nid": 1000006, 通知id
			// "origin": "321321",
			// "child_type": 0,
			// "create_time": 1460896423, 时间
			// "type": 100
			// },
	//内容
	private String content;
    // 标题
	private String title;
    // 0是未读1是已读
	private String status;
    //通知id
	private String nid;

	private String child_type;
    //时间
	private String create_time;
	// type 98商户消息 100系统消息 全部消息 -1
	private String type;

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getTurl() {
		return turl;
	}

	public void setTurl(String turl) {
		this.turl = turl;
	}

	private String sender;
	private String turl;

	private MessageInfo_shopinfo info_shopinfo;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getNid() {
		return nid;
	}

	public void setNid(String nid) {
		this.nid = nid;
	}

	public String getChild_type() {
		return child_type;
	}

	public void setChild_type(String child_type) {
		this.child_type = child_type;
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

	public MessageInfo_shopinfo getInfo_shopinfo() {
		return info_shopinfo;
	}

	public void setInfo_shopinfo(MessageInfo_shopinfo info_shopinfo) {
		this.info_shopinfo = info_shopinfo;
	}

}
