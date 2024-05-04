package com.zcx.front.settings;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfigurationSettings {

    @Bean
    public Docket docket() {
        Docket docket = new Docket(DocumentationType.SWAGGER_2);

        ApiInfo apiInfo = new ApiInfoBuilder()
                .title("盈利宝")
                .version("1.0")
                .description("前后端分离的项目，前端Vue，后端Spring Boot + Dubbo分布式项目")
                .build();

        docket = docket.apiInfo(apiInfo);

        docket = docket.select().apis(RequestHandlerSelectors.basePackage("com.zcx.front.controller")).build();
        return docket;
    }

}
