package com.zcx.dataService;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableDubbo
@MapperScan("com.zcx.dataService.mapper")
@SpringBootApplication
public class MicrDataServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MicrDataServiceApplication.class, args);
    }

}
