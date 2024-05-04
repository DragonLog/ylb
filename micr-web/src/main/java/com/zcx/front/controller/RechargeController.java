package com.zcx.front.controller;

import com.zcx.api.model.RechargeRecord;
import com.zcx.front.view.RechargeView;
import com.zcx.front.view.RespResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Api(tags = "充值业务")
@RestController
@RequestMapping("/v1")
public class RechargeController extends BaseController {

    private List<RechargeView> toView(List<RechargeRecord> src) {
        List<RechargeView> target = new ArrayList<>();
        for (RechargeRecord rechargeRecord : src) {
            target.add(new RechargeView(rechargeRecord));
        }
        return target;
    }

    @ApiOperation(value = "查询某个用户的充值记录", notes = "根据用户id和分页参数")
    @GetMapping("/recharge/records")
    public RespResult queryRechargePage(@RequestHeader("uid") Integer uid, @RequestParam(required = false, defaultValue = "1") Integer pageNo, @RequestParam(required = false, defaultValue = "6") Integer pageSize) {
        RespResult result = RespResult.fail();
        if (uid != null && uid > 0) {
            List<RechargeRecord> records = rechargeService.queryByUid(uid, pageNo, pageSize);
            result = RespResult.ok();
            result.setList(toView(records));
        }
        return result;
    }

}
