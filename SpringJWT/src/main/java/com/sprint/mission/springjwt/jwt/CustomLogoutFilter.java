package com.sprint.mission.springjwt.jwt;

import com.sprint.mission.springjwt.repository.RefreshRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@RequiredArgsConstructor
public class CustomLogoutFilter extends GenericFilterBean {

    private final JWTUtil jwtUtil;

    private final RefreshRepository refreshRepository;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        doFilter((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse, filterChain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        //path and method verify
        String requestUri = request.getRequestURI();
        if(!requestUri.matches("^\\/logout$")) {
            filterChain.doFilter(request, response);
            return;
        }

        String requestMethod = request.getMethod();
        if(!requestMethod.equals("POST")) {
            filterChain.doFilter(request, response);
            return;
        }

        // get refresh token
        String refresh = null;
        Cookie[] cookies = request.getCookies();

        for (Cookie cookie : cookies) {
            if(cookie.getName().equals("refresh")) {
                refresh = cookie.getValue();
            }
        }

        // refresh null check
        if(refresh == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // expired check
        try {
            jwtUtil.isExpired(refresh);
        } catch(ExpiredJwtException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // check refresh
        String category = jwtUtil.getCategory(refresh);

        if (!category.equals("refresh")) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // db check
        boolean isExist = refreshRepository.existsByRefresh(refresh);
        if(!isExist) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // 로그아웃 진행
        // db에서 refresh 토큰 제거
        refreshRepository.deleteByRefresh(refresh);

        Cookie cookie = new Cookie("refresh", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");

        response.addCookie(cookie);
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
