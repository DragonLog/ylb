package com.zcx.api.pojo;

import com.zcx.api.model.IncomeRecord;

public class IncomeInfo extends IncomeRecord {

    private String incomeTime;

    private String productName;

    public String getIncomeTime() {
        return incomeTime;
    }

    public void setIncomeTime(String incomeTime) {
        this.incomeTime = incomeTime;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
