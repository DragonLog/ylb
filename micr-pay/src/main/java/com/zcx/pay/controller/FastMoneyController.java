package com.zcx.pay.controller;

import com.zcx.api.model.User;
import com.zcx.pay.service.FastMoneyServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Map;

@Controller
@RequestMapping("/kq")
public class FastMoneyController {

    @Autowired
    private FastMoneyServiceImpl fastMoneyService;

    @GetMapping("/receive/recharge")    //讲道理，感觉这里也得验一下token，起码得登录了才能充值吧
    public String receiveFrontRechargeFastMoney(Integer uid, BigDecimal rechargeMoney, Model model) {
        String view = "err";
        if (uid != null && uid > 0 && rechargeMoney != null && rechargeMoney.doubleValue() > 0) {

            User user = fastMoneyService.queryUser(uid);
            if (user != null) {
                Map<String, String> data = fastMoneyService.generateFormData(uid, rechargeMoney, user.getPhone());
                model.addAllAttributes(data);
                boolean flag = fastMoneyService.addRecharge(uid, rechargeMoney, data.get("orderId"));
                if (flag) {
                    fastMoneyService.addOrderIdToRedis(data.get("orderId"));
                    view = "kqForm";
                }
            }
        }
        return view;
    }

    @GetMapping("/receive/notify")
    @ResponseBody
    public String payResultNotify(HttpServletRequest request) {
        System.out.println("##############" + "接受异步请求" + "################");
        fastMoneyService.fastMoneyNotify(request);
        return "<result>1</result><redirecturl>http://localhost:8080</redirecturl>";
    }

    @GetMapping("/receive/query")
    @ResponseBody
    public String queryFastMoneyOrder() {
        fastMoneyService.handleQueryOrder();
        return "接受了查询的请求";
    }
}
