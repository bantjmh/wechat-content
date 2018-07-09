package com.loyi.stone.content.sdk.client;

import com.loyi.stone.content.api.domain.Attach;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by fq on 2018/7/6.
 */
@FeignClient(url = "${com.loyi.stone.content.url:http://localhost:8082}", name = "imageFeignService")
public interface IImageFeignService {

    @GetMapping(value = "/image/detail")
    public Attach detail(@RequestParam(value = "mediaId") String mediaId);
}
