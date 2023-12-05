package cn.stan.common.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Data
@PropertySource("classpath:excludeUrl.properties")
@ConfigurationProperties(prefix = "exclude")
public class ExcludeUrlProperties {

    /**
     * 无需鉴权的接口
     */
    private List<String> url;

    /**
     * ip限流校验的接口
     */
    private List<String> ipLimitUrl;
}
