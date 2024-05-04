package com.zcx.front.controller;

import com.zcx.api.pojo.IncomeInfo;
import com.zcx.front.view.RespResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "收益业务")
@RequestMapping("/v1")
@RestController
public class IncomeController extends BaseController {

    @ApiOperation(value = "查询某个用户的收益记录", notes = "根据用户的id和分页参数")
    @GetMapping("/income/records")
    public RespResult queryIncomePage(@RequestHeader("uid") Integer uid, @RequestParam(required = false, defaultValue = "1") Integer pageNo, @RequestParam(required = false, defaultValue = "6") Integer pageSize) {
        RespResult result = RespResult.fail();
        if (uid != null && uid > 0) {
            List<IncomeInfo> records = incomeService.queryByUid(uid, pageNo, pageSize);
            for (IncomeInfo incomeInfo : records) {
                incomeInfo.setIncomeTime(DateFormatUtils.format(incomeInfo.getIncomeDate(), "yyyy-MM-dd"));
            }
            result = RespResult.ok();
            result.setList(records);
        }
        return result;
    }

}
