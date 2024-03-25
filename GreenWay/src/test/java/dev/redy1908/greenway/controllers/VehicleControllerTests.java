package dev.redy1908.greenway.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.redy1908.greenway.delivery_man.domain.IDeliveryManService;
import dev.redy1908.greenway.vehicle.domain.IVehicleService;
import dev.redy1908.greenway.vehicle.domain.Vehicle;
import dev.redy1908.greenway.vehicle.domain.dto.VehicleDTO;
import dev.redy1908.greenway.vehicle.web.VehicleController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VehicleController.class)
class VehicleControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IVehicleService vehicleService;

    @MockBean
    private IDeliveryManService deliveryManService;

    @Test
    void it_should_save_vehicle_status_201_created() throws Exception {
        VehicleDTO vehicleDTO = new VehicleDTO("Model", 100.0, 10.0, 50.0, 200.0);

        Vehicle vehicle = new Vehicle("Model", 100.0, 10.0, 50.0, 200.0);
        vehicle.setId(1L);

        when(vehicleService.saveVehicle(vehicleDTO)).thenReturn(vehicle);

        ResultActions resultActions = mockMvc.perform(post("/api/v1/vehicles")
                        .with(jwt().authorities(new SimpleGrantedAuthority("GREEN_WAY_ADMIN")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(vehicleDTO)));

        resultActions
                .andExpect(status().isCreated())
                .andExpect(header().stringValues("Location", "http://localhost/api/v1/vehicles/1"));
    }

    @Test
    void it_should_get_vehicle_status_200_OK() throws Exception {

        VehicleDTO vehicleDTO = new VehicleDTO("Model", 100.0, 10.0, 50.0, 200.0);

        when(vehicleService.getVehicleById(1L)).thenReturn(vehicleDTO);

        ResultActions resultActions = mockMvc.perform(get("/api/v1/vehicles/1")
                .with(jwt().authorities(new SimpleGrantedAuthority("GREEN_WAY_ADMIN"))));

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("model").value(vehicleDTO.model()));
    }
}
