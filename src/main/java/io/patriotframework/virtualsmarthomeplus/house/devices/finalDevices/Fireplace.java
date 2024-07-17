package io.patriotframework.virtualsmarthomeplus.house.devices.finalDevices;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.patriot_framework.generator.device.impl.basicActuators.BasicActuator;
import io.patriot_framework.generator.device.passive.actuators.stateMachine.StateMachine;
import io.patriotframework.virtualsmarthomeplus.DTOs.DeviceDTO;
import io.patriotframework.virtualsmarthomeplus.DTOs.FireplaceDTO;
import io.patriotframework.virtualsmarthomeplus.house.House;
import io.patriotframework.virtualsmarthomeplus.house.devices.Device;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Representation of fireplace device.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Fireplace extends Device {

    public static final String ON_FIRE = "on_fire";
    public static final String EXTINGUISHED = "extinguished";
    private static final Logger LOGGER = LoggerFactory.getLogger(House.class);

    private final BasicActuator fireplace = new BasicActuator("fireplace");

    /**
     * Creates new fireplace with given label.
     *
     * @param label label creates identity of the fireplace and is compared in the equals method
     */
    @JsonCreator
    public Fireplace(String label) {
        super(label);
        fireplace.setStateMachine(new StateMachine.Builder()
                .from(ON_FIRE)
                    .to(EXTINGUISHED, "extinguish")
                .from(EXTINGUISHED)
                    .to(ON_FIRE, "fire_up")
                .build()
        );
    }

    /**
     * Creates new fireplace with the same values of the attributes as given door except label.
     * Label of the new fireplace is given by parameter.
     *
     * @param origFireplace new fireplace copies values of given fireplace
     * @param newLabel      label creates identity of the fireplace and is compared in the equals method
     * @throws IllegalArgumentException if given label is null or blank
     */
    public Fireplace(Fireplace origFireplace, String newLabel) {
        super(origFireplace, newLabel);
        fireplace.setStateMachine(new StateMachine.Builder()
                .from(ON_FIRE)
                .to(EXTINGUISHED, "extinguish")
                .from(EXTINGUISHED)
                .to(ON_FIRE, "fire_up")
                .build()
        );
    }

    /**
     * Fires up the fireplace.
     */
    public void fireUp() {
        fireplace.controlSignal("fire_up");
    }

    /**
     * Extinguishes the fireplace.
     */
    public void extinguish() {
        fireplace.controlSignal("extinguish");
    }

    /**
     * Returns the info about the fireplace.
     *
     * @return {@link #ON_FIRE} if the fireplace is lit, {@link #EXTINGUISHED} otherwise
     */
    @JsonIgnore
    public String getStatus() {
        try {
            return fireplace.requestData().get(0).get(String.class);
        } catch (NullPointerException e) {
            LOGGER.error("Fireplace status is null");
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Fireplace createWithSameAttributes(String newLabel) {
        return new Fireplace(this, newLabel);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasSameAttributes(Device fireplace) throws IllegalArgumentException {
        if (fireplace == null) {
            throw new IllegalArgumentException("Fireplace cannot be null");
        }
        if (getClass() != fireplace.getClass()) {
            throw new IllegalArgumentException("device must be of class Fireplace");
        }

        final Fireplace typedFireplace = (Fireplace) fireplace;

        if (isEnabled() != typedFireplace.isEnabled()) {
            return false;
        }
        return  ((Fireplace) fireplace).getStatus().equals(getStatus());
    }
    /**
     * Updates the fireplace object with the values from provided DTO.
     *
     * @param deviceDTO fireplace DTO containing the updated values or null if value was not updated
     */
    public void update(DeviceDTO deviceDTO) {
        final FireplaceDTO fireplaceDTO = (FireplaceDTO) deviceDTO;
        if (fireplaceDTO.getStatus() != null) {
            if (fireplaceDTO.getStatus().equals(EXTINGUISHED)) {
                if(fireplace.requestData().get(0).get(String.class).equals(ON_FIRE))
                    this.extinguish();
            } else if (fireplaceDTO.getStatus().equals(ON_FIRE)) {
                if(fireplace.requestData().get(0).get(String.class).equals(EXTINGUISHED))
                    this.fireUp();
            }
        }
        super.update(fireplaceDTO);
    }
}
