package com.cargosmart.mongodemo.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.logging.log4j.util.Base64Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;

/**
 * @author licankun
 */
@EnableConfigurationProperties(Audience.class)
@Component
public class TokenUtil {

    @Autowired
    Audience audience;

    public Claims parseJWT(String jwtString) {
        Claims claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(audience.getBase64Secret()))
                .parseClaimsJws(jwtString).getBody();
        return claims;
    }

    public String createJWT(String userId) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(audience.getBase64Secret());
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
        //userId是重要信息，进行加密下
        String base64UserId = Base64Util.encode(userId);
        //添加构成JWT的参数
        JwtBuilder builder = Jwts.builder().setHeaderParam("typ", "JWT")
                .claim("base64UserId", base64UserId)
                .setSubject(userId)
                .setIssuer(audience.getIssuer())
                .setIssuedAt(now)
                .setAudience(audience.getClientId())
                .signWith(signatureAlgorithm, signingKey);

        //添加Token过期时间
        int TTLMillis = audience.getExpiresSecond();
        if (TTLMillis >= 0) {
            long expMillis = nowMillis + TTLMillis;
            Date exp = new Date(expMillis);
            /**
             *  exp: 代表这个JWT的过期时间；
             *  now: 代表这个JWT生效的开始时间，意味着在这个时间之前验证JWT是会失败的
             */
            System.out.println("JWT的过期时间"+ exp);
            System.out.println("JWT生效的开始时间"+ now);
            builder.setExpiration(exp).setNotBefore(now);
        }
        //生成JWT
        return builder.compact();
    }
}
