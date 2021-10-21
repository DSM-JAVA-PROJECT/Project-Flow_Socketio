package com.projectflow.projectflow.global.security.httpsecurity;

import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
public class JwtConfigure extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final JwtTokenValidator jwtTokenValidator;
//    private final CorsFilter corsFilter;

    @Override
    public void configure(HttpSecurity httpSecurity) {
        HttpJwtTokenFilter jwtTokenFilter = new HttpJwtTokenFilter(jwtTokenValidator);
        httpSecurity.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
//        httpSecurity.addFilterBefore(corsFilter, HttpJwtTokenFilter.class);
    }
}
