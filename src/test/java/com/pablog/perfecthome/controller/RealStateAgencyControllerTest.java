package com.pablog.perfecthome.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pablog.perfecthome.entity.RealStateAgency;
import com.pablog.perfecthome.repository.RealStateAgencyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class RealStateAgencyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RealStateAgencyRepository realStateAgencyRepository;

    private RealStateAgency agency1, agency2;

    @BeforeEach
    void setUp() {
        realStateAgencyRepository.deleteAll();
        agency1 = new RealStateAgency("Agency One", "Description 1", "Address 1", "658485231", "agency1@example.com", "website1", "logo1", "G97642583", true);
        agency2 = new RealStateAgency("Agency Two", "Description 2", "Address 2", "658485232", "agency2@example.com", "website2", "logo2", "L64273198", false);
        realStateAgencyRepository.saveAll(Arrays.asList(agency1, agency2));
    }

    @Test
    void testGetAllRealStateAgencies() throws Exception {
        mockMvc.perform(get("/real-state-agencies"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].name").value(agency1.getName()))
                .andExpect(jsonPath("$.content[1].name").value(agency2.getName()));
    }

    @Test
    void testGetRealStateAgencyById_ExistingRealStateAgency() throws Exception {
        mockMvc.perform(get("/real-state-agencies/{id}", agency1.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value(agency1.getName()));
    }

    @Test
    void testGetRealStateAgencyById_NonExistingRealStateAgency() throws Exception {
        mockMvc.perform(get("/real-state-agencies/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateRealStateAgency_ValidInput() throws Exception {
        RealStateAgency newAgency = new RealStateAgency("Agency Three", "Description 3", "Address 3", "658435231", "agency3@example.com", "website3", "logo3", "P97643583", true);

        mockMvc.perform(post("/real-state-agencies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newAgency)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/real-state-agencies/")))
                .andExpect(jsonPath("$.name").value(newAgency.getName()));
    }

    @Test
    void testCreateRealStateAgency_InvalidInput() throws Exception {
        RealStateAgency invalidAgency = new RealStateAgency(); // Missing required fields

        mockMvc.perform(post("/real-state-agencies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidAgency)))
                .andExpect(status().isBadRequest())
                /*.andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.description").exists())
                .andExpect(jsonPath("$.address").exists())
                .andExpect(jsonPath("$.phone").exists())
                .andExpect(jsonPath("$.email").exists())
                .andExpect(jsonPath("$.cif").exists())
                .andExpect(jsonPath("$.isVerified").exists())*/;
    }

    @Test
    void testUpdateRealStateAgency_ExistingRealStateAgency() throws Exception {
        RealStateAgency agencyUpdate = new RealStateAgency("Updated Agency", "Updated Description", "Updated Address", "658485231", "agency1@example.com", "website1", "logo1", "G97642583", true);
        agencyUpdate.setId(agency1.getId()); // Ensure ID is set for update

        mockMvc.perform(put("/real-state-agencies/{id}", agency1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(agencyUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(agencyUpdate.getName()))
                .andExpect(jsonPath("$.description").value(agencyUpdate.getDescription()));
    }

    @Test
    void testUpdateRealStateAgency_NonExistingRealStateAgency() throws Exception {
        RealStateAgency agencyUpdate = new RealStateAgency("Updated Agency", "Updated Description", "Updated Address", "658485231", "agency1@example.com", "website1", "logo1", "G97642583", false);
        agencyUpdate.setId(999L);

        mockMvc.perform(put("/real-state-agencies/{id}", 999)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(agencyUpdate)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteRealStateAgency_ExistingRealStateAgency() throws Exception {
        mockMvc.perform(delete("/real-state-agencies/{id}", agency1.getId()))
                .andExpect(status().isNoContent());

        assertTrue(realStateAgencyRepository.findById(agency1.getId()).get().getDisabled()); // Verify deletion from DB
    }

    @Test
    void testDeleteRealStateAgency_NonExistingRealStateAgency() throws Exception {
        mockMvc.perform(delete("/real-state-agencies/{id}", 999))
                .andExpect(status().isNotFound());
    }

    @Test
    void testFilterRealStateAgencies_ByIsVerified() throws Exception {
        mockMvc.perform(get("/real-state-agencies")
                        .param("isVerified", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].name").value(agency1.getName()));
    }

    @Test
    void testFilterRealStateAgencies_ByNameLike() throws Exception {
        mockMvc.perform(get("/real-state-agencies")
                        .param("name", "Agency"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2))); // Should find both agency1 and agency2
    }
}
