package com.pablog.perfecthome.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pablog.perfecthome.entity.*;
import com.pablog.perfecthome.enums.PropertyType;
import com.pablog.perfecthome.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Year;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ListingRepository listingRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private RealStateAgencyRepository realStateAgencyRepository;

    private Transaction transaction1;
    private Client client;
    private Listing listing;
    private Property property;
    private RealStateAgency agency;

    @BeforeEach
    void setUp() {
        agency = new RealStateAgency("Agency One", "Description 1", "Address 1", "658485231", "agency1@example.com", "website1", "logo1", "G97642583", true);
        agency = realStateAgencyRepository.save(agency);
        property = new Property("Address 1", 100.0, 3, 2, true, false, Year.of(2020), PropertyType.APARTMENT);
        property = propertyRepository.save(property);
        listing = new Listing("Listing 1", "Description 1", BigDecimal.valueOf(150000), LocalDateTime.now(), property, agency);
        listing = listingRepository.save(listing);
        client = new Client("client1", "password", "client1@example.com", "111-222-3333", LocalDateTime.now(), true);
        client = clientRepository.save(client);


        transaction1 = new Transaction(client, listing);
        transactionRepository.save(transaction1);
    }

    @Test
    void testGetAllTransactions() throws Exception {
        mockMvc.perform(get("/transactions"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(1)));
    }

    @Test
    void testGetTransactionById_ExistingTransaction() throws Exception {
        mockMvc.perform(get("/transactions/{id}", transaction1.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(transaction1.getId()));
    }

    @Test
    void testGetTransactionById_NonExistingTransaction() throws Exception {
        mockMvc.perform(get("/transactions/{id}", 999))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateTransaction_Repeated() throws Exception {
        Transaction newTransaction = new Transaction(client, listing);

        mockMvc.perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newTransaction)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateTransaction_InvalidInput() throws Exception {
        Transaction invalidTransaction = new Transaction(); // Missing required fields

        mockMvc.perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidTransaction)))
                .andExpect(status().isBadRequest());
    }


    @Test
    void testDeleteTransaction_ExistingTransaction() throws Exception {
        mockMvc.perform(delete("/transactions/{id}", transaction1.getId()))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void testFilterTransactions_ByClient() throws Exception {
        mockMvc.perform(get("/transactions")
                        .param("username", client.getUsername()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)));
    }
}
