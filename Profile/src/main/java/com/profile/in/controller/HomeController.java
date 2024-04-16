package com.profile.in.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/profile")
public class HomeController {
    @GetMapping("/home")
    public String loginPage() {
        return "home";
    }
}
