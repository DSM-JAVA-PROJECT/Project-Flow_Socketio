package com.projectflow.projectflow.global.security.httpsecurity;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.cors.CorsUtils;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class MvcSecurityConfigurer extends WebSecurityConfigurerAdapter {

    private final JwtTokenValidator validator;

    private final CustomAuthenticationEntryPoint entryPoint;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .headers().frameOptions().disable().and()
                .cors().disable()
                .csrf().disable()
                .formLogin().disable()
                .apply(new JwtConfigure(validator))
                .and()
                .authorizeRequests()
                .antMatchers("/websocket").permitAll()
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                .anyRequest().authenticated().and()
                .exceptionHandling()
                .authenticationEntryPoint(entryPoint);
    }

}
