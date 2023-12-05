package cn.stan.filter;

import cn.stan.common.base.BaseInfoProperties;
import cn.stan.common.result.GraceResult;
import cn.stan.common.result.ResponseStatusEnum;
import cn.stan.common.property.ExcludeUrlProperties;
import cn.stan.common.utils.GsonUtils;
import cn.stan.common.utils.IPUtils;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 该过滤器用于限制同一ip在单位时间内只能请求限流接口n次
 */
@Slf4j
@Component
public class IpLimitFilter extends BaseInfoProperties implements GlobalFilter, Ordered {

    // 路径匹配的规则器
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Autowired
    private ExcludeUrlProperties excludeUrlProperties;

    @Value("${blackIP.maxCountLimit}")
    private Integer maxCountLimit;
    @Value("${blackIP.timeInterval}")
    private Integer timeInterval;
    @Value("${blackIP.timeLimits}")
    private Integer timeLimits;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        // 获取当前请求路径
        String path = exchange.getRequest().getURI().getPath();

        // 获取需要进行ip限流的接口
        List<String> ipLimitUrls = excludeUrlProperties.getIpLimitUrls();

        if (!CollectionUtils.isEmpty(ipLimitUrls)) {
            boolean match = ipLimitUrls.stream().anyMatch(url -> antPathMatcher.matchStart(path, url));
            // 匹配成功进行接口限流判断
            if (match) {
                return doLimit(exchange, chain, path);
            }
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 1;
    }

    public Mono<Void> doLimit(ServerWebExchange exchange, GatewayFilterChain chain, String path) {
        // 获取当前访问ip
        ServerHttpRequest request = exchange.getRequest();
        String ip = IPUtils.getIP(request);

        // 设置redis的键
        String ipPathKey = "gateway-ip-path:" + ip + ":" + path;
        String ipLimitKey = "gateway-ip-limit:" + ip + ":" + path;

        // 1.校验当前ip是否已被限制，已被限制直接返回
        long ttl = redisUtils.ttl(ipLimitKey);
        if (ttl > 0) {
            return renderErrorMsg(exchange, ResponseStatusEnum.SYSTEM_ERROR_BLACK_IP);
        }

        // 2.设置当前ip请求接口的次数
        long reqCount = redisUtils.increment(ipPathKey, 1);
        // 第一次请求，需要设置接口的限制时间
        if (reqCount == 1) {
            redisUtils.expire(ipPathKey, timeInterval);
        }

        // 单位时间内ip请求接口的次数达到限制，则进行限流并返回错误
        if (reqCount > maxCountLimit) {
            redisUtils.set(ipLimitKey, "1", timeLimits);
            return renderErrorMsg(exchange, ResponseStatusEnum.SYSTEM_ERROR_BLACK_IP);
        }

        return chain.filter(exchange);
    }

    /**
     * 重新包装并返回错误信息
     *
     * @param exchange
     * @param statusEnum
     * @return
     */
    public Mono<Void> renderErrorMsg(ServerWebExchange exchange, ResponseStatusEnum statusEnum) {

        // 1.获取response
        ServerHttpResponse response = exchange.getResponse();

        // 2.修改响应的状态码为500
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);

        // 3.设置响应头类型
        if (!response.getHeaders().containsKey("Content-Type")) {
            response.getHeaders().add("Content-Type", MimeTypeUtils.APPLICATION_JSON_VALUE);
        }

        // 4.错误对象转成json并设置进response中
        String resultJson = GsonUtils.objectToString(GraceResult.error(statusEnum));
        DataBuffer dataBuffer = response.bufferFactory().wrap(resultJson.getBytes(StandardCharsets.UTF_8));

        return response.writeWith(Mono.just(dataBuffer));
    }
}
