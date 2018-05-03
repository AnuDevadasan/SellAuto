package com.sellauto.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
//login page
public class LoginController {

    @GetMapping("login")
    public String displayLogin() {
        return "login";
    }
}