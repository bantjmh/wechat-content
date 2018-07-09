package com.loyi.stone.content.sdk.client;

import com.loyi.stone.content.api.domain.Article;
import com.loyi.stone.content.api.domain.ArticleSendRecord;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(url = "${com.loyi.stone.content.url:http://localhost:8082}", name = "articleFeignService")
public interface IArticleFeignService {

    @GetMapping(value = "/article/detail")
    public Article detail(@RequestParam(value = "articleId") String articleId);

    @PostMapping(value = "/article/save/record")
    public void saveRecord(@RequestBody ArticleSendRecord articleSendRecord);
}
