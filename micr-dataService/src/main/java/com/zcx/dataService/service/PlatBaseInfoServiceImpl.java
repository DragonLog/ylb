package com.zcx.dataService.service;

import com.zcx.api.pojo.BaseInfo;
import com.zcx.api.service.PlatBaseInfoService;
import com.zcx.dataService.mapper.BidInfoMapper;
import com.zcx.dataService.mapper.ProductInfoMapper;
import com.zcx.dataService.mapper.UserMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

@DubboService(interfaceClass = PlatBaseInfoService.class, version = "1.0")
public class PlatBaseInfoServiceImpl implements PlatBaseInfoService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ProductInfoMapper productInfoMapper;

    @Autowired
    private BidInfoMapper bidInfoMapper;

    @Override
    public BaseInfo queryPlatBaseInfo() {
        int registerUser = userMapper.selectCountUsers();
        BigDecimal avgRate = productInfoMapper.selectAvgRate();
        BigDecimal sumBidMoney = bidInfoMapper.selectSumBidMoney();

        BaseInfo baseInfo = new BaseInfo(avgRate, sumBidMoney, registerUser);



        return baseInfo;
    }
}
