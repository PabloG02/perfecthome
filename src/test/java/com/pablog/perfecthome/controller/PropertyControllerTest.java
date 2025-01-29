package com.pablog.perfecthome.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pablog.perfecthome.entity.Listing;
import com.pablog.perfecthome.entity.Property;
import com.pablog.perfecthome.entity.RealStateAgency;
import com.pablog.perfecthome.enums.PropertyType;
import com.pablog.perfecthome.repository.ListingRepository;
import com.pablog.perfecthome.repository.PropertyRepository;
import com.pablog.perfecthome.repository.RealStateAgencyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.Arrays;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class PropertyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private ListingRepository listingRepository;

    @Autowired
    private RealStateAgencyRepository realStateAgencyRepository;

    private Property property1, property2;

    @BeforeEach
    void setUp() {
        property1 = new Property("Address 1", 100.0, 3, 2, true, false, Year.of(2020), PropertyType.APARTMENT);
        property2 = new Property("Address 2", 150.0, 4, 3, false, true, Year.of(2015), PropertyType.HOUSE);
        propertyRepository.saveAll(Arrays.asList(property1, property2));

        RealStateAgency agency = new RealStateAgency("Agency One", "Description", "Address", "884987765", "agency1@example.com", null, null, "Y4013885P", true);
        agency = realStateAgencyRepository.save(agency);

        Listing listing = new Listing("Listing 1", "Description 1", BigDecimal.valueOf(150000), LocalDateTime.now().minusDays(1), property2, agency);
        listingRepository.save(listing);
    }

    @Test
    void testGetAllProperties() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/properties"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].address").value(property1.getAddress()))
                .andExpect(jsonPath("$.content[1].address").value(property2.getAddress()));
    }

    @Test
    void testGetPropertyById_ExistingProperty() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/properties/{id}", property1.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.address").value(property1.getAddress()));
    }

    @Test
    void testGetPropertyById_NonExistingProperty() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/properties/{id}", 999))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateProperty_ValidInput() throws Exception {
        Property newProperty = new Property("New Address", 200.0, 5, 4, true, true, Year.of(2023), PropertyType.VILLA);

        mockMvc.perform(post("/properties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newProperty)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/properties/")))
                .andExpect(jsonPath("$.address").value(newProperty.getAddress()));
    }

    @Test
    void testCreateProperty_InvalidInput() throws Exception {
        Property invalidProperty = new Property(); // Missing required fields

        mockMvc.perform(post("/properties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidProperty)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateProperty_ExistingProperty() throws Exception {
        Property propertyUpdate = new Property("Updated Address", 250.0, 6, 5, false, false, Year.of(2024), PropertyType.HOUSE);

        mockMvc.perform(MockMvcRequestBuilders.put("/properties/{id}", property1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(propertyUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address").value(propertyUpdate.getAddress()))
                .andExpect(jsonPath("$.size").value(propertyUpdate.getSize()))
                .andExpect(jsonPath("$.rooms").value(propertyUpdate.getRooms()));
    }

    @Test
    void testUpdateProperty_NonExistingProperty() throws Exception {
        Property propertyUpdate = new Property("Updated Address", 250.0, 6, 5, false, false, Year.of(2024), PropertyType.HOUSE);

        mockMvc.perform(MockMvcRequestBuilders.put("/properties/{id}", 999)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(propertyUpdate)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteProperty_ExistingProperty() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/properties/{id}", property1.getId()))
                .andExpect(status().isNoContent());

        assertFalse(propertyRepository.existsById(property1.getId()));
    }

    @Test
    void testDeleteProperty_ExistingPropertyWithListing() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/properties/{id}", property2.getId()))
                .andExpect(status().isConflict());

        assertTrue(propertyRepository.existsById(property2.getId()));
    }

    @Test
    void testDeleteProperty_NonExistingProperty() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/properties/{id}", 999))
                .andExpect(status().isNotFound());
    }

    @Test
    void testFilterProperties_ByType() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/properties")
                        .param("propertyType", "HOUSE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].address").value(property2.getAddress()));
    }

    @Test
    void testFilterProperties_ByAddressLike() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/properties")
                        .param("address", "Address"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2))); // Should find both property1 and property2
    }
}
