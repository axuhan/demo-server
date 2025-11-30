package com.xh.agentdemo.healthcheck;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HealthCheckController {

    @GetMapping("/healthcheck")
    @ResponseBody
    public String healthCheck() {
        return "OK";
    }
}
