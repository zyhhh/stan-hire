package cn.stan.common.utils;

import cn.stan.common.exception.GraceException;
import cn.stan.common.result.ResponseStatusEnum;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sun.misc.BASE64Encoder;

import javax.crypto.SecretKey;
import javax.swing.table.AbstractTableModel;
import java.util.Date;

@Slf4j
@Component
public class JWTUtil {

    @Autowired
    private JWTProperties jwtProperties;

    private static final String AT = "@";

    public String createJWT(String body) {

        return handleJWT(body, null);
    }

    public String createJWT(String body, Long expireTime) {

        if (expireTime == null)
            GraceException.display(ResponseStatusEnum.SYSTEM_NO_EXPIRE_ERROR);

        if (expireTime < 0)
            GraceException.display(ResponseStatusEnum.SYSTEM_EXPIRE_TIME_ERROR);

        return handleJWT(body, expireTime);
    }

    public String createJWTWithPrefix(String body, String prefix) {

        return prefix + AT + handleJWT(body, null);
    }

    public String createJWTWithPrefix(String body, Long expireTime, String prefix) {

        if (expireTime == null)
            GraceException.display(ResponseStatusEnum.SYSTEM_NO_EXPIRE_ERROR);

        if (expireTime < 0)
            GraceException.display(ResponseStatusEnum.SYSTEM_EXPIRE_TIME_ERROR);

        return prefix + AT + handleJWT(body, expireTime);
    }

    /**
     * 处理生成 jwt 主要方法
     *
     * @param body
     * @param expireTime
     * @return
     */
    public String handleJWT(String body, Long expireTime) {

        String jwtToken = "";

        // 1.获取secretKey
        SecretKey secretKey = generateSecretKey();

        // 2.生成jwtToken
        if (expireTime == null) {
            jwtToken = generateJWT(body, secretKey);
        } else {
            jwtToken = generateJWT(body, expireTime, secretKey);
        }

        return jwtToken;
    }

    /**
     * 生成 SecretKey
     *
     * @return
     */
    public SecretKey generateSecretKey() {
        // 对秘钥进行base64加密
        String encode = new BASE64Encoder().encode(jwtProperties.getKey().getBytes());

        // 封装SecretKey
        return Keys.hmacShaKeyFor(encode.getBytes());
    }

    /**
     * 生成 jwt
     *
     * @param body
     * @param secretKey
     * @return
     */
    public String generateJWT(String body, SecretKey secretKey) {

        return Jwts.builder()
                .setSubject(body)
                .signWith(secretKey)
                .compact();
    }

    /**
     * 生成 jwt，带过期时间
     *
     * @param body
     * @param expireTime
     * @param secretKey
     * @return
     */
    public String generateJWT(String body, Long expireTime, SecretKey secretKey) {

        Date expireDate = new Date(System.currentTimeMillis() + expireTime);

        return Jwts.builder()
                .setSubject(body)
                .setExpiration(expireDate)
                .signWith(secretKey)
                .compact();
    }

}
