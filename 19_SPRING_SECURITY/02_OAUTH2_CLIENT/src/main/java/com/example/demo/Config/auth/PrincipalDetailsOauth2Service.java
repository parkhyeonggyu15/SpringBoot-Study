package com.example.demo.Config.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PrincipalDetailsOauth2Service extends DefaultOAuth2UserService {
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
//        System.out.println("userRequest : " + userRequest);
//        System.out.println("userRequest.getClientRegistration : " + userRequest.getClientRegistration());
//        System.out.println("userRequest.getAccessToken() : " + userRequest.getAccessToken());
//        System.out.println("userRequest.getAdditionalParameters() : " + userRequest.getAdditionalParameters());
//        System.out.println("userRequest.getAccessToken().getTokenValue() : " + userRequest.getAccessToken().getTokenValue());
//        System.out.println("userRequest.getAccessToken().getTokenType().getValue() : " + userRequest.getAccessToken().getTokenType().getValue());
//        System.out.println("userRequest.getAccessToken().getScopes() : " + userRequest.getAccessToken().getScopes());
        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println("oAuth2User : " + oAuth2User);
        System.out.println("oAuth2User.getAttributes() : " + oAuth2User.getAttributes());
        System.out.println("Provider Name : " + userRequest.getClientRegistration().getClientName());
        return oAuth2User;
    }
}
