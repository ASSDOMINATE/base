package com.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/")
public class BaseController {

    @RequestMapping("demo")
    @ResponseBody
    public Map<String,Object> demo(){
        Map<String ,Object> test = new HashMap<>(1);
        test.put("test","yes");
        return test;
    }
}
