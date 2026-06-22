package com.example.demo.Config.auth.handler.provider;

import java.util.Map;

public interface OAuth2UserInfo {
    String getName();
    String getEmail();
    String getProvider();
    String getProviderId();
    Map<String,Object> getAttributes();
}
