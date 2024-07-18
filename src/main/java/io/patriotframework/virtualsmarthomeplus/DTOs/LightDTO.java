package io.patriotframework.virtualsmarthomeplus.DTOs;

import lombok.Getter;
import lombok.Setter;
import java.util.Objects;

@Getter
@Setter
public class LightDTO extends DeviceDTO{

    /**
     * In response to request this attribute has value
     * {@link io.patriotframework.virtualsmarthomeplus.house.devices.finalDevices.Light#ON} or
     * {@link io.patriotframework.virtualsmarthomeplus.house.devices.finalDevices.Light#OFF}.
     */
    private String status;

    public LightDTO() {
        this.setDeviceType("Light");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof final LightDTO that)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(getDeviceType(), that.getDeviceType())
                && Objects.equals(getStatus(), that.getStatus());
    }

}
