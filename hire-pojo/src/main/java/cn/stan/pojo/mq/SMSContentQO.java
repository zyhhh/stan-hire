package cn.stan.pojo.mq;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SMSContentQO {

    // 手机号
    private String mobile;

    // 内容
    private String content;

    // 过期时间，分钟
    private String expireTime;
}
