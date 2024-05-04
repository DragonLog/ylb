package com.zcx.front.controller;

import com.zcx.api.pojo.IncomeInfo;
import com.zcx.api.service.*;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;

//@CrossOrigin
public class BaseController {

    @Autowired
    protected StringRedisTemplate stringRedisTemplate;

    @DubboReference(interfaceClass = UserService.class, version = "1.0")
    protected UserService userService;

    @DubboReference(interfaceClass = PlatBaseInfoService.class, version = "1.0")
    protected PlatBaseInfoService platBaseInfoService;

    @DubboReference(interfaceClass = ProductService.class, version = "1.0")
    protected ProductService productService;

    @DubboReference(interfaceClass = InvestService.class, version = "1.0")
    protected InvestService investService;

    @DubboReference(interfaceClass = RechargeService.class, version = "1.0")
    protected RechargeService rechargeService;

    @DubboReference(interfaceClass = IncomeService.class, version = "1.0")
    protected IncomeService incomeService;
}
