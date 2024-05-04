package com.zcx;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@EnableDubbo
public class MicrTaskApplication {

    public static void main(String[] args) {
        SpringApplication.run(MicrTaskApplication.class, args);
    }

}
