package com.zcx.front.view;

import com.zcx.api.model.RechargeRecord;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.math.BigDecimal;

public class RechargeView {

    private Integer id = 0;
    private String result = "xxx";
    private String rechargeDate = "-";
    private BigDecimal rechargeMoney = new BigDecimal(0);

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getRechargeDate() {
        return rechargeDate;
    }

    public void setRechargeDate(String rechargeDate) {
        this.rechargeDate = rechargeDate;
    }

    public BigDecimal getRechargeMoney() {
        return rechargeMoney;
    }

    public void setRechargeMoney(BigDecimal rechargeMoney) {
        this.rechargeMoney = rechargeMoney;
    }

    public RechargeView() {
    }

    public RechargeView(Integer id, String result, String rechargeDate, BigDecimal rechargeMoney) {
        this.id = id;
        this.result = result;
        this.rechargeDate = rechargeDate;
        this.rechargeMoney = rechargeMoney;
    }

    public RechargeView(RechargeRecord record) {
       this.id = record.getId();
       this.rechargeMoney = record.getRechargeMoney();
       if (record.getRechargeTime() != null) {
           this.rechargeDate = DateFormatUtils.format(record.getRechargeTime(), "yyyy-MM-dd");
       }
       switch (record.getRechargeStatus()) {
           case 0:
               this.result = "充值中";
               break;
           case 1:
               this.result = "成功";
               break;
           case 2:
               this.result = "失败";
       }
    }

    @Override
    public String toString() {
        return "RechargeView{" +
                "id=" + id +
                ", result='" + result + '\'' +
                ", rechargeDate='" + rechargeDate + '\'' +
                ", rechargeMoney=" + rechargeMoney +
                '}';
    }
}
