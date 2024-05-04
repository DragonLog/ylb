package com.zcx.front.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.zcx.common.enums.RCodeEnum;
import com.zcx.common.util.JwtUtil;
import com.zcx.front.view.RespResult;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class TokenInterceptor implements HandlerInterceptor {

    private String secret;

    public TokenInterceptor(String secret) {
        this.secret = secret;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String headerToken = request.getHeader("Authorization");
        String uid = request.getHeader("uid");

        if (StringUtils.isNotBlank(headerToken) && StringUtils.isNotBlank(uid)) {
            String token = headerToken.substring(7);
            try {
                JwtUtil jwtUtil = new JwtUtil(secret);
                Claims claims = jwtUtil.readJwt(token);
                String jwtUid = String.valueOf(claims.get("uid"));
                if (uid.equals(jwtUid)) {
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        RespResult result = RespResult.fail();
        result.setRCode(RCodeEnum.TOKEN_INVALID);

        String respJson = JSONObject.toJSONString(result);
        response.setContentType("application/json;charset=utf-8");

        PrintWriter printWriter = response.getWriter();
        printWriter.print(respJson);
        printWriter.flush();
        printWriter.close();

        return false;
    }
}
