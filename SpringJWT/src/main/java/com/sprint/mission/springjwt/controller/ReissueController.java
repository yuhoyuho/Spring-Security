package com.sprint.mission.springjwt.controller;

import com.sprint.mission.springjwt.entity.RefreshEntity;
import com.sprint.mission.springjwt.jwt.JWTUtil;
import com.sprint.mission.springjwt.repository.RefreshRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequiredArgsConstructor
public class ReissueController {

    private final JWTUtil jwtUtil;

    private final RefreshRepository refreshRepository;

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {

        // get refresh token
        String refresh = null;
        Cookie[] cookies = request.getCookies();

        for (Cookie cookie : cookies) {
            if(cookie.getName().equals("refresh")) {
                refresh = cookie.getValue();
            }
        }

        if(refresh == null) {
            // response status code
            return new ResponseEntity<>("refresh token null !!", HttpStatus.BAD_REQUEST);
        }

        // expired check
        try {
            jwtUtil.isExpired(refresh);
        } catch(ExpiredJwtException e) {
            // response status code
            return new ResponseEntity<>("access token expired !!", HttpStatus.BAD_REQUEST);
        }

        // 토큰이 refresh인지 확인 (토큰을 발급하는 경우 payload에 명시)
        String category = jwtUtil.getCategory(refresh);

        if (!category.equals("refresh")) {
            // response status code
            return new ResponseEntity<>("Invalid refresh token !!", HttpStatus.BAD_REQUEST);
        }

        // db에 refresh 토큰이 저장되어 있는지 확인
        boolean isExist = refreshRepository.existsByRefresh(refresh);
        if(!isExist) {
            //response body
            return new ResponseEntity<>("Invalid refresh token !!", HttpStatus.BAD_REQUEST);
        }

        String username = jwtUtil.getUsername(refresh);
        String role = jwtUtil.getRole(refresh);

        // access 토큰 재발급
        String newAccessToken = jwtUtil.createToken("access", username, role, 600000L);
        String newRefreshToken = jwtUtil.createToken("refresh", username, role, 86400000L);

        refreshRepository.deleteByRefresh(refresh);
        addRefreshEntity(username, newRefreshToken, 86400000L);

        // response
        response.setHeader("access", newAccessToken);
        response.addCookie(createCookie("refresh", newRefreshToken));

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void addRefreshEntity(String username, String refresh, Long expiredTime) {
        Date date = new Date(System.currentTimeMillis() + expiredTime);

        RefreshEntity refreshEntity = new RefreshEntity();
        refreshEntity.setUsername(username);
        refreshEntity.setRefresh(refresh);
        refreshEntity.setExpiration(date.toString());

        refreshRepository.save(refreshEntity);
    }

    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24 * 60 * 60);
        cookie.setHttpOnly(true);
//        cookie.setSecure(true); -> https 통신시 주석 헤재
//        cookie.setPath("/"); -> cookie가 적용될 장소

        return cookie;
    }
}
