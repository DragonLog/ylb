package com.zcx.api.pojo;

import com.zcx.api.model.BidInfo;

public class InvestInfo extends BidInfo {

    String investTime;


    public String getInvestTime() {
        return investTime;
    }

    public void setInvestTime(String investTime) {
        this.investTime = investTime;
    }

    private String productName;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
