package com.zcx.front.view;

public class RankView {

    private String phone;
    private Double money;

    public RankView() {
    }

    public RankView(String phone, Double money) {
        this.phone = phone;
        this.money = money;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    @Override
    public String toString() {
        return "RankView{" +
                "phone='" + phone + '\'' +
                ", money=" + money +
                '}';
    }
}
