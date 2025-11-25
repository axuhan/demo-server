package com.xh.agentdemo.healthcheck;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(path = "healthcheck")
public class HealthCheckController {

    @RequestMapping(method = RequestMethod.GET, path = "")
    @ResponseBody
    public String healthCheck() {
        return "OK";
    }
}
