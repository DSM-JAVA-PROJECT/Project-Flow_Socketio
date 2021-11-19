package com.projectflow.projectflow.global.security.httpsecurity;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.cors.CorsUtils;

@RequiredArgsConstructor
@Configuration
public class MvcSecurityConfigurer extends WebSecurityConfigurerAdapter {

    private final JwtTokenValidator validator;

    private final CustomAuthenticationEntryPoint entryPoint;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .headers().frameOptions().disable().and()
                .csrf().disable()
                .formLogin().disable()
                .cors().and()
                .apply(new JwtConfigure(validator))
                .and()
                .authorizeRequests()
                .antMatchers("/websocket").permitAll()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                .anyRequest().authenticated().and()
                .exceptionHandling()
                .authenticationEntryPoint(entryPoint);
    }

}
