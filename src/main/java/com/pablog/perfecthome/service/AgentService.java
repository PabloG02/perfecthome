package com.pablog.perfecthome.service;

import com.pablog.perfecthome.entity.Agent;
import com.pablog.perfecthome.repository.AgentRepository;
import com.pablog.perfecthome.specification.AgentSpecificationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AgentService {

    private final AgentRepository agentRepository;

    @Autowired
    public AgentService(AgentRepository agentRepository) {
        this.agentRepository = agentRepository;
    }

    public List<Agent> findAll() {
        return agentRepository.findAll();
    }

    public Page<Agent> findAll(Pageable pageable) {
        return agentRepository.findAll(pageable);
    }

    public Optional<Agent> findById(String username) {
        return agentRepository.findById(username);
    }

    public Agent save(Agent agent) {
        if (agentRepository.existsByEmployeeNumberAndRealStateAgencyId(agent.getEmployeeNumber(), agent.getRealStateAgency().getId())) {
            throw new IllegalStateException("There is already an agent with the same employee number in the same real estate agency");
        }

        return agentRepository.save(agent);
    }

    public Agent update(Agent agent, boolean hasEmployeeNumberChanged) {
        if (hasEmployeeNumberChanged && agentRepository.existsByEmployeeNumberAndRealStateAgencyId(agent.getEmployeeNumber(), agent.getRealStateAgency().getId())) {
            throw new IllegalStateException("There is already an agent with the same employee number in the same real estate agency");
        }

        return agentRepository.save(agent);
    }

    // Soft delete -> The user has to be kept in the database, to keep the history of their activity (right now, there is no activity history)
    public void deleteById(String username) {
        agentRepository.findById(username).ifPresent(agent -> {
            agent.setIsActive(false);
            agentRepository.save(agent);
        });
    }

    public Page<Agent> findAll(Specification<Agent> spec, Pageable pageable) {
        return agentRepository.findAll(spec, pageable);
    }

    public Specification<Agent> buildSpecification(AgentSpecificationBuilder builder) {
        return builder.build();
    }
}
