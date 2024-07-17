package io.patriotframework.virtualsmarthomeplus.Mapper;

import io.patriotframework.virtualsmarthomeplus.DTOS.MockDTO;
import io.patriotframework.virtualsmarthomeplus.DTOs.*;
import io.patriotframework.virtualsmarthomeplus.house.House;
import io.patriotframework.virtualsmarthomeplus.house.devices.DeviceMock;
import io.patriotframework.virtualsmarthomeplus.house.devices.finalDevices.Door;
import io.patriotframework.virtualsmarthomeplus.house.devices.finalDevices.Fireplace;
import io.patriotframework.virtualsmarthomeplus.house.devices.finalDevices.Light;
import io.patriotframework.virtualsmarthomeplus.house.devices.finalDevices.Thermometer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

import static io.patriotframework.virtualsmarthomeplus.house.devices.finalDevices.Door.CLOSED;
import static io.patriotframework.virtualsmarthomeplus.house.devices.finalDevices.Door.OPENED;
import static io.patriotframework.virtualsmarthomeplus.house.devices.finalDevices.Fireplace.EXTINGUISHED;
import static io.patriotframework.virtualsmarthomeplus.house.devices.finalDevices.Fireplace.ON_FIRE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DTOMapperTest {

    private final DTOMapper dtoMapper;

    public DTOMapperTest() {
        this.dtoMapper = new DTOMapper(new ModelMapper());
    }
    @Test
    public void unknownDeviceToDeviceDTO() {
        DeviceMock deviceMock = new DeviceMock("deviceMock");
        deviceMock.setEnabled(true);

        assertThrows(DeviceMappingNotSupportedException.class, () -> dtoMapper.map(deviceMock));
    }

    @Test
    public void thermometerToThermometerDTO() {
        Thermometer thermometer = new Thermometer("thermometer1");
        thermometer.setEnabled(true);
        thermometer.setUnit("F");

        ThermometerDTO thermometerDTO = new ThermometerDTO();
        thermometerDTO.setLabel("thermometer1");
        thermometerDTO.setEnabled(true);
        thermometerDTO.setUnit("F");

        assertEquals(dtoMapper.map(thermometer),thermometerDTO);
    }



    @Test
    public void lightToLightDTO() {
        Light light = new Light("light1");
        light.setEnabled(true);
        light.turnOn();

        LightDTO lightDTO = new LightDTO();
        lightDTO.setEnabled(true);
        lightDTO.setStatus(Light.ON);
        lightDTO.setLabel("light1");

        assertEquals(dtoMapper.map(light), lightDTO);

        light.turnOff();
        lightDTO.setStatus(Light.OFF);

        assertEquals(dtoMapper.map(light), lightDTO);
    }


    @Test
    public void fireplaceToFireplaceDTO() {
        Fireplace fireplace = new Fireplace("fireplace1");
        fireplace.setEnabled(true);
        fireplace.fireUp();

        FireplaceDTO fireplaceDTO = new FireplaceDTO();
        fireplaceDTO.setLabel("fireplace1");
        fireplaceDTO.setStatus(ON_FIRE);
        fireplaceDTO.setEnabled(true);

        assertEquals(dtoMapper.map(fireplace),fireplaceDTO);

        fireplace.extinguish();
        fireplaceDTO.setStatus(EXTINGUISHED);

        assertEquals(dtoMapper.map(fireplace),fireplaceDTO);
    }

    @Test
    public void doorToDoorDTO() {
        Door door = new Door("door1");
        door.setEnabled(true);
        door.open();

        DoorDTO doorDTO = new DoorDTO();
        doorDTO.setLabel("door1");
        doorDTO.setEnabled(true);
        doorDTO.setStatus(OPENED);

        assertEquals(dtoMapper.map(door),doorDTO);

        door.close();
        doorDTO.setStatus(CLOSED);

        assertEquals(dtoMapper.map(door),doorDTO);
    }

    @Test
    public void classDTOToClass() {
        assertEquals(Light.class,dtoMapper.mapDtoClassType(LightDTO.class));
        assertEquals(Door.class,dtoMapper.mapDtoClassType(DoorDTO.class));
        assertEquals(Fireplace.class,dtoMapper.mapDtoClassType(FireplaceDTO.class));
        assertEquals(Thermometer.class,dtoMapper.mapDtoClassType(ThermometerDTO.class));

        assertThrows(DeviceMappingNotSupportedException.class, () -> dtoMapper.mapDtoClassType(MockDTO.class));
    }

    @Test
    public void classToClassDTO() {
        assertEquals(LightDTO.class,dtoMapper.mapDeviceClassType(Light.class));
        assertEquals(DoorDTO.class,dtoMapper.mapDeviceClassType(Door.class));
        assertEquals(FireplaceDTO.class,dtoMapper.mapDeviceClassType(Fireplace.class));
        assertEquals(ThermometerDTO.class,dtoMapper.mapDeviceClassType(Thermometer.class));

        assertThrows(DeviceMappingNotSupportedException.class, () -> dtoMapper.mapDeviceClassType(DeviceMock.class));
    }

    @AfterAll
    @Test
    public void houseToHouseDTO() {
        House house = new House();
        Light light = new Light("light1");
        house.addDevice(light);

        HouseDTO houseDTO = new HouseDTO();
        List<DeviceDTO> list = new ArrayList<>();
        houseDTO.setDevices(list);
        list.add(dtoMapper.map(light));
        houseDTO.setDevices(list);

        HouseDTO houseDTO1 = dtoMapper.map(house);
        assertEquals(houseDTO1,houseDTO);
    }

}
