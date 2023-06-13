package cn.stan;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

// @EnableRetry 开启重试
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan(basePackages = "cn.stan.mapper")
@EnableFeignClients("cn.stan.api.feign")
public class AuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }

}
