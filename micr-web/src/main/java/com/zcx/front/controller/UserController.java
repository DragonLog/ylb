package com.zcx.front.controller;

import com.zcx.api.model.User;
import com.zcx.api.pojo.UserAccountInfo;
import com.zcx.common.enums.RCodeEnum;
import com.zcx.common.util.CommonUtil;
import com.zcx.common.util.JwtUtil;
import com.zcx.front.service.SmsService;
import com.zcx.front.service.impl.RealNameServiceImpl;
import com.zcx.front.view.RespResult;
import com.zcx.front.vo.RealNameVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Api(tags = "用户功能")
@RestController
@RequestMapping("/v1")
public class UserController extends BaseController {

    @Autowired
    private SmsService smsCodeRegisterImpl;

    @Autowired
    private SmsService smsCodeLoginImpl;

    @Autowired
    private RealNameServiceImpl realNameServiceImpl;

    @Autowired
    private JwtUtil jwtUtil;

    @ApiOperation(value = "手机号是否注册过", notes = "在注册功能中，判断手机号是否可以注册")
    @GetMapping("/user/phone/exists")
    public RespResult phoneExists(@RequestParam("phone") String phone) {
        RespResult result = new RespResult();
        result.setRCode(RCodeEnum.PHONE_EXISTS);
        if (CommonUtil.checkPhone(phone)) {
            User user = userService.queryByPhone(phone);
            if (user == null) {
                result = RespResult.ok();
            }
        } else {
            result.setRCode(RCodeEnum.PHONE_FORMAT_ERR);
        }
        return result;
    }

    @ApiOperation(value = "手机号注册用户")
    @PostMapping("/user/register")
    public RespResult userRegister(@RequestParam("phone") String phone, @RequestParam("miMa") String miMa, @RequestParam("yanZhengMa") String yanZhengMa) {
        RespResult result = RespResult.fail();
        if (CommonUtil.checkPhone(phone)) {
            if (miMa != null && miMa.length() == 32) {
                if (smsCodeRegisterImpl.checkSmsCode(phone, yanZhengMa)) {
                    int registerResult = userService.userRegister(phone, miMa);
                    if (registerResult == 1) {
                        result = RespResult.ok();
                    } else if (registerResult == 2) {
                        result.setRCode(RCodeEnum.PHONE_EXISTS);
                    } else {
                        result.setRCode(RCodeEnum.REQUEST_PARAM_ERR);
                    }
                } else {
                    result.setRCode(RCodeEnum.SMS_CODE_INVALID);
                }
            } else {
                result.setRCode(RCodeEnum.REQUEST_PARAM_ERR);
            }
        } else {
            result.setRCode(RCodeEnum.PHONE_FORMAT_ERR);
        }
        return result;
    }

    @ApiOperation(value = "用户登录", notes = "获取系统token")
    @PostMapping("/user/login")
    public RespResult userLogin(@RequestParam("phone") String phone, @RequestParam("miMa") String miMa, @RequestParam("yanZhengMa") String yanZhengMa) {
        RespResult result = RespResult.fail();

        if (CommonUtil.checkPhone(phone) && miMa != null && miMa.length() == 32) {
            if (smsCodeLoginImpl.checkSmsCode(phone, yanZhengMa)) {
                User user = userService.userLogin(phone, miMa);
                if (user != null) {
                    Map<String, Object> data = new HashMap<>();
                    data.put("uid", user.getId());
                    String token = jwtUtil.createJwt(data, 120);
                    result = RespResult.ok();
                    result.setAccessToken(token);

                    Map<String, Object> userInfo = new HashMap<>();
                    userInfo.put("uid", user.getId());
                    userInfo.put("phone", user.getPhone());
                    userInfo.put("name", user.getName());
                    result.setData(userInfo);
                } else {
                    result.setRCode(RCodeEnum.INFORMATION_ERR);
                }
            } else {
                result.setRCode(RCodeEnum.SMS_CODE_INVALID);
            }
        } else {
            result.setRCode(RCodeEnum.REQUEST_PARAM_ERR);
        }

        return result;
    }

    @ApiOperation(value = "实名认证", notes = "根据姓名和身份证号进行认证")
    @PostMapping("/user/realName")
    public RespResult userRealName(@RequestBody RealNameVO realNameVO) {
        System.out.println("#############" + realNameVO + "#############");
        RespResult result = RespResult.fail();
        //此处忽略验证码的判断
        //这里我认为同一个手机号也可以变更身份认证（其实就是懒得做“判断已经实名认证了”这个功能！）
        if (CommonUtil.checkPhone(realNameVO.getPhone())) {
            if (userService.queryByPhone(realNameVO.getPhone()) != null) {
                if (StringUtils.isNotBlank(realNameVO.getName()) && StringUtils.isNotBlank(realNameVO.getIdCard())) {
                    if (realNameServiceImpl.handleRealName(realNameVO.getPhone(), realNameVO.getName(), realNameVO.getIdCard())) {
                        result = RespResult.ok();
                    } else {
                        result.setRCode(RCodeEnum.REALNAME_FAIL);
                    }
                } else {
                    result.setRCode(RCodeEnum.REQUEST_PARAM_ERR);
                }
            } else {
                result.setRCode(RCodeEnum.PHONE_NOT_EXISTS);
            }
        } else {
            result.setRCode(RCodeEnum.REQUEST_PARAM_ERR);
        }
        return result;
    }

    @ApiOperation(value = "用户中心", notes = "根据header中信息查询用户账户信息")
    @GetMapping("/user/userCenter")
    public RespResult userCenter(@RequestHeader("uid") Integer uid) {
        RespResult result = RespResult.fail();
        if (uid != null && uid > 0) {
            UserAccountInfo userAccountInfo = userService.queryUserAllInfo(uid);
            if (userAccountInfo != null) {
                result = RespResult.ok();
                Map<String, Object> data = new HashMap<>();
                data.put("name", userAccountInfo.getName());
                data.put("phone", userAccountInfo.getPhone());
                data.put("headerUrl", userAccountInfo.getHeaderImage());
                data.put("money", userAccountInfo.getAvailableMoney());
                if (userAccountInfo.getLastLoginTime() != null) {
                    data.put("loginTime", DateFormatUtils.format(userAccountInfo.getLastLoginTime(), "yyyy-MM-dd HH:mm:ss"));
                } else {
                    data.put("loginTime", "-");
                }
                result.setData(data);
            }
        }
        return result;
    }
}
