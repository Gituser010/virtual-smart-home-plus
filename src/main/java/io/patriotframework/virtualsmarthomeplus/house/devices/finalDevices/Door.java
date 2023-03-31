package io.patriotframework.virtualsmarthomeplus.house.devices.finalDevices;

import io.patriotframework.virtualsmarthomeplus.house.House;
import io.patriotframework.virtualsmarthomeplus.house.devices.Device;
import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Representation of door device.
 */
public class Door extends Device {

    private boolean opened = false;
    private static final Logger LOGGER = LoggerFactory.getLogger(House.class);

    public static final String OPENED = "opened";
    public static final String CLOSED = "closed";

    /**
     * Creates new door with given label.
     *
     * @param label label creates identity of the door and is compared in the equals method
     */
    public Door(String label) {
        super(label);
    }

    /**
     * Creates new door with the same values of the attributes as given door except label.
     * Label of the new door is given by parameter.
     *
     * @param origDoor new door copies values of given device
     * @param newLabel   label creates identity of the door and is compared in the equals method
     * @throws IllegalArgumentException if given label is null or blank
     */
    public Door(Door origDoor, String newLabel) {
        super(origDoor, newLabel);
        opened = origDoor.opened;
    }

    /**
     * Opens the door.
     */
    public void open() {
        if (!opened) {
            opened = true;
            LOGGER.debug(String.format("Door %s opened", getLabel()));
        }
    }

    /**
     * Closes the door.
     */
    public void close() {
        if (opened) {
            opened = false;
            LOGGER.debug(String.format("Door %s closed", getLabel()));
        }
    }

    /**
     * Returns info about the door.
     *
     * @return {@link #OPENED} if the door is opened, {@link #CLOSED} otherwise
     */
    public String getStatus() {
        return opened ? OPENED : CLOSED;
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
        if(door == null) {
            throw new IllegalArgumentException("Door cannot be null");
        }
        if(getClass() != door.getClass()) {
            throw new IllegalArgumentException("device must be of class Door");
        }

        Door typedDoor = (Door)door;

        if(isEnabled() != typedDoor.isEnabled()) {
            return false;
        }
        return typedDoor.opened == opened;
    }
}
