package com.adaptive.environments.validation_service.security;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class AuthKeyRegistry {
    private final Map<String, String> authKeys = new HashMap<String, String>();

    public AuthKeyRegistry() {
        authKeys.put("admin", "admin");
        authKeys.put("pump-01", "secret-key");

    }

    public boolean isValid(String deviceId, String authKey) {
        return authKey != null && authKey.equals(authKeys.get(deviceId));
    }
}