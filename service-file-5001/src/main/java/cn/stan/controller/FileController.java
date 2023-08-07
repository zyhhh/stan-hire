package cn.stan.controller;

import cn.stan.common.result.GraceResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zyh
 * @date 2023-08-07 下午 06:04
 */
@Slf4j
@RestController
@RequestMapping("file")
public class FileController {

    @GetMapping("hello")
    public GraceResult hello() {
        return GraceResult.ok("hello, file service");
    }
}
