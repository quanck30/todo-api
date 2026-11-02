package com.quan.todoapi.controller;

/*
      課題名：HelloController
      作成日：2026/10/23
      作成者：ディンビンクアン
 */

import com.quan.todoapi.service.GreetingService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    private final GreetingService greetingService;
    public HelloController(GreetingService greetingService) {
        this.greetingService = greetingService;
    }
    @GetMapping("/hello")
    public String hello(@RequestParam(name = "name", defaultValue = "World") String name) {
        return greetingService.greet(name);
    }
}
