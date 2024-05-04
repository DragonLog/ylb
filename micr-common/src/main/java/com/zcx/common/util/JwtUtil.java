package com.zcx.common.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.apache.commons.lang3.time.DateUtils;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

public class JwtUtil {


    private String selfKey;

    public JwtUtil(String selfKey) {
        this.selfKey = selfKey;
    }

    public String createJwt(Map<String, Object> data, Integer minute) {
        SecretKey secretKey = Keys.hmacShaKeyFor(selfKey.getBytes(StandardCharsets.UTF_8));
        String jwt = Jwts.builder().signWith(secretKey, SignatureAlgorithm.HS256)
                .setExpiration(DateUtils.addMinutes(new Date(), minute))
                .setIssuedAt(new Date())
                .setId(UUID.randomUUID().toString().replaceAll("-", "").toUpperCase())
                .addClaims(data)
                .compact();
        return jwt;
    }

    public Claims readJwt(String jwt) {
        SecretKey secretKey = Keys.hmacShaKeyFor(selfKey.getBytes(StandardCharsets.UTF_8));
        Claims body = Jwts.parserBuilder().setSigningKey(secretKey)
                .build().parseClaimsJws(jwt).getBody();
        return body;
    }

}
