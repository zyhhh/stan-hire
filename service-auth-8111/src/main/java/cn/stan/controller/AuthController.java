package cn.stan.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("a")
public class AuthController {

    @GetMapping("hello")
    public Object hello() {
        return "hello, auth";
    }

}
