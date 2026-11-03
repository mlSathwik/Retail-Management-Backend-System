package com.retail.management.controller;

import com.retail.management.dto.StoreDTO;
import com.retail.management.service.ServiceClass;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StoreController.class)
class StoreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ServiceClass serviceClass;

    @Test
    @DisplayName("GET /api/stores returns list of stores")
    void testGetAllStores() throws Exception {
        StoreDTO store1 = StoreDTO.builder().id(1L).name("Downtown Store").addressLine1("100 Market St").build();
        when(serviceClass.getAllStores()).thenReturn(List.of(store1));

        mockMvc.perform(get("/api/stores").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Downtown Store"));
    }
}
