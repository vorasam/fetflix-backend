package com.dev.auth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class TestController {

    @GetMapping("/api/hello")
    public String hello() {
        System.out.println("calles test");
        return "HELLO SECURED API";
    }
}
