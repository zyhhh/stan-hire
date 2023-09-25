package cn.stan.common.utils;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Data
@PropertySource("classpath:excludeUrl.properties")
@ConfigurationProperties(prefix = "path")
public class ExcludeUrlProperties {

    /**
     * 无需鉴权的接口
     */
    private List<String> noAuthUrls;

    /**
     * ip限流校验的接口
     */
    private List<String> ipLimitUrls;
}
