package cn.stan.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

/**
 * 配置跨域，当前使用网关配置文件配置，所以此处注释
 */
// @Configuration
public class CorsConfig {

    // @Bean
    public CorsWebFilter corsWebFilter() {

        // 设置cors相关信息
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        // 允许所有域名跨域调用
        corsConfiguration.addAllowedOriginPattern("*");
        // 允许所有方法
        corsConfiguration.addAllowedMethod("*");
        // 允许所有请求头
        corsConfiguration.addAllowedHeader("*");
        // 设置是否发送cookie信息
        corsConfiguration.setAllowCredentials(true);

        // 设置映射的路径
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return new CorsWebFilter(source);

    }
}
