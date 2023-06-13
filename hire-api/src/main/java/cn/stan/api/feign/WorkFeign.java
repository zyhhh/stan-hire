package cn.stan.api.feign;

import cn.stan.common.result.GraceResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "work-service")
public interface WorkFeign {

    @PostMapping("/resume/init")
    GraceResult init(@RequestParam("userId") String userId);
}
