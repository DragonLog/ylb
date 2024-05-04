package com.zcx.front.vo;

public class RealNameVO {

    private String phone;
    private String name;
    private String idCard;
    private String code;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public RealNameVO() {
    }

    public RealNameVO(String phone, String name, String idCard, String code) {
        this.phone = phone;
        this.name = name;
        this.idCard = idCard;
        this.code = code;
    }

    @Override
    public String toString() {
        return "RealNameVO{" +
                "phone='" + phone + '\'' +
                ", name='" + name + '\'' +
                ", idCard='" + idCard + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
