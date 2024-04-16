package com.profile.in.service;


import com.profile.in.model.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Map;


public interface UserService extends UserDetailsService {
public String regsiter(UserDetails details);

    Map<String, Object> username(String username);

    Map<String, Object> forgetPassword(String username, String password);

    Map<String, Object> otpVerify(String username, String otp);
}
