package cn.stan.common.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@Data
@PropertySource("classpath:tencentCloud.properties")
@ConfigurationProperties(prefix = "tencent.cloud")
public class TencentCloudProperties {

    private String secretId;

    private String secretKey;
}
