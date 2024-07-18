package io.patriotframework.virtualsmarthomeplus.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.patriotframework.virtualsmarthomeplus.DTOs.DeviceDTO;
import io.patriotframework.virtualsmarthomeplus.DTOs.FireplaceDTO;
import io.patriotframework.virtualsmarthomeplus.DTOs.LightDTO;
import io.patriotframework.virtualsmarthomeplus.Mapper.DTOMapper;
import io.patriotframework.virtualsmarthomeplus.VirtualSmartHomePlusApplication;
import io.patriotframework.virtualsmarthomeplus.house.House;
import io.patriotframework.virtualsmarthomeplus.house.devices.finalDevices.Light;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static io.patriotframework.virtualsmarthomeplus.house.devices.finalDevices.Light.OFF;
import static io.patriotframework.virtualsmarthomeplus.house.devices.finalDevices.Light.ON;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = VirtualSmartHomePlusApplication.class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LightApiTest {

    private final DTOMapper dtoMapper = new DTOMapper(new ModelMapper());
    @Autowired
    public House house = new House();
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext wac;

    @BeforeAll
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void shouldFetchDevice() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        LightDTO lightDTO = (LightDTO) dtoMapper.map(house.getDevice("light1"));
        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v0.1/house/device/light/light1")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(200))
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        LightDTO responseDTO = objectMapper.readValue(responseBody, LightDTO.class);

        assertEquals(lightDTO, responseDTO);
    }

    @Test
    public void shouldPostFireplace() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        LightDTO lightDTO = new LightDTO();
        lightDTO.setEnabled(true);
        lightDTO.setStatus(ON);
        lightDTO.setLabel("light2");

        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v0.1/house/device/light/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(lightDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(200))
                .andReturn();

        DeviceDTO deviceDTO = dtoMapper.map(house.getDevice("light2"));
        assertEquals(lightDTO,deviceDTO);

        LightDTO lightDTO2 = new LightDTO();
        lightDTO2.setEnabled(true);
        lightDTO2.setStatus(OFF);
        lightDTO2.setLabel("light3");

        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v0.1/house/device/light/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(lightDTO2))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(200));
        deviceDTO = dtoMapper.map(house.getDevice("light3"));
        assertEquals(lightDTO2.getStatus(),((LightDTO)deviceDTO).getStatus());
        assertEquals(lightDTO2,deviceDTO);
    }

    @Test
    public void shouldUpdateLight() throws Exception {
        LightDTO lightDTO = new LightDTO();
        lightDTO.setLabel("light1");
        lightDTO.setStatus(ON);
        lightDTO.setEnabled(true);
        ObjectMapper objectMapper = new ObjectMapper();
        this.mockMvc.perform(MockMvcRequestBuilders.put("/api/v0.1/house/device/light/light1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(lightDTO)))
                .andExpect(status().isOk());
        DeviceDTO deviceDTO = dtoMapper.map(house.getDevice("light1"));
        assertEquals(lightDTO.getStatus(), ON);
        assertEquals(lightDTO, deviceDTO);

        lightDTO.setLabel("light2");
        lightDTO.setStatus(ON);

        this.mockMvc.perform(MockMvcRequestBuilders.put("/api/v0.1/house/device/light/light2")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(lightDTO)))
                .andExpect(status().isOk());
        deviceDTO = dtoMapper.map(house.getDevice("light2"));
        assertEquals(((LightDTO)deviceDTO).getStatus(), ON);

        lightDTO = new LightDTO();
        lightDTO.setLabel("light1");
        lightDTO.setStatus(OFF);
        lightDTO.setEnabled(true);

        this.mockMvc.perform(MockMvcRequestBuilders.put("/api/v0.1/house/device/light/light1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(lightDTO)))
                .andExpect(status().isOk());
        deviceDTO = dtoMapper.map(house.getDevice("light1"));
        assertEquals(((LightDTO)deviceDTO).getStatus(), OFF);
        assertEquals(deviceDTO.getEnabled(), true);

        lightDTO = new LightDTO();
        lightDTO.setLabel("2");

        this.mockMvc.perform(MockMvcRequestBuilders.put("/api/v0.1/house/device/light/light1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(lightDTO)))
                .andExpect(status().is(400));
    }

    @Test
    public void shouldDeleteMapping() throws Exception {
        Light light = new Light("1");
        house.addDevice(light);
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/api/v0.1/house/device/light/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(204));

        assertNull(house.getDevice("1"));
    }

    @Test
    public void deviceAlreadyExists() throws Exception {
        LightDTO lightDTO = new LightDTO();
        lightDTO.setLabel("light1");
        lightDTO.setStatus(ON);
        lightDTO.setEnabled(true);

        ObjectMapper objectMapper = new ObjectMapper();
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v0.1/house/device/light/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(lightDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(409));
    }

    @Test
    public void wrongApiVersion() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v0/house/device/light/light1")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(404));
    }

    @Test
    public void noSuchElementException() throws Exception {
        house.removeDevice("light2");
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v0.1/house/device/light/light2")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(404));
    }

    @Test
    public void wrongLabel() throws Exception {
        LightDTO lightDTO = new LightDTO();
        lightDTO.setLabel(null);
        lightDTO.setStatus(ON);
        lightDTO.setEnabled(true);

        ObjectMapper objectMapper = new ObjectMapper();
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v0.1/house/device/light/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(lightDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(400));
    }
}
