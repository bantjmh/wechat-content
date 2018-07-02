package com.loyi.cloud.wechat.platform.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ValidationException;

import com.loyi.cloud.wechat.platform.config.WechatProperties;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

import com.loyi.ucenter.constant.AccountRole;
import com.loyi.ucenter.jwt.domain.UserInfo;

@RestController
public class BaseController {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    protected HttpServletRequest request;

    @Autowired
    WechatProperties wechatProperties;

    protected final String MOBILE_REDIRECT_URL = "mobile_redirect_url";

    @ModelAttribute
    public void setReqAndRes(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
    }

    protected HttpServletRequest getRequest() {
        return request;
    }

    protected String getToken() {
        String token = request.getHeader("Authorization");
        if (StringUtils.isNotBlank(token) && token.startsWith("Bearer ")) {
            token = token.split(" ")[1];
        } else {
            token = (String) request.getSession().getAttribute("AccessToken");
            if (StringUtils.isBlank(token)) {
                token = request.getParameter("AccessToken");
            }
        }
        return token;
    }

    /**
     * 获取用户标识
     * 
     * @return
     */
    protected String getLoginUID() {
        UserInfo user = getUser();
        if (user != null) {
            return getUser().getUid();
        }
        return "";
    }

    /**
     * 获取用户名称
     * 
     * @return
     */
    protected String getLoginUname() {
        UserInfo user = getUser();
        if (user != null) {
            return getUser().getUsername();
        }
        return "";
    }

    protected String getRealName() {
        UserInfo user = getUser();
        if (user != null) {
            return getUser().getName();
        }
        return "";
    }

    protected UserInfo getUser() {
        Object principal = SecurityUtils.getSubject().getPrincipal();
        if (principal == null) {
            throw new AuthenticationException("未登录");
        }

        UserInfo user = (UserInfo) principal;
        return user;
    }

    /**
     * 判断当前用户是否管理端
     * 
     * @return
     */
    protected boolean isAdmin() {
        UserInfo user = getUser();
        if (user.getRoles().contains(AccountRole.ADMIN)) {
            return true;
        }
        return false;
    }

    /**
     * 判断当前用户是否是超级管理员
     * 
     * @return
     */
    protected boolean isSystem() {
        UserInfo user = getUser();
        if (user.getRoles().contains(AccountRole.SYSTEM)) {
            return true;
        }
        return false;
    }

    /**
     * 判断当前用户是否普通用户
     * 
     * @return
     */
    protected boolean isUser() {
        UserInfo user = getUser();
        if (user.getRoles().contains(AccountRole.USER)) {
            return true;
        }
        return false;
    }

    /**
     * 检查错误
     * 
     * @param errors
     */
    protected void checkError(Errors errors) {
        if (errors.hasErrors()) {
            throw new ValidationException(errors.getFieldErrors().get(0).getDefaultMessage());
        }
    }
}