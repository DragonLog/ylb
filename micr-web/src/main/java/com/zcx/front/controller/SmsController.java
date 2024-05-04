package com.zcx.front.controller;

import com.zcx.common.constants.RedisKey;
import com.zcx.common.enums.RCodeEnum;
import com.zcx.common.util.CommonUtil;
import com.zcx.front.service.SmsService;
import com.zcx.front.view.RespResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "短信业务")
@RestController
@RequestMapping("/v1")
public class SmsController extends BaseController {

    @Autowired
    private SmsService smsCodeLoginImpl;

    @Autowired
    private SmsService smsCodeRegisterImpl;

    @GetMapping("/sms/code/register")
    @ApiOperation(value = "注册短信发送", notes = "根据注册手机号发送短信")
    public RespResult sendCodeRegister(@RequestParam("phone") String phone) {
        RespResult result = RespResult.fail();
        if (CommonUtil.checkPhone(phone)) {
            if (stringRedisTemplate.hasKey(RedisKey.KEY_SMS_CODE_REG + ":" + phone)) {
                result.setRCode(RCodeEnum.SMS_CODE_CAN_USE);
            } else if (userService.queryByPhone(phone) != null) {
                result.setRCode(RCodeEnum.PHONE_EXISTS);
            } else {
                if (smsCodeRegisterImpl.sendSms(phone)) {
                    result = RespResult.ok();
                }
            }
        } else {
            result.setRCode(RCodeEnum.PHONE_FORMAT_ERR);
        }
        return result;
    }

    @GetMapping("/sms/code/login")
    @ApiOperation(value = "登录短信发送", notes = "根据登录手机号发送短信")
    public RespResult sendCodeLogin(@RequestParam("phone") String phone) {
        RespResult result = RespResult.fail();
        if (CommonUtil.checkPhone(phone)) {
            if (stringRedisTemplate.hasKey(RedisKey.KEY_SMS_CODE_LOGIN + ":" + phone)) {
                result.setRCode(RCodeEnum.SMS_CODE_CAN_USE);
            } else {
                if (userService.queryByPhone(phone) != null) {
                    if (smsCodeLoginImpl.sendSms(phone)) {
                        result = RespResult.ok();
                    }
                } else {
                    result.setRCode(RCodeEnum.PHONE_NOT_EXISTS);
                }
            }
        } else {
            result.setRCode(RCodeEnum.PHONE_FORMAT_ERR);
        }
        return result;
    }

}
