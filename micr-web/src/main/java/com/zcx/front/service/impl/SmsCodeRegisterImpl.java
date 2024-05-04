package com.zcx.front.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.zcx.common.constants.RedisKey;
import com.zcx.front.config.AliyunSmsConfiguration;
import com.zcx.front.service.SmsService;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service(value = "smsCodeRegisterImpl")
public class SmsCodeRegisterImpl implements SmsService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private AliyunSmsConfiguration aliyunSmsConfiguration;

    @Override
    public boolean sendSms(String phone) {
        String random = RandomStringUtils.randomNumeric(4);
        String content = String.format(aliyunSmsConfiguration.getParam(), random, 1);
        System.out.println("###############" + content + "################");

        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost(aliyunSmsConfiguration.getUrl());

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("mobile", phone));
        params.add(new BasicNameValuePair("param", content));
        params.add(new BasicNameValuePair("smsSignId", aliyunSmsConfiguration.getSmsSignId()));
        params.add(new BasicNameValuePair("templateId", aliyunSmsConfiguration.getTemplateId()));

        try {
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(params);
            post.setEntity(formEntity);
            post.setHeader("Authorization", "APPCODE " + aliyunSmsConfiguration.getAppCode());
            CloseableHttpResponse response = client.execute(post);
            System.out.println("###############" + response.getStatusLine().getStatusCode() + "################");
            if (response.getStatusLine().getStatusCode() == 200) {
                String result = EntityUtils.toString(response.getEntity());
                System.out.println("###############" + result + "################");
                if (StringUtils.isNotBlank(result)) {
                    JSONObject jsonObject = JSONObject.parseObject(result);
                    if ("0".equals(jsonObject.getString("code"))) {
                        stringRedisTemplate.boundValueOps(RedisKey.KEY_SMS_CODE_REG + ":" + phone).set(random, 1, TimeUnit.MINUTES);
                        return true;
                    }
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean checkSmsCode(String phone, String code) {
        if (stringRedisTemplate.hasKey(RedisKey.KEY_SMS_CODE_REG + ":" + phone)) {
            String querySmsCode = stringRedisTemplate.boundValueOps(RedisKey.KEY_SMS_CODE_REG + ":" + phone).get();
            if (code.equals(querySmsCode)) {
                return true;
            }
        }
        return false;
    }
}
