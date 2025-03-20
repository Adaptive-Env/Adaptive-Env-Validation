package com.adaptive.environments.validation_service.model.device;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ValidatedData {
    private DeviceData deviceData;
    private String hash;


}
