package io.patriotframework.virtualsmarthomeplus.house.devices.finalDevices;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.patriot_framework.generator.device.impl.basicActuators.BasicActuator;
import io.patriot_framework.generator.device.passive.actuators.stateMachine.StateMachine;
import io.patriotframework.virtualsmarthomeplus.DTOs.DeviceDTO;
import io.patriotframework.virtualsmarthomeplus.DTOs.DoorDTO;
import io.patriotframework.virtualsmarthomeplus.house.House;
import io.patriotframework.virtualsmarthomeplus.house.devices.Device;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Representation of door device.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Door extends Device {

    public static final String OPENED = "opened";
    public static final String CLOSED = "closed";
    private static final Logger LOGGER = LoggerFactory.getLogger(House.class);

    private final BasicActuator door = new BasicActuator("door");
    /**
     * Creates new door with given label.
     *
     * @param label label creates identity of the door and is compared in the equals method
     */
    @JsonCreator
    public Door(String label) {
        super(label);
        door.setStateMachine(new StateMachine.Builder()
                .from(OPENED)
                    .to(CLOSED, "close")
                .from(CLOSED)
                    .to(OPENED, "open")
                .build());
    }

    /**
     * Creates new door with the same values of the attributes as given door except label.
     * Label of the new door is given by parameter.
     *
     * @param origDoor new door copies values of given device
     * @param newLabel label creates identity of the door and is compared in the equals method
     * @throws IllegalArgumentException if given label is null or blank
     */
    public Door(Door origDoor, String newLabel) {
        super(origDoor, newLabel);
        door.setStateMachine(new StateMachine.Builder()
                .from(OPENED)
                    .to(CLOSED, "close")
                .from(CLOSED)
                    .to(OPENED, "open")
                .build());
    }

    /**
     * Opens the door.
     */
    public void open() {
        door.controlSignal("open");
    }

    /**
     * Closes the door.
     */
    public void close() {
        door.controlSignal("close");
    }

    /**
     * Returns info about the door.
     *
     * @return {@link #OPENED} if the door is opened, {@link #CLOSED} otherwise
     */
    public String getStatus() {
        return door.requestData().get(0).get(String.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Door createWithSameAttributes(String newLabel) {
        return new Door(this, newLabel);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasSameAttributes(Device door) throws IllegalArgumentException {
        if (door == null) {
            throw new IllegalArgumentException("Door cannot be null");
        }
        if (getClass() != door.getClass()) {
            throw new IllegalArgumentException("device must be of class Door");
        }

        final Door typedDoor = (Door) door;

        if (isEnabled() != typedDoor.isEnabled()) {
            return false;
        }
        return getStatus().equals(typedDoor.getStatus());
    }
    /**
     * Updates the door object with the values from provided DTO.
     *
     * @param deviceDTO door DTO containing the updated values or null if value was not updated
     */
    public void update(DeviceDTO deviceDTO) {
        final DoorDTO doorDTO = (DoorDTO) deviceDTO;
        if (doorDTO.getStatus() != null) {
            if (doorDTO.getStatus().equals(OPENED)) {
                if(getStatus().equals(CLOSED))
                    this.open();
            } else if (doorDTO.getStatus().equals(CLOSED)) {
                if (getStatus().equals(OPENED))
                    this.close();
            }
        }
        super.update(doorDTO);
    }
}
