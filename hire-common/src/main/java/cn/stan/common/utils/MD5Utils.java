package cn.stan.common.utils;

import org.springframework.util.DigestUtils;

/**
 * MD5 加密工具类
 */
public class MD5Utils {

    /**
     * MD5混合加密
     * @param data: 待加密字符串
     * @param salt: 盐，用于混合md5加密
     * @return
     */
    public static String encrypt(String data, String salt) {
        String base = data + salt;
        return DigestUtils.md5DigestAsHex(base.getBytes());
    }

    public static void main(String[] args) {
       String md5Str = MD5Utils.encrypt("123456", "8111");
        System.out.println(md5Str);
    }
}