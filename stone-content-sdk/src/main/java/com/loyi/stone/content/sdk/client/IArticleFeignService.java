package com.loyi.stone.content.sdk.client;

import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient(url = "${com.loyi.stone.content.url:http://localhost:8080}", name = "articleFeignService")
public interface IArticleFeignService {

}
