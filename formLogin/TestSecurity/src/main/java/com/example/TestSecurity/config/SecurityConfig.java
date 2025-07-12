package com.example.TestSecurity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                    .requestMatchers("/", "/login", "/join")
                    .permitAll()
                    .requestMatchers("/admin")
                    .hasRole("ADMIN")
                    .requestMatchers("/my/**")
                    .hasAnyRole("ADMIN", "USER")
                    .anyRequest()
                    .authenticated()
        );

        http.formLogin(auth -> auth
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .permitAll()
        );

//        HTTP 로그인 방식
//        http.httpBasic(Customizer.withDefaults());

//        http.csrf(auth -> auth.disable());
        // csrf => 로그인 시 csrf 토큰도 함께 보내주어야 로그인이 진행됨.

        http.sessionManagement(auth -> auth
                        .maximumSessions(1)
                        .maxSessionsPreventsLogin(true)
        );

        http.sessionManagement(auth -> auth
                        .sessionFixation()
                        .changeSessionId()
        );

        http.logout(auth -> auth
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
        );

        return http.build();
    }
}
