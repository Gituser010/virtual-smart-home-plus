package io.patriotframework.virtualsmarthomeplus.house.devices.finalDevices;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.patriot_framework.generator.device.impl.basicActuators.BasicActuator;
import io.patriot_framework.generator.device.passive.actuators.stateMachine.StateMachine;
import io.patriotframework.virtualsmarthomeplus.DTOs.DeviceDTO;
import io.patriotframework.virtualsmarthomeplus.DTOs.LightDTO;
import io.patriotframework.virtualsmarthomeplus.house.devices.Device;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Light extends Device {

    /**
     * Status of the light when it is on.
     */
    public static final String ON = "on";
    /**
     * Status of the light when it is off.
     */
    public static final String OFF = "off";

    /**
     * Light actuator that controls the light.
     */
    public final BasicActuator light = new BasicActuator("light");

    /**
     * Creates new light with given label.
     *
     * @param label label creates identity of the light and is compared in the equals method
     * @throws IllegalArgumentException if given label is null or blank
     */
    @JsonCreator
    public Light(String label) {
        super(label);
        light.setStateMachine(new StateMachine.Builder()
                .from(ON)
                    .to(OFF, "turn_off")
                .from(OFF)
                    .to(ON, "turn_on")
                .build()
        );
    }

    /**
     * Creates new light with the same values of the attributes as given light except label.
     * Label of the new light is given by parameter.
     *
     * @param origLight new light copies values of given light
     * @param newLabel  label creates identity of the light and is compared in the equals method
     * @throws IllegalArgumentException if given label is null or blank
     */
    @JsonCreator
    public Light(Light origLight, String newLabel) {
        super(origLight, newLabel);
        light.setStateMachine(new StateMachine.Builder()
                .from(ON)
                    .to(OFF, "turn_off")
                .from(OFF)
                    .to(ON, "turn_on")
                .build()
        );
    }

    /**
     * Turns on the light.
     */
    public void turnOn() {
        light.controlSignal("turn_on");
    }

    /**
     * * Turns off the light.
     */
    public void turnOff() {
        light.controlSignal("turn_off");
    }

    /**
     * Updates the light status.
     *
     * @param lightDTO DTO with new light status
     */
    public void update(DeviceDTO lightDTO) {
        if (((LightDTO)lightDTO).getStatus().equals(ON)) {
            if(light.requestData().get(0).get(String.class).equals(OFF))
                turnOn();
        } else {
            if(light.requestData().get(0).get(String.class).equals(ON))
                turnOff();
        }
        super.update(lightDTO);
    }

    /**
     * Returns the info about the light.
     *
     * @return {@link #ON} if the light is on, {@link #OFF} otherwise
     */
    public String getStatus() {
        return light.requestData().get(0).get(String.class);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Device createWithSameAttributes(String newLabel) {
        return new Light(this, newLabel);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasSameAttributes(Device device) throws IllegalArgumentException {
        if (device == null) {
            throw new IllegalArgumentException("Device cannot be null");
        }
        if (getClass() != device.getClass()) {
            throw new IllegalArgumentException("Device must be of class Light");
        }
        if (isEnabled() != device.isEnabled()) {
            return false;
        }
        System.out.println("Light");
        System.out.println(getStatus());
        System.out.println(((Light) device).getStatus());
        System.out.println("End");
        return getStatus().equals(((Light) device).getStatus());
    }
}
