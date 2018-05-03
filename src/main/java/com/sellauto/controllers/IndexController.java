package com.sellauto.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
//Home page
public class IndexController {
	
    @GetMapping("/")
    public String displayIndex() {
        return "index";
    }
}
