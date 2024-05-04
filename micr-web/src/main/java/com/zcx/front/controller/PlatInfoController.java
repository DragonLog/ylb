package com.zcx.front.controller;

import com.zcx.api.pojo.BaseInfo;
import com.zcx.front.view.RespResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "平台信息功能")
@RestController
@RequestMapping("/v1")
public class PlatInfoController extends BaseController {

    @ApiOperation(value = "平台三项基本信息", notes = "注册人数，平均利率，总投资金额")
    @GetMapping("/plat/info")
    public RespResult queryPlatBaseInfo() {
        BaseInfo baseInfo = platBaseInfoService.queryPlatBaseInfo();
        RespResult result = RespResult.ok();
        result.setData(baseInfo);
        return result;
    }

}
