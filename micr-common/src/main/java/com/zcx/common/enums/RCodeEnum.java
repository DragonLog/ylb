package com.zcx.common.enums;

public enum RCodeEnum {

    SUCC(200, "请求成功"),
    UNKNOWN(0, "请稍后重试"),
    REQUEST_PARAM_ERR(1001, "请求参数有误"),
    REQUEST_PRODUCT_TYPE_ERR(1002, "产品类型有误"),
    PRODUCT_OFFLINE(1003, "产品已经下线"),
    PHONE_FORMAT_ERR(1004, "手机号格式不正确"),
    PHONE_EXISTS(1005, "手机号已经注册过"),
    SMS_CODE_CAN_USE(1006, "验证码可以继续使用"),
    SMS_CODE_INVALID(1007, "验证码无效"),
    PHONE_NOT_EXISTS(1008, "手机号未注册"),
    INFORMATION_ERR(1009, "账号或密码有误"),
    REALNAME_FAIL(1010, "认证失败，请检查信息"),
    INVEST_PRODUCT_FAIL(1011, "投资产品失败"),
    TOKEN_INVALID(3000, "token无效");

    private int code;
    private String text;

    RCodeEnum(int c, String t) {
        this.code =c;
        this.text = t;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
