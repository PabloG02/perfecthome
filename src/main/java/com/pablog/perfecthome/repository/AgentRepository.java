package com.pablog.perfecthome.repository;

import com.pablog.perfecthome.entity.Agent;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AgentRepository extends JpaRepository<Agent, String>, JpaSpecificationExecutor<Agent> {
    boolean existsByEmployeeNumberAndRealStateAgencyId(@NotNull(message = "Employee number cannot be null") Long employeeNumber, @NotNull(message = "Real estate agency id cannot be null") Long realStateAgencyId);
}
