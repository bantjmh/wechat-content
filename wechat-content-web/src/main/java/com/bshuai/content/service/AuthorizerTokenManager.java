package com.bshuai.content.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

import weixin.popular.bean.component.ApiQueryAuthResult.Authorization_info;

/**
 * 授权方token管理
 *
 */
@Service
public class AuthorizerTokenManager {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired(required = false)
	private StringRedisTemplate redisTemplate;



	private final String AUTHORIZER_HASH = "authorizer_hash";

	public Authorization_info get(String authorizer_appid) {
		HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
		String json = opsForHash.get(AUTHORIZER_HASH, authorizer_appid);
		return JSON.parseObject(json, Authorization_info.class);
	}

	public void store(Authorization_info authorization_info) {
		HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
		opsForHash.put(AUTHORIZER_HASH, authorization_info.getAuthorizer_appid(),
				JSON.toJSONString(authorization_info));
	}

}
