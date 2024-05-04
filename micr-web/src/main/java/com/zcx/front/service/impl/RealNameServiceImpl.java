package com.zcx.front.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.zcx.api.service.UserService;
import com.zcx.common.constants.RedisKey;
import com.zcx.front.config.AliyunRealNameConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
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
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class RealNameServiceImpl {

    @Autowired
    private AliyunRealNameConfiguration aliyunRealNameConfiguration;

    @DubboReference(interfaceClass = UserService.class, version = "1.0")
    private UserService userService;

    public boolean handleRealName(String phone, String name, String idCard) {


        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost(aliyunRealNameConfiguration.getUrl());

        List<NameValuePair> params = new ArrayList<>();

        params.add(new BasicNameValuePair("name", name));
        params.add(new BasicNameValuePair("id_number", idCard));

        try {
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(params, "UTF-8");
            post.setEntity(formEntity);
            post.setHeader("Authorization", "APPCODE " + aliyunRealNameConfiguration.getAppCode());
            CloseableHttpResponse response = client.execute(post);
            System.out.println("###############" + response.getStatusLine().getStatusCode() + "################");
            if (response.getStatusLine().getStatusCode() == 200) {
                String result = EntityUtils.toString(response.getEntity());
                System.out.println("###############" + result + "################");
                if (StringUtils.isNotBlank(result)) {
                    JSONObject jsonObject = JSONObject.parseObject(result);
                    if ("0000".equals(jsonObject.getString("result_code"))) {
                        JSONObject target = jsonObject.getJSONObject("result");
                        if ("1".equals(target.getString("checkresult"))) {
                            return userService.modifyRealName(phone, name, idCard);
                        }
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

}
