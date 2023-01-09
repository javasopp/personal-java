package com.mds.auth.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author sopp
 * @version 1.0
 * @date 2021/9/8 11:27
 */
@Data
@Slf4j
@Component
public class JwtUtils {
    @Value("${jwt.expiration}")
    private long expire;

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.header}")
    private String header;

    /**
     * 生成jwt
     *
     * @param username
     * @return
     */
    public String generateToken(String username) {

        Date nowDate = new Date();
        Date expireDate = new Date(nowDate.getTime() + 1000 * expire);

        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(username)
                .setIssuedAt(nowDate)
                // 7天过期
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * 解析jwt
     *
     * @param jwt
     * @return
     */
    public Claims getClaimByToken(String jwt) {
        try {
            return Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(jwt)
                    .getBody();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * jwt是否过期
     *
     * @param claims
     * @return
     */
    public boolean isTokenExpired(Claims claims) {
        boolean isTokeExpired = false;
        if (claims == null) {
            return false;
        }
        try {
            isTokeExpired = claims.getExpiration().before(new Date());
        } catch (Exception e) {
            log.info("我是当前的claims的过期时间:{}", claims.getExpiration());
            e.printStackTrace();
        }
        return isTokeExpired;
    }
}
