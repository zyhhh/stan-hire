package cn.stan.common.utils;

import cn.stan.common.exception.GraceException;
import cn.stan.common.result.RespStatusEnum;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import sun.misc.BASE64Encoder;

import javax.crypto.SecretKey;
import java.util.Date;

@Slf4j
@Component
@RefreshScope
public class JWTUtils {

    public static final String AT = "@";

    @Value("${jwt.key}")
    public String jwtKey;

    // @Autowired
    // private JWTProperties jwtProperties;

    public String createToken(String body) {
        return generateToken(body, null);
    }

    public String createToken(String body, Long expireTime) {
        if (expireTime == null)
            GraceException.display(RespStatusEnum.SYSTEM_NO_EXPIRE_ERROR);

        if (expireTime < 0)
            GraceException.display(RespStatusEnum.SYSTEM_EXPIRE_TIME_ERROR);

        return generateToken(body, expireTime);
    }

    public String createToken(String body, String prefix) {
        return prefix + AT + generateToken(body, null);
    }

    public String createToken(String body, String prefix, Long expireTime) {
        if (expireTime == null)
            GraceException.display(RespStatusEnum.SYSTEM_NO_EXPIRE_ERROR);

        if (expireTime < 0)
            GraceException.display(RespStatusEnum.SYSTEM_EXPIRE_TIME_ERROR);

        return prefix + AT + generateToken(body, expireTime);
    }

    /**
     * 处理生成jwt主要方法
     *
     * @param body
     * @param expireTime
     * @return
     */
    public String generateToken(String body, Long expireTime) {
        // 1.获取secretKey
        SecretKey secretKey = generateSecretKey();

        // 2.生成jwtToken
        String jwtToken;
        if (expireTime == null) {
            jwtToken = generateJWT(body, secretKey);
        } else {
            jwtToken = generateJWT(body, expireTime, secretKey);
        }

        return jwtToken;
    }

    /**
     * 校验jwt，返回数据
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
     * 生成SecretKey
     *
     * @return
     */
    public SecretKey generateSecretKey() {
        // log.info("--------> jwtKey: {}", jwtKey);
        // 对秘钥进行base64加密
        String encode = new BASE64Encoder().encode(jwtKey.getBytes());

        // 封装SecretKey
        return Keys.hmacShaKeyFor(encode.getBytes());
    }

    /**
     * 生成jwt，无过期时间
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
     * 生成jwt，带过期时间
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
