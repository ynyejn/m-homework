package com.musinsa.homework.common.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // CSRF 보안 설정 비활성화
                .headers(headers -> headers // h2-console 사용을 위한 설정
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)
                )
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/v1/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()// Swagger 관련 리소스에 대한 접근 허용
                                .requestMatchers("/","/page/**").permitAll()              // 페이지 이동을 위한 설정
                                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll() // 정적 리소스에 대한 접근 허용
                                .requestMatchers(PathRequest.toH2Console()).permitAll() // h2-console 사용을 위한 설정
                                .anyRequest().authenticated()
                );

        return http.build();
    }
}