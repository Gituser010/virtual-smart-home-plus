package io.patriotframework.virtualsmarthomeplus.controllers;

import io.patriotframework.virtualsmarthomeplus.APIRoutes;
import io.patriotframework.virtualsmarthomeplus.APIVersions;
import io.patriotframework.virtualsmarthomeplus.DTOs.DeviceDTO;
import io.patriotframework.virtualsmarthomeplus.DTOs.LightDTO;
import io.patriotframework.virtualsmarthomeplus.house.House;
import io.patriotframework.virtualsmarthomeplus.house.devices.finalDevices.Light;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;


@RestController
public class LightController extends FinalDeviceHandling {
    private static final String LIGHT_ID_ROUTE = APIRoutes.LIGHT_ROUTE + "/{label}";

    LightController(House house) {
        super(house);
    }

    /**
     * Creates the Light
     *
     * @param device     new Light DTO
     * @param apiVersion api version specified in route
     * @return Light added to the house
     */
    @PostMapping(APIRoutes.LIGHT_ROUTE)
    public ResponseEntity<DeviceDTO> postLed(@Valid @RequestBody LightDTO device, @PathVariable String apiVersion) {
        if (apiVersion.equals(APIVersions.V0_1)) {
            return new ResponseEntity<>(handlePost(device), HttpStatus.OK);
        }
        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, String.format("Unknown api version: %s", apiVersion) // 404
        );
    }

    /**
     * Returns the Light
     *
     * @param label      label specified in route
     * @param apiVersion api version specified in route
     * @return light if present in the house
     */
    @GetMapping(LIGHT_ID_ROUTE)
    public ResponseEntity<DeviceDTO> getLed(@PathVariable @NotNull String label, @PathVariable String apiVersion) {
        if (apiVersion.equals(APIVersions.V0_1)) {
            return new ResponseEntity<>(handleGet(label, Light.class), HttpStatus.OK);
        }
        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, String.format("Unknown api version: %s", apiVersion) // 404
        );
    }

    /**
     * Updates or creates the Light
     *
     * @param device     updated Light DTO
     * @param apiVersion api version specified in route
     * @param label label of the Light to be created
     * @return Light updated or added to the house
     */
    @PutMapping(LIGHT_ID_ROUTE)
    public ResponseEntity<DeviceDTO> putLed(
            @Valid @RequestBody LightDTO device,
            @PathVariable @NotNull String label,
            @PathVariable String apiVersion
    ) {
        if (apiVersion.equals(APIVersions.V0_1)) {
            return new ResponseEntity<>(handlePut(label, device), HttpStatus.OK);
        }
        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, String.format("Unknown api version: %s", apiVersion) // 404
        );
    }


    /**
     * Deletes the Light
     *
     * @param label      label of the Light to be deleted
     * @param apiVersion api version specified in route
     * @return "OK" if Light exists in the house and was deleted
     */
    @DeleteMapping(LIGHT_ID_ROUTE)
    public ResponseEntity<HttpStatus> deleteLight(
            @PathVariable @NotNull String label,
            @PathVariable String apiVersion
    ) {
        if (apiVersion.equals(APIVersions.V0_1)) {
            handleDelete(label, Light.class);

            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, String.format("Unknown api version: %s", apiVersion) // 404
        );
    }
}
