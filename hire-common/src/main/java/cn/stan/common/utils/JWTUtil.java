package cn.stan.common.utils;

import cn.stan.common.exception.GraceException;
import cn.stan.common.result.ResponseStatusEnum;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import sun.misc.BASE64Encoder;

import javax.crypto.SecretKey;
import javax.swing.table.AbstractTableModel;
import java.util.Date;

@Slf4j
@Component
@RefreshScope
public class JWTUtil {

    public static final String AT = "@";

    @Value("${jwt.key}")
    public String jwtKey;

    // @Autowired
    // private JWTProperties jwtProperties;

    public String createJWT(String body) {

        return buildJWT(body, null);
    }

    public String createJWT(String body, Long expireTime) {

        if (expireTime == null)
            GraceException.display(ResponseStatusEnum.SYSTEM_NO_EXPIRE_ERROR);

        if (expireTime < 0)
            GraceException.display(ResponseStatusEnum.SYSTEM_EXPIRE_TIME_ERROR);

        return buildJWT(body, expireTime);
    }

    public String createJWTWithPrefix(String body, String prefix) {

        return prefix + AT + buildJWT(body, null);
    }

    public String createJWTWithPrefix(String body, Long expireTime, String prefix) {

        if (expireTime == null)
            GraceException.display(ResponseStatusEnum.SYSTEM_NO_EXPIRE_ERROR);

        if (expireTime < 0)
            GraceException.display(ResponseStatusEnum.SYSTEM_EXPIRE_TIME_ERROR);

        return prefix + AT + buildJWT(body, expireTime);
    }

    /**
     * 处理生成 jwt 主要方法
     *
     * @param body
     * @param expireTime
     * @return
     */
    public String buildJWT(String body, Long expireTime) {


        // 1.获取secretKey
        SecretKey secretKey = generateSecretKey();

        String jwtToken;

        // 2.生成jwtToken
        if (expireTime == null) {
            jwtToken = generateJWT(body, secretKey);
        } else {
            jwtToken = generateJWT(body, expireTime, secretKey);
        }

        return jwtToken;
    }

    /**
     * 校验 jwt，返回数据
     *
     * @param jwtStr
     * @return
     */
    public String checkJWT(String jwtStr) {
        // 1.获取secretKey
        SecretKey secretKey = generateSecretKey();

        // 2.创建jwt解析器
        JwtParser jwtParser = Jwts.parserBuilder().setSigningKey(secretKey).build();

        // 3.解析jwt得到Claims
        Jws<Claims> claimsJws = jwtParser.parseClaimsJws(jwtStr);

        return claimsJws.getBody().getSubject();
    }

    /**
     * 生成 SecretKey
     *
     * @return
     */
    public SecretKey generateSecretKey() {
        log.info("--------> jwtKey: {}", jwtKey);
        // 对秘钥进行base64加密
        String encode = new BASE64Encoder().encode(jwtKey.getBytes());

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
