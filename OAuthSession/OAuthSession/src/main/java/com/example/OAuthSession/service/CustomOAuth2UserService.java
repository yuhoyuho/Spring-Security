package com.example.OAuthSession.service;

import com.example.OAuthSession.entity.User;
import com.example.OAuthSession.dto.CustomOAuth2User;
import com.example.OAuthSession.dto.GoogleResponse;
import com.example.OAuthSession.dto.NaverResponse;
import com.example.OAuthSession.dto.OAuth2Response;
import com.example.OAuthSession.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {

        OAuth2User user = super.loadUser(request);
        System.out.println(user.getAttributes());

        String registrationId = request.getClientRegistration().getRegistrationId(); // naver, google, kakao등 어디서 온 데이터인지 판단
        OAuth2Response oAuth2Response = null;

        if(registrationId.equals("naver")) {
            oAuth2Response = new NaverResponse(user.getAttributes());
        }
        else if(registrationId.equals("google")) {
            oAuth2Response = new GoogleResponse(user.getAttributes());
        }
        else {
            return null;
        }

        String username = oAuth2Response.getProvider() + " " + oAuth2Response.getProviderId();

        User existedUser = userRepository.findByUsername(username);
        String role = "ROLE_USER";

        if(existedUser == null) {
            User newUser = new User();
            newUser.setUsername(username);
            newUser.setEmail(oAuth2Response.getEmail());
            newUser.setRole(role);

            userRepository.save(newUser);
        }
        else {
            role = existedUser.getRole();

            existedUser.setUsername(username);
            existedUser.setEmail(oAuth2Response.getEmail());

            userRepository.save(existedUser);
        }

        return new CustomOAuth2User(oAuth2Response, role);
    }
}
