package com.zcx.task;

import com.zcx.api.service.IncomeService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

@Component
public class TaskManager {

    @DubboReference(interfaceClass = IncomeService.class, version = "1.0")
    private IncomeService incomeService;

    @Scheduled(cron = "0 0 1 * * ?")
    public void invokeGenerateIncomePlan() {
        incomeService.generateIncomePlan();
    }

    @Scheduled(cron = "0 0 2 * * ?")
    public void invokeGenerateIncomeBack() {
        incomeService.generateIncomeBack();
    }

    @Scheduled(cron = "0 0/20 * * * ?")
    public void invokeFastMoneyQuery() {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet get = new HttpGet("http://localhost:9000/pay/kq/receive/query");
        try {
            client.execute(get);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
