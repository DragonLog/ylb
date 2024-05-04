package com.zcx.front.controller;

import com.zcx.api.model.BidInfo;
import com.zcx.api.model.User;
import com.zcx.api.pojo.InvestInfo;
import com.zcx.common.constants.RedisKey;
import com.zcx.common.enums.RCodeEnum;
import com.zcx.common.util.CommonUtil;
import com.zcx.front.view.RankView;
import com.zcx.front.view.RespResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Api(tags = "投资理财产品")
@RestController
@RequestMapping("/v1")
public class InvestController extends BaseController {

    @ApiOperation(value = "投资排行榜", notes = "显示投资金额最高的3位用户信息")
    @GetMapping("/invest/rank")
    public RespResult showInvestRank() {
        Set<ZSetOperations.TypedTuple<String>> sets = stringRedisTemplate.boundZSetOps(RedisKey.KEY_INVEST_RANK).reverseRangeWithScores(0, 2);

        List<RankView> rankList = new ArrayList<>();

        for (ZSetOperations.TypedTuple<String> tuple : sets) {
            rankList.add(new RankView(CommonUtil.tuoMinPhone(tuple.getValue()), tuple.getScore()));
        }

        RespResult result = RespResult.ok();
        result.setList(rankList);
        return result;
    }

    @ApiOperation(value = "查询某个用户的投资记录", notes = "根据用户id和分页参数")
    @GetMapping("/invest/records")
    public RespResult queryInvestPage(@RequestHeader("uid") Integer uid, @RequestParam(required = false, defaultValue = "1") Integer pageNo, @RequestParam(required = false, defaultValue = "6") Integer pageSize) {
        RespResult result = RespResult.fail();
        if (uid != null && uid > 0) {
            List<InvestInfo> records = investService.queryByUid(uid, pageNo, pageSize);
            for (InvestInfo investInfo : records) {
                investInfo.setInvestTime(DateFormatUtils.format(investInfo.getBidTime(), "yyyy-MM-dd"));
            }
            result = RespResult.ok();
            result.setList(records);
        }
        return result;
    }

    @ApiOperation(value = "投资理财产品", notes = "根据产品id，用户id， 投资金额进行投资")
    @PostMapping("/invest/product")
    public RespResult investProduct(@RequestParam("productId") Integer productId, @RequestParam("money")BigDecimal money, @RequestHeader("uid") Integer uid) {
        RespResult result = RespResult.fail();
        if (uid != null && uid > 0 && productId != null && productId > 0 && money != null && money.intValue() >= 100 && money.intValue() % 100 == 0) {//这里用BigDecimal的方法应该更准确
            int investResult = investService.investProduct(uid, productId, money);
            if (investResult == 1) {
                modifyInvestRank(uid, money);
                result = RespResult.ok();
            } else {
                result.setRCode(RCodeEnum.INVEST_PRODUCT_FAIL);     //直接定义个枚举返回了，不搞什么switch case了
            }
        } else {
            result.setRCode(RCodeEnum.REQUEST_PARAM_ERR);
        }
        return result;
    }

    private void modifyInvestRank(Integer uid, BigDecimal money) {
        User user = userService.queryById(uid);
        if (user != null) {
            stringRedisTemplate.boundZSetOps(RedisKey.KEY_INVEST_RANK).incrementScore(user.getPhone(), money.doubleValue());
        }
    }
}
