package com.sprint.mission.springjwt.jwt;

import com.sprint.mission.springjwt.entity.RefreshEntity;
import com.sprint.mission.springjwt.repository.RefreshRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Date;

@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    private final JWTUtil jwtUtil;

    private final RefreshRepository refreshRepository;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        String username = obtainUsername(request);
        String password = obtainPassword(request);

        System.out.println("username : " + username + " , password : " + password + "");
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, null); // dto에 담아서 Authentication에 전달해줌

        return authenticationManager.authenticate(authToken);
        // authmanager에 담아서 반환해주면 authmanager가 token을 검증해줌
        // 검증 방법 : db에서 유저 정보를 가져와서 UserDetails를 통해 검증을 진행
        // 그림 추가하기
        // 검증 성공시
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain, Authentication authResult) {
//        -- 단일 토큰 발급 방법 --
//        System.out.println("성공");
//        CustomUserDetails customUserDetails = (CustomUserDetails) authResult.getPrincipal();
//
//        String username = customUserDetails.getUsername();
//        String role = customUserDetails.getAuthorities().stream().findFirst().get().getAuthority();
//
//        String token = jwtUtil.createToken(username, role, 60 * 60 * 10L);
//        response.addHeader("Authorization", "Bearer " + token);

        // 사용자 정보
        String username = authResult.getName();
        String role = authResult.getAuthorities().stream().iterator().next().getAuthority();

        String accessToken = jwtUtil.createToken("access", username, role, 600000L);
        String refreshToken = jwtUtil.createToken("refresh", username, role, 86400000L);

        // refresh 토큰 추가
        addRefreshEntity(username, refreshToken, 86400000L);

        // 응답 설정
        response.setHeader("access", accessToken);
        response.addCookie(createCookie("refresh", refreshToken));
        response.setStatus(HttpStatus.OK.value());
    }

    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24 * 60 * 60);
        cookie.setHttpOnly(true);
//        cookie.setSecure(true); -> https 통신시 주석 헤재
//        cookie.setPath("/"); -> cookie가 적용될 장소

        return cookie;
    }

    protected void addRefreshEntity(String username, String refresh, Long expiredTime) {
        Date date = new Date(System.currentTimeMillis() + expiredTime);

        RefreshEntity refreshEntity = new RefreshEntity();
        refreshEntity.setUsername(username);
        refreshEntity.setRefresh(refresh);
        refreshEntity.setExpiration(date.toString());

        refreshRepository.save(refreshEntity);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {

        response.setStatus(401);
    }
}
