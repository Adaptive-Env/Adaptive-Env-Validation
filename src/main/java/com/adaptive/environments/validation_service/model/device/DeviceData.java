package com.adaptive.environments.validation_service.model.device;

public interface DeviceData {
    String getDeviceId();
    String getType();
    Long getTimestamp();
    String getAuthKey();
}
