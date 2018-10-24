package com.bshuai.content.model.vo;

public class AuthVo {
	private String authUrl;

	public String getAuthUrl() {
		return authUrl;
	}

	public void setAuthUrl(String authUrl) {
		this.authUrl = authUrl;
	}

	@Override
	public String toString() {
		return "AuthVo{" + "authUrl='" + authUrl + '\'' + '}';
	}
}
