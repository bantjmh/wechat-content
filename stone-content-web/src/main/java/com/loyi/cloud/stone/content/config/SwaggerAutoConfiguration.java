package com.loyi.cloud.stone.content.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerAutoConfiguration {

	@Bean
	public Docket docket() {
		Docket docket = new Docket(DocumentationType.SWAGGER_2).apiInfo(this.apiInfo()).select()
				.apis(RequestHandlerSelectors.basePackage("com.loyi.cloud")).paths(PathSelectors.any()).build();
		return docket;
	}

	/**
	 * API文档主信息对象
	 * 
	 * @return
	 */
	private ApiInfo apiInfo() {
		ApiInfo apiInfo = (new ApiInfoBuilder()).title("微信管理平台 API接口文档").description("")
				.contact(new Contact("loyi", "http://www.loyi.cn", "14283184@qq.com")).version("1.0").build();
		return apiInfo;
	}

}
