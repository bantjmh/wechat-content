package com.bshuai.content.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "wechat")
public class WechatProperties {

	private String imageServerUrl = "http://image.bshuai.cn";

	private String filePath = "/var/file";

	private String apiServerUrl = "http://api.bshuai.cn";

	public String getImageServerUrl() {
		return imageServerUrl;
	}

	public void setImageServerUrl(String imageServerUrl) {
		this.imageServerUrl = imageServerUrl;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getApiServerUrl() {
		return apiServerUrl;
	}

	public void setApiServerUrl(String apiServerUrl) {
		this.apiServerUrl = apiServerUrl;
	}

}
