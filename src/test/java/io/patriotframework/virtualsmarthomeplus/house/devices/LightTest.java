package io.patriotframework.virtualsmarthomeplus.house.devices;

import io.patriotframework.virtualsmarthomeplus.house.devices.finalDevices.Light;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LightTest {
    private final Light light1 = new Light("light1");
    private final Light light2 = new Light("light2");


    @Test
    public void constructorTest() {
        assertDoesNotThrow(() -> {
            Light device2 = new Light("light1");
        });
        assertThrows(IllegalArgumentException.class, () -> new Light(""));
        assertThrows(IllegalArgumentException.class, () -> new Light(null));
    }

    @Test
    public void turnOnOffTest() {
        light1.setEnabled(true);
        light1.turnOn();
        assertEquals(light1.getStatus(), Light.ON);
        light1.turnOff();
        assertEquals(light1.getStatus(), Light.OFF);
        light1.turnOn();
        light1.turnOn();
        assertEquals(light1.getStatus(), Light.ON);

        light1.turnOff();
        light1.turnOff();
        assertEquals(light1.getStatus(), Light.OFF);
    }

    @Test
    public void createWithSameAttributes() {
        light1.setEnabled(true);
        Light light3 = (Light) light1.createWithSameAttributes("rgb3");
        assertEquals(light3.isEnabled(), light1.isEnabled());
        assertTrue(light3.hasSameAttributes(light1));
    }

    @Test
    public void testHasSameAttributes() {
        DeviceMock device1 = new DeviceMock("light1");
        assertThrows(IllegalArgumentException.class, () -> light1.hasSameAttributes(device1));

        assertThrows(IllegalArgumentException.class, () -> light1.hasSameAttributes(null));

        assertTrue(light1.hasSameAttributes(light2));
        light1.turnOff();
        assertFalse(light1.hasSameAttributes(light2));
        light1.turnOn();
    }
}
