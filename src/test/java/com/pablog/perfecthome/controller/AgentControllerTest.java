package com.pablog.perfecthome.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pablog.perfecthome.dto.AgentDTO;
import com.pablog.perfecthome.entity.Agent;
import com.pablog.perfecthome.entity.RealStateAgency;
import com.pablog.perfecthome.repository.AgentRepository;
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

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class AgentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private RealStateAgencyRepository realStateAgencyRepository;

    private RealStateAgency testAgency;
    private Agent testAgent;

    @BeforeEach
    void setup() {
        // Setup RealStateAgency
        testAgency = new RealStateAgency("Test Agency", "Test Description", "Test Address", "123456789", "test@agency.com", "www.testagency.com", null, "A45678912", true);
        testAgency = realStateAgencyRepository.save(testAgency);

        // Setup Agent
        testAgent = new Agent();
        testAgent.setUsername("testagent");
        testAgent.setPassword("password");
        testAgent.setEmail("agent@test.com");
        testAgent.setIsActive(true);
        testAgent.setEmployeeNumber(11L);
        testAgent.setRealStateAgency(testAgency);
        testAgent = agentRepository.save(testAgent);
    }


    @Test
    void testGetAllAgents() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/agents"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    void testGetAgentByUsername_ExistingAgent() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/agents/{username}", testAgent.getUsername()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").value(testAgent.getUsername()));
    }

    @Test
    void testGetAgentByUsername_NonExistingAgent() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/agents/{username}", "nonexistent"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateAgent_ValidInput() throws Exception {
        AgentDTO agentDTO = new AgentDTO();
        agentDTO.setUsername("newagent");
        agentDTO.setPassword("password");
        agentDTO.setEmail("newagent@test.com");
        agentDTO.setIsActive(true);
        agentDTO.setEmployeeNumber(54321L);
        agentDTO.setRealStateAgencyId(testAgency.getId());

        mockMvc.perform(MockMvcRequestBuilders.post("/agents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(agentDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string("Location", containsString("/agents/newagent")))
                .andExpect(jsonPath("$.username").value("newagent"));
    }

    @Test
    void testCreateAgent_InvalidInput() throws Exception {
        AgentDTO agentDTO = new AgentDTO(); // Missing required fields

        mockMvc.perform(MockMvcRequestBuilders.post("/agents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(agentDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.username").exists())
                .andExpect(jsonPath("$.password").exists())
                .andExpect(jsonPath("$.email").exists())
                .andExpect(jsonPath("$.employeeNumber").exists())
                .andExpect(jsonPath("$.realStateAgencyId").exists());
    }

    @Test
    void testUpdateAgent_ExistingAgent() throws Exception {
        AgentDTO agentDTO = new AgentDTO();
        agentDTO.setUsername(testAgent.getUsername());
        agentDTO.setPassword("updated");
        agentDTO.setEmail("updated@test.com");
        agentDTO.setPhone("127358468");
        agentDTO.setIsActive(true);
        agentDTO.setEmployeeNumber(testAgent.getEmployeeNumber());
        agentDTO.setRealStateAgencyId(testAgency.getId());

        mockMvc.perform(MockMvcRequestBuilders.put("/agents/{username}", testAgent.getUsername())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(agentDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.email").value("updated@test.com"));
    }

    @Test
    void testUpdateAgent_NonExistingAgent() throws Exception {
        AgentDTO agentDTO = new AgentDTO();
        agentDTO.setUsername("nonexistent");
        agentDTO.setPassword("updated");
        agentDTO.setEmail("updated@test.com");
        agentDTO.setPhone("127358468");
        agentDTO.setIsActive(true);
        agentDTO.setEmployeeNumber(testAgent.getEmployeeNumber());
        agentDTO.setRealStateAgencyId(testAgency.getId());

        mockMvc.perform(MockMvcRequestBuilders.put("/agents/{username}", "nonexistent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(agentDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteAgent_ExistingAgent() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/agents/{username}", testAgent.getUsername()))
                .andExpect(status().isNoContent());
        assertFalse(agentRepository.findById(testAgent.getUsername()).get().getIsActive());
    }

    @Test
    void testDeleteAgent_NonExistingAgent() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/agents/{username}", "nonexistent"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testFilterAgents_ByIsActive() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/agents")
                        .param("isActive", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].username").value(testAgent.getUsername()));
    }

    @Test
    void testFilterAgents_ByUsernameLike() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/agents")
                        .param("username", "test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }
}