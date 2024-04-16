package com.profile.in.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfigApp {

	@Autowired
	private UserDetailsService service;

	@Autowired
	private BCryptPasswordEncoder encoder;

	@Autowired
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(service).passwordEncoder(encoder);
	}

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		RequestMatcher optionsMatcher = new AntPathRequestMatcher("/**", HttpMethod.OPTIONS.name());
		http.authorizeHttpRequests(auth -> auth
				.requestMatchers(optionsMatcher).permitAll()
				.requestMatchers("/css/**", "/js/**").permitAll()
				.dispatcherTypeMatchers().permitAll()
				.requestMatchers("/auth/**").permitAll()
				.requestMatchers(new AntPathRequestMatcher("/**")).authenticated());
		http.formLogin(httpSecurityFormLoginConfigurer ->
				httpSecurityFormLoginConfigurer
						.loginPage("/auth/loginPage")
						.loginProcessingUrl("/login")
						.defaultSuccessUrl("/profile/home")
						.failureUrl("/auth/loginPage?error"));
		http.logout(httpSecurityLogoutConfigurer ->
				httpSecurityLogoutConfigurer.logoutUrl("/auth/signout")
						.logoutSuccessUrl("/auth/loginPage?logout"));
		http.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
				.maximumSessions(1);
		http.csrf(AbstractHttpConfigurer::disable);
		return http.build();
	}
}
