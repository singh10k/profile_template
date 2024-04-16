package com.profile.in.controller;

import com.profile.in.model.UserDetails;
import com.profile.in.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/auth")
@AllArgsConstructor
public class RegistrationController {
    private final UserService userService;

    @GetMapping(value = "/helloWord")
    public String loginPage() {
        return "helloWord";
    }

    @GetMapping("/register")
    public String showRegisterPage(@ModelAttribute("userInfo") UserDetails details) {
        System.out.println("HomeController.showRegisterPage()");
        return "user_register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("userInfo") UserDetails details, Map<String, Object> map) {
        System.out.println("HomeController.registerUser()");
        String resultMsg = userService.regsiter(details);
        map.put("message", resultMsg);
        return  "redirect:/auth/loginPage?registered";
    }

    @GetMapping("/forgetPassword")
    public String showForgetPassword() {
        System.out.println("HomeController.showRegisterPage()");
        return "forgetPass";
    }
    @PostMapping("/username")
    public @ResponseBody Map<String, Object> username(@RequestParam ("username") String username) {
        Map<String, Object> map=new HashMap<>();
        map = userService.username(username);
        System.out.println("HomeController.showRegisterPage()");
        return map;
    }

    @PostMapping("/forgetPassword")
    public @ResponseBody Map<String, Object> forgetPassword(@RequestParam ("username") String username, @RequestParam ("password") String password) {
        Map<String, Object> map=new HashMap<>();
        map = userService.forgetPassword(username, password);
        System.out.println("HomeController.showRegisterPage()");
        return map;
    }

    @PostMapping("/otpVerify")
    public @ResponseBody Map<String, Object> otpVerify(@RequestParam ("username") String username, @RequestParam ("otp") String otp){
        Map<String, Object> map=new HashMap<>();
        map = userService.otpVerify(username, otp);
        return map;
    }

}
