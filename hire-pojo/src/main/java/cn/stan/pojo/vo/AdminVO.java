package cn.stan.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AdminVO {

    /**
     * 登录名
     */
    private String username;

    /**
     * 头像
     */
    private String face;
}
