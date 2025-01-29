package com.pablog.perfecthome.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pablog.perfecthome.entity.Client;
import com.pablog.perfecthome.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ClientRepository clientRepository;

    private Client client1, client2;

    @BeforeEach
    void setUp() {
        client1 = new Client("client1", "password", "client1@example.com", "654489543", LocalDateTime.now(), true);
        client2 = new Client("client2", "password", "client2@example.com", "685323214", LocalDateTime.now(), false);
        clientRepository.saveAll(Arrays.asList(client1, client2));
    }

    @Test
    void testGetAllClients() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/clients"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    void testGetClientById_ExistingClient() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/clients/{username}", client1.getUsername()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").value(client1.getUsername()));
    }

    @Test
    void testGetClientById_NonExistingClient() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/clients/{username}", "nonexistent"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateClient_ValidInput() throws Exception {
        Client newClient = new Client("newClient", "password", "new@example.com", "777-888-9999", LocalDateTime.now(), true);

        mockMvc.perform(post("/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newClient)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/clients/newClient")))
                .andExpect(jsonPath("$.username").value("newClient"));
    }

    @Test
    void testCreateClient_InvalidInput() throws Exception {
        Client invalidClient = new Client(); // Missing required fields

        mockMvc.perform(post("/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidClient)))
                .andExpect(status().isBadRequest())
                /*.andExpect(jsonPath("$.username").exists())
                .andExpect(jsonPath("$.password").exists())
                .andExpect(jsonPath("$.email").exists())*/;
    }

    @Test
    void testUpdateClient_ExistingClient() throws Exception {
        Client clientUpdate = new Client(client1.getUsername(), "newPassword", "updated@example.com", "000-111-2222", LocalDateTime.now(), false);

        mockMvc.perform(MockMvcRequestBuilders.put("/clients/{username}", client1.getUsername())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientUpdate)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.email").value("updated@example.com"))
                .andExpect(jsonPath("$.isActive").value(false));
    }

    @Test
    void testUpdateClient_NonExistingClient() throws Exception {
        Client clientUpdate = new Client("nonexistent", "password", "update@example.com", "000-000-0000", LocalDateTime.now(), true);

        mockMvc.perform(MockMvcRequestBuilders.put("/clients/{username}", "nonexistent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientUpdate)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteClient_ExistingClient() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/clients/{username}", client1.getUsername()))
                .andExpect(status().isNoContent());
        assertFalse(clientRepository.findById(client1.getUsername()).get().getIsActive());
    }

    @Test
    void testDeleteClient_NonExistingClient() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/clients/{username}", "nonexistent"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testFilterClients_ByIsActive() throws Exception {
        mockMvc.perform(get("/clients")
                        .param("isActive", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].username").value("client1"));
    }

    @Test
    void testFilterClients_ByUsernameLike() throws Exception {
        mockMvc.perform(get("/clients")
                        .param("username", "client"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2))); // Should find both client1 and client2
    }
}
