package com.zcx.api.pojo;

import java.io.Serializable;
import java.math.BigDecimal;

public class BidInfoProduct implements Serializable {

    private Integer id;
    private String phone;
    private String bidTime;
    private BigDecimal bidMoney;

    public BidInfoProduct() {
    }

    public BidInfoProduct(Integer id, String phone, String bidTime, BigDecimal bidMoney) {
        this.id = id;
        this.phone = phone;
        this.bidTime = bidTime;
        this.bidMoney = bidMoney;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBidTime() {
        return bidTime;
    }

    public void setBidTime(String bidTime) {
        this.bidTime = bidTime;
    }

    public BigDecimal getBidMoney() {
        return bidMoney;
    }

    public void setBidMoney(BigDecimal bidMoney) {
        this.bidMoney = bidMoney;
    }

    @Override
    public String toString() {
        return "BidInfoProduct{" +
                "id=" + id +
                ", phone='" + phone + '\'' +
                ", bidTime='" + bidTime + '\'' +
                ", bidMoney=" + bidMoney +
                '}';
    }
}
