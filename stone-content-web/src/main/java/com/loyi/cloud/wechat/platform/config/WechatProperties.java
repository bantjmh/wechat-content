package com.loyi.cloud.wechat.platform.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "wechat")
public class WechatProperties {

//	private String appid = "wx1f1077880c4a3d9c";
//	private String appsecret = "f5fbbe63c21d6448484942271fa4b9e1";
//
//	private String token = "tjo0kybbic0k1ujlogpnqscnvggfdwch";// 消息校验Token
//	private String encodingAesKey = "cDBX5M2CmpDZd0GUSfS6wQ0WM5G8d2M6b2SH9y0BJ4b";// 消息加解密Key
//
//	private String callbackUrl = "http://open.loyi.cn/component/bind/callback/%s";// 授权后回调URI
//
	private String serverUrl = "http://open.loyi.cn";

	private String filePath = "E:\\resource";

	public String getServerUrl() {
		return serverUrl;
	}

	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

//	public String getAppid() {
//		return appid;
//	}
//
//	public void setAppid(String appid) {
//		this.appid = appid;
//	}

//	public String getCallbackUrl() {
//		return callbackUrl;
//	}
//
//	public void setCallbackUrl(String callbackUrl) {
//		this.callbackUrl = callbackUrl;
//	}

//	public String getAppsecret() {
//		return appsecret;
//	}
//
//	public void setAppsecret(String appsecret) {
//		this.appsecret = appsecret;
//	}
//
//	public String getToken() {
//		return token;
//	}
//
//	public void setToken(String token) {
//		this.token = token;
//	}
//
//	public String getEncodingAesKey() {
//		return encodingAesKey;
//	}
//
//	public void setEncodingAesKey(String encodingAesKey) {
//		this.encodingAesKey = encodingAesKey;
//	}

}
