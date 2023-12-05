package cn.stan.common.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@Data
@PropertySource("classpath:jwt.properties")
@ConfigurationProperties(prefix = "auth")
public class JWTProperties {

    private String key;
}
