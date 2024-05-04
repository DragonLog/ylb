package com.zcx.front.view;

import com.zcx.common.enums.RCodeEnum;

import java.util.List;

public class RespResult {

    private int code;
    private String msg;
    private Object data;
    private List list;
    private PageInfo page;
    private String accessToken;

    public static RespResult ok() {
        RespResult result = new RespResult();
        result.setRCode(RCodeEnum.SUCC);
        return result;
    }

    public static RespResult fail() {
        RespResult result = new RespResult();
        result.setRCode(RCodeEnum.UNKNOWN);
        return result;
    }

    public void setRCode(RCodeEnum rCodeEnum) {
        this.code = rCodeEnum.getCode();
        this.msg = rCodeEnum.getText();
    }

    public RespResult() {
    }

    public RespResult(int code, String msg, Object data, List list, PageInfo page, String accessToken) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.list = list;
        this.page = page;
        this.accessToken = accessToken;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public List getList() {
        return list;
    }

    public void setList(List list) {
        this.list = list;
    }

    public PageInfo getPage() {
        return page;
    }

    public void setPage(PageInfo page) {
        this.page = page;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public String toString() {
        return "RespResult{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                ", list=" + list +
                ", page=" + page +
                ", accessToken='" + accessToken + '\'' +
                '}';
    }
}
