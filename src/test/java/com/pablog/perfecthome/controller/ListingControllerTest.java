package com.pablog.perfecthome.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pablog.perfecthome.dto.ListingDTO;
import com.pablog.perfecthome.entity.Listing;
import com.pablog.perfecthome.entity.Property;
import com.pablog.perfecthome.entity.RealStateAgency;
import com.pablog.perfecthome.enums.PropertyType;
import com.pablog.perfecthome.repository.ListingRepository;
import com.pablog.perfecthome.repository.PropertyRepository;
import com.pablog.perfecthome.repository.RealStateAgencyRepository;
import com.pablog.perfecthome.repository.TransactionRepository;
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
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.Arrays;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ListingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ListingRepository listingRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private RealStateAgencyRepository realStateAgencyRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    private Listing listing1, listing2;
    private Property property;
    private RealStateAgency agency;

    @BeforeEach
    void setUp() {
        agency = new RealStateAgency("Agency One", "Description", "Address", "884987765", "agency1@example.com", null, null, "Y4013885P", true);
        agency = realStateAgencyRepository.save(agency);
        property = new Property("Address 1", 100.0, 3, 2, true, false, Year.of(2020), PropertyType.APARTMENT);
        property = propertyRepository.save(property);

        listing1 = new Listing("Listing 1", "Description 1", BigDecimal.valueOf(150000), LocalDateTime.now().minusDays(1), property, agency);
        listing2 = new Listing("Listing 2", "Description 2", BigDecimal.valueOf(250000), LocalDateTime.now().minusDays(1), property, agency);
        listingRepository.saveAll(Arrays.asList(listing1, listing2));
    }

    @Test
    void testGetAllListings() throws Exception {
        mockMvc.perform(get("/listings"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].title").value(listing1.getTitle()))
                .andExpect(jsonPath("$.content[1].title").value(listing2.getTitle()));
    }

    @Test
    void testGetListingById_ExistingListing() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/listings/{id}", listing1.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value(listing1.getTitle()));
    }

    @Test
    void testGetListingById_NonExistingListing() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/listings/{id}", 999))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateListing_ValidInput() throws Exception {
        ListingDTO newListing = new ListingDTO("New Listing", "New Description", BigDecimal.valueOf(300000), LocalDateTime.now(), property.getId(), agency.getId());

        mockMvc.perform(MockMvcRequestBuilders.post("/listings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newListing)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/listings/3")))
                .andExpect(jsonPath("$.title").value(newListing.getTitle()));
    }

    @Test
    void testCreateListing_InvalidInput() throws Exception {
        ListingDTO invalidListing = new ListingDTO(); // Missing required fields

        mockMvc.perform(MockMvcRequestBuilders.post("/listings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidListing)))
                .andExpect(status().isBadRequest())
                /*.andExpect(jsonPath("$.title").exists())
                .andExpect(jsonPath("$.description").exists())
                .andExpect(jsonPath("$.price").exists())
                .andExpect(jsonPath("$.publicationDate").exists())
                .andExpect(jsonPath("$.property").exists())
                .andExpect(jsonPath("$.publisher").exists())*/;
    }

    @Test
    void testUpdateListing_ExistingListing_PriceSame() throws Exception {
        ListingDTO listingUpdate = new ListingDTO("New Listing", "New Description", BigDecimal.valueOf(150000), LocalDateTime.now(), property.getId(), agency.getId());
        listingUpdate.setId(1L);

        mockMvc.perform(MockMvcRequestBuilders.put("/listings/{id}", listingUpdate.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(listingUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(listingUpdate.getTitle()))
                .andExpect(jsonPath("$.description").value(listingUpdate.getDescription()))
                .andExpect(jsonPath("$.price").value(listingUpdate.getPrice()));
    }

    @Test
    void testUpdateListing_ExistingListing_PriceChanged() throws Exception {
        ListingDTO listingUpdate = new ListingDTO("Updated Listing", "Updated Description", BigDecimal.valueOf(350000), LocalDateTime.now(), property.getId(), agency.getId());
        listingUpdate.setId(1L);

        mockMvc.perform(MockMvcRequestBuilders.put("/listings/{id}", listingUpdate.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(listingUpdate)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value(listingUpdate.getTitle()))
                .andExpect(jsonPath("$.description").value(listingUpdate.getDescription()))
                .andExpect(jsonPath("$.price").value(listingUpdate.getPrice()));
    }

    @Test
    void testUpdateListing_NonExistingListing() throws Exception {
        ListingDTO listingUpdate = new ListingDTO("Updated Listing", "Updated Description", BigDecimal.valueOf(350000), LocalDateTime.now(), property.getId(), agency.getId());
        listingUpdate.setId(999L);

        mockMvc.perform(MockMvcRequestBuilders.put("/listings/{id}", listingUpdate.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(listingUpdate)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteListing_ExistingListing() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/listings/{id}", listing1.getId()))
                .andExpect(status().isNoContent());

        Listing listing = listingRepository.findById(listing1.getId()).get();
        LocalDateTime expirationDate = listing.getExpirationDate();
        LocalDateTime now = LocalDateTime.now();

        // Threshold to determine if expiration date is close to the current time
        Duration duration = Duration.between(now, expirationDate);

        // Check if expiration date is close to the current time (within 2 minutes)
        assertTrue(Math.abs(duration.toMinutes()) <= 2, "Expiration date is not close to current time.");
    }

    @Test
    void testDeleteListing_NonExistingListing() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/listings/{id}", 999))
                .andExpect(status().isNotFound());
    }

    @Test
    void testFilterListings_ByPriceBetween() throws Exception {
        mockMvc.perform(get("/listings")
                        .param("minPrice", "200000")
                        .param("maxPrice", "300000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].title").value(listing2.getTitle()));
    }

    @Test
    void testFilterListings_ByTitleLike() throws Exception {
        mockMvc.perform(get("/listings")
                        .param("title", "Listing"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2))); // Should find both listing1 and listing2
    }
}
