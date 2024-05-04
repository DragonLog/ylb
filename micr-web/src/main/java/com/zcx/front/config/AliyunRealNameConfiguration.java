package com.zcx.front.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "aliyun.realname")
public class AliyunRealNameConfiguration {

    private String url;
    private String appCode;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public AliyunRealNameConfiguration(String url, String appCode) {
        this.url = url;
        this.appCode = appCode;
    }

    public AliyunRealNameConfiguration() {
    }

    @Override
    public String toString() {
        return "AliyunRealNameConfiguration{" +
                "url='" + url + '\'' +
                ", appCode='" + appCode + '\'' +
                '}';
    }
}
