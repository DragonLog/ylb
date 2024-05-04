package com.zcx.api.service;

import com.zcx.api.model.RechargeRecord;

import java.util.List;

public interface RechargeService {

    List<RechargeRecord> queryByUid(Integer uid, Integer pageNo, Integer pageSize);

    int addRechargeRecord(RechargeRecord rechargeRecord);

    int handleFastMoneyNotify(String orderId, String payAmount, String payResult);
}
