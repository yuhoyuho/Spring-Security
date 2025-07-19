package com.example.OAuthSession.config;

import com.example.OAuthSession.oauth2.CustomClientRegistrationRepo;
import com.example.OAuthSession.oauth2.CustomOAuth2AuthorizedClientService;
import com.example.OAuthSession.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;

    private final CustomClientRegistrationRepo customClientRegistrationRepo;

    private final CustomOAuth2AuthorizedClientService customOAuth2AuthorizedClientService;

    private final JdbcTemplate jdbcTemplate;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, OAuth2AuthorizedClientService authorizedClientService) throws Exception {

        http.csrf(auth -> auth.disable());

        http.formLogin(auth -> auth.disable());

        http.httpBasic(auth -> auth.disable());

        http.oauth2Login(auth -> auth
                .loginPage("/login") // 이 부분에 커스텀 로그인 페이지를 명시해야 로그인 시 그 페이지로 이동함
                .clientRegistrationRepository(customClientRegistrationRepo.clientRegistrationRepository())
                .authorizedClientService(customOAuth2AuthorizedClientService.oAuth2AuthorizedClientService(jdbcTemplate, customClientRegistrationRepo.clientRegistrationRepository()))
                .userInfoEndpoint(userInfoEndpointConfig ->
                        userInfoEndpointConfig.userService(customOAuth2UserService))
        );

        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/oauth2/**", "/login/**")
                .permitAll()
                .anyRequest()
                .authenticated()
        );

        return http.build();
    }
}
