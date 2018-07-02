package com.loyi.cloud.wechat.platform.model.vo;

/**
 * Created by fq on 2018/6/25.
 */
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
        return "AuthVo{" +
                "authUrl='" + authUrl + '\'' +
                '}';
    }
}
