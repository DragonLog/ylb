package com.zcx.front.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "aliyun.sms")
public class AliyunSmsConfiguration {

    private String url;
    private String appCode;
    private String templateId;
    private String smsSignId;
    private String param;

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

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getSmsSignId() {
        return smsSignId;
    }

    public void setSmsSignId(String smsSignId) {
        this.smsSignId = smsSignId;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public AliyunSmsConfiguration() {
    }

    public AliyunSmsConfiguration(String url, String appCode, String templateId, String smsSignId, String param) {
        this.url = url;
        this.appCode = appCode;
        this.templateId = templateId;
        this.smsSignId = smsSignId;
        this.param = param;
    }

    @Override
    public String toString() {
        return "aliyunSmsConfiguration{" +
                "url='" + url + '\'' +
                ", appCode='" + appCode + '\'' +
                ", templateId='" + templateId + '\'' +
                ", smsSignId='" + smsSignId + '\'' +
                ", param='" + param + '\'' +
                '}';
    }
}
