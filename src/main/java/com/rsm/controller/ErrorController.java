package com.rsm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/errors")
public class ErrorController {
    @GetMapping("/403")
    public String forbidden(){
        return "errors/403";
    }
}
