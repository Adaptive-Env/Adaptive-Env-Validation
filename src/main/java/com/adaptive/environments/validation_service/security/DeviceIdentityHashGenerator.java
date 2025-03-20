package com.adaptive.environments.validation_service.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Component
public class DeviceIdentityHashGenerator {

    private final String secretKey;

    public DeviceIdentityHashGenerator(@Value("${validation.integrity.secret-key}") String secretKey) {
        this.secretKey = secretKey;
    }

    public String hash(String deviceId) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");
            mac.init(keySpec);
            byte[] rawHmac = mac.doFinal(deviceId.getBytes());
            return Base64.getEncoder().encodeToString(rawHmac);
        } catch (Exception e) {
            throw new RuntimeException("Could not generate hash for device ID", e);
        }
    }
}