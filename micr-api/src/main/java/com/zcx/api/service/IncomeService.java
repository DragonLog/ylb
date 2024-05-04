package com.zcx.api.service;

import com.zcx.api.pojo.IncomeInfo;

import java.util.List;

public interface IncomeService {

    void generateIncomePlan();

    void generateIncomeBack();


    List<IncomeInfo> queryByUid(Integer uid, Integer pageNo, Integer pageSize);
}
