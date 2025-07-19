package com.example.OAuthSession.dto;

public interface OAuth2Response {

    String getProvider(); // 제공자 이름

    String getProviderId(); // 제공자 측에서 각 유저에 부여한 번호

    String getEmail(); // 이메일

    String getName(); // 사용자 이름
}
