package com.bshuai.content.model;

import java.util.Date;

public class App {

	private String appId;

	private String nickName;

	private String alias;

	private String headImg;

	private String principalName;

	private String userName;

	private String qrcodeUrl;

	private int serviceTypeInfo;

	private int verifyTypeInfo;

	private String funcInfo;

	private Date created;

	private String manager = "";

	/**
	 * {@link AppState}
	 */
	private int state = AppState.ENABLE;

	public String getTypeInfo() {
		switch (this.getServiceTypeInfo()) {
		case 0:
			return "订阅号";
		case 1:
			return "订阅号";
		case 2:
			return "服务号";
		}
		return "";
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getManager() {
		return manager;
	}

	public void setManager(String manager) {
		this.manager = manager;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getHeadImg() {
		return headImg;
	}

	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}

	public String getPrincipalName() {
		return principalName;
	}

	public void setPrincipalName(String principalName) {
		this.principalName = principalName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getQrcodeUrl() {
		return qrcodeUrl;
	}

	public void setQrcodeUrl(String qrcodeUrl) {
		this.qrcodeUrl = qrcodeUrl;
	}

	public int getServiceTypeInfo() {
		return serviceTypeInfo;
	}

	public void setServiceTypeInfo(int serviceTypeInfo) {
		this.serviceTypeInfo = serviceTypeInfo;
	}

	public int getVerifyTypeInfo() {
		return verifyTypeInfo;
	}

	public void setVerifyTypeInfo(int verifyTypeInfo) {
		this.verifyTypeInfo = verifyTypeInfo;
	}

	public String getFuncInfo() {
		return funcInfo;
	}

	public void setFuncInfo(String funcInfo) {
		this.funcInfo = funcInfo;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

}
