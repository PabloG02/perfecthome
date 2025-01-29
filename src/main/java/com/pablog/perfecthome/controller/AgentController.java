package com.pablog.perfecthome.controller;

import com.pablog.perfecthome.dto.AgentDTO;
import com.pablog.perfecthome.entity.Agent;
import com.pablog.perfecthome.entity.RealStateAgency;
import com.pablog.perfecthome.service.AgentService;
import com.pablog.perfecthome.service.RealStateAgencyService;
import com.pablog.perfecthome.specification.AgentSpecificationBuilder;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping(path = "/agents", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin
public class AgentController {

    private final AgentService agentService;
    private final RealStateAgencyService realStateAgencyService;

    @Autowired
    public AgentController(AgentService agentService, RealStateAgencyService realStateAgencyService) {
        this.agentService = agentService;
        this.realStateAgencyService = realStateAgencyService;
    }

    @GetMapping
    public ResponseEntity<Page<Agent>> getAllAgents(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(required = false) Long employeeNumber,
            @PageableDefault(sort = "username", direction = Sort.Direction.ASC, size = 10) Pageable pageable) {

        AgentSpecificationBuilder builder = new AgentSpecificationBuilder()
                .withUsername(username)
                .withEmail(email)
                .withPhone(phone)
                .withIsActive(isActive)
                .withEmployeeNumber(employeeNumber);

        Page<Agent> agents = agentService.findAll(agentService.buildSpecification(builder), pageable);
        return new ResponseEntity<>(agents, HttpStatus.OK);
    }

    @GetMapping("/{username}")
    public ResponseEntity<Agent> getAgentById(@PathVariable String username) {
        Optional<Agent> agent = agentService.findById(username);
        return agent.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Agent> createAgent(@Valid @RequestBody AgentDTO agentDTO) {
        RealStateAgency agency = realStateAgencyService.findById(agentDTO.getRealStateAgencyId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid RealStateAgency ID"));

        Agent agent = new Agent();
        agent.setUsername(agentDTO.getUsername());
        agent.setPassword(agentDTO.getPassword());
        agent.setEmail(agentDTO.getEmail());
        agent.setPhone(agentDTO.getPhone());
        agent.setIsActive(agentDTO.getIsActive());
        agent.setEmployeeNumber(agentDTO.getEmployeeNumber());
        agent.setRealStateAgency(agency);

        Agent createdAgent = agentService.save(agent);
        URI location = buildLocation(createdAgent.getUsername());
        return ResponseEntity.created(location).body(createdAgent);
    }

    @PutMapping("/{username}")
    public ResponseEntity<Agent> updateAgent(@PathVariable String username, @Valid @RequestBody AgentDTO agentDetails) {
        Optional<Agent> existingAgentOptional = agentService.findById(username);
        if (existingAgentOptional.isPresent()) {
            Agent agent = existingAgentOptional.get();
            boolean hasEmployeeNumberChanged = false;

            // Update fields if they are provided (partial updates)
            if (agentDetails.getPassword() != null) {
                // TODO: It should not be done like this in the real world
                agent.setPassword(agentDetails.getPassword());
            }
            if (agentDetails.getEmail() != null) {
                agent.setEmail(agentDetails.getEmail());
            }
            if (agentDetails.getPhone() != null) {
                agent.setPhone(agentDetails.getPhone());
            }
            if (agentDetails.getIsActive() != null) {
                agent.setIsActive(agentDetails.getIsActive());
            }
            /*if (agentDetails.getEmployeeNumber() != null) {
                // TODO: Should I let the user change the employee number?
                hasEmployeeNumberChanged = agentDetails.getEmployeeNumber() != agentDetails.getEmployeeNumber();
                agent.setEmployeeNumber(agentDetails.getEmployeeNumber());
            }*/

            // Save and return updated agent
            Agent updatedAgent = agentService.update(agent, hasEmployeeNumberChanged);
            return new ResponseEntity<>(updatedAgent, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @DeleteMapping("/{username}")
    public ResponseEntity<HttpStatus> deleteAgent(@PathVariable String username) {
        if (agentService.findById(username).isPresent()) {
            agentService.deleteById(username);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    private URI buildLocation(String username) {
        return ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{username}")
                .buildAndExpand(username)
                .toUri();
    }
}
