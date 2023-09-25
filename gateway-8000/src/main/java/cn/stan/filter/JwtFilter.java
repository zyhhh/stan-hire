package cn.stan.filter;

import cn.stan.common.base.BaseInfoProperties;
import cn.stan.common.result.GraceResult;
import cn.stan.common.result.ResponseStatusEnum;
import cn.stan.common.utils.ExcludeUrlProperties;
import cn.stan.common.utils.GsonUtils;
import cn.stan.common.utils.JWTUtils;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
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
 * 该过滤器用于解析Token，并将用户信息存入header中
 */
@Slf4j
@Component
public class JwtFilter extends BaseInfoProperties implements GlobalFilter, Ordered {

    private static final String HEADER_USER_TOKEN = "headerUserToken";

    // 路径匹配的规则器
    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Autowired
    private ExcludeUrlProperties excludeUrlProperties;

    @Autowired
    private JWTUtils jwtUtils;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        // 获取当前请求路径
        String path = exchange.getRequest().getURI().getPath();
        // log.info("请求路径: {}", path);

        // 获取免鉴权的接口
        List<String> noAuthUrls = excludeUrlProperties.getNoAuthUrls();

        if (!CollectionUtils.isEmpty(noAuthUrls)) {
            boolean isExclude = noAuthUrls.stream().anyMatch(url -> antPathMatcher.matchStart(path, url));
            // 匹配成功则直接放行
            if (isExclude) {
                return chain.filter(exchange);
            }
        }
        // log.info("url被JwtFilter拦截了");

        // 获取header中的token
        HttpHeaders headers = exchange.getRequest().getHeaders();
        String token = headers.getFirst(HEADER_USER_TOKEN);

        // 无Token不放行。网关抛出异常无法被全局异常捕获，需要重新构建response
        if (StringUtils.isBlank(token)) {
            return renderErrorMsg(exchange, ResponseStatusEnum.UN_LOGIN);
        }

        // 验证token
        String[] split = token.split(JWTUtils.AT);
        if (split.length < 2) {
            return renderErrorMsg(exchange, ResponseStatusEnum.JWT_ERROR);
        }

        String prefix = split[0];
        String jwt = split[1];

        // 判断针对不同用户存不同的键
        if (TOKEN_USER_PREFIX.equalsIgnoreCase(prefix)) {
            prefix = APP_USER_JSON;
        } else if (TOKEN_SAAS_PREFIX.equalsIgnoreCase(prefix)) {
            prefix = SAAS_USER_JSON;
        } else if (TOKEN_ADMIN_PREFIX.equalsIgnoreCase(prefix)) {
            prefix = ADMIN_USER_JSON;
        }

        // jwt校验并设置进header中
        return verifyJWT(jwt, prefix, exchange, chain);
    }

    /**
     * 过滤器执行顺序，数字越小越先执行
     *
     * @return
     */
    @Override
    public int getOrder() {
        return 0;
    }

    /**
     * 校验jwt，错误抛出异常
     *
     * @param jwt
     * @param exchange
     * @param chain
     * @return
     */
    public Mono<Void> verifyJWT(String jwt, String header, ServerWebExchange exchange, GatewayFilterChain chain) {
        try {
            String userJson = jwtUtils.checkJWT(jwt);
            ServerWebExchange newExchange = setNewHeader(exchange, header, userJson);
            return chain.filter(newExchange);
        } catch (ExpiredJwtException e) {
            return renderErrorMsg(exchange, ResponseStatusEnum.JWT_EXPIRE_ERROR);
        } catch (Exception e) {
            return renderErrorMsg(exchange, ResponseStatusEnum.JWT_ERROR);
        }
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

    /**
     * 设置新的请求头
     *
     * @param exchange
     * @param header
     * @param value
     * @return
     */
    public ServerWebExchange setNewHeader(ServerWebExchange exchange, String header, String value) {
        // 重新构建新的request
        ServerHttpRequest newRequest = exchange.getRequest().mutate().header(header, value).build();

        // 将新的request设置进exchange中
        return exchange.mutate().request(newRequest).build();
    }
}
