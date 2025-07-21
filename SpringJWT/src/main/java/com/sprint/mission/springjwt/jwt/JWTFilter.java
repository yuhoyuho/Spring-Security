package com.sprint.mission.springjwt.jwt;

import com.sprint.mission.springjwt.dto.CustomUserDetails;
import com.sprint.mission.springjwt.entity.User;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;

@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String accessToken = request.getHeader("access");

        // 토큰이 없다면 다음 필터로 넘김
        if(accessToken == null) {
            filterChain.doFilter(request, response);
            return;
        }

        // 토큰 만료 확인. 만료된 경우 다음 필터로 넘기지 않음.
        try {
            jwtUtil.isExpired(accessToken);
        } catch (ExpiredJwtException e) {

            // response body
            PrintWriter writer = response.getWriter();
            writer.write("AccessToken Expired !!");

            // response status code
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String category = jwtUtil.getCategory(accessToken);

        // access 토큰인지 확인
        if(!category.equals("access")) {

            // response body
            PrintWriter writer = response.getWriter();
            writer.write("Invalid access token !!");

            // response status code
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        System.out.println("token ok");
        String username = jwtUtil.getUsername(accessToken);
        String role = jwtUtil.getRole(accessToken);

        User user = new User();
        user.setUsername(username);
        user.setRole(role);
        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);

//        -- 단일 토큰 발급 방법 --
//        String authorization = request.getHeader("Authorization");
//
//        if (authorization == null || !authorization.startsWith("Bearer ")) {
//            System.out.println("Token null !!");
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        System.out.println("Token Create !!");
//        String token = authorization.split(" ")[1]; // Bearer String인증토큰 형식으로 구성되어 있기 때문에 " "뒤에 오는 부분이 인증토큰 부분이다.
//
//        if(jwtUtil.isExpired(token)) {
//            System.out.println("Token Expired !!");
//            filterChain.doFilter(request, response);
//
//            return;
//        }
//
//        String username = jwtUtil.getUsername(token);
//        String role = jwtUtil.getRole(token);
//
//        User user = new User();
//        user.setUsername(username);
//        user.setPassword("12341234");
//        user.setRole(role);
//
//        CustomUserDetails customUserDetails = new CustomUserDetails(user);
//
//        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
//        SecurityContextHolder.getContext().setAuthentication(authToken); // 유저 세션 생성
//
//        filterChain.doFilter(request, response);
    }
}
