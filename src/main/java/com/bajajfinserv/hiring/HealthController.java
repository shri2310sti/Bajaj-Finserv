package com.bajajfinserv.hiring;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/")
    public String home() {
        return "Application is running!";
    }

    @GetMapping("/healthz")
    public String health() {
        return "OK";
    }
}
