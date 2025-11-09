package com.nhnacademy.api.controller;// TaskAPI 프로젝트 내의 TaskController.java

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class TestController {

    @GetMapping("/test")
    public String getHello() {
        log.info("request!");
        return "Hello from Task API!";
    }
}