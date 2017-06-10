package com.digoshop.app.base;

import com.digoshop.app.utils.ErrorJson;

public class BaseInfo implements ErrorJson{
    public String desc;
    public String provider;
    public String code;
    public String cost;
    public String uid;
    public String token;
    public String st;
	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getSt() {
		return st;
	}

	public void setSt(String st) {
		this.st = st;
	}

	@Override
	public void setdesc(String desc) {
		this.desc = desc;
	}

	@Override
	public String getdesc() {
		return desc;
	}

	@Override
	public void setprovider(String provider) {
		this.provider = provider;
	}

	@Override
	public String getprovider() {
		return provider;
	}

	@Override
	public void setcode(String code) {
		this.code = code;
	}

	@Override
	public String getcode() {
		// TODO Auto-generated method stub
		return code;
	}

	@Override
	public void setcost(String cost) {
		this.cost = cost;
	}

	@Override
	public String getcost() {
		// TODO Auto-generated method stub
		return cost;
	}
   
}
