package com.pablog.perfecthome.entity;

import com.pablog.perfecthome.enums.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Agent extends User {
    @Column(nullable = false)
    @NotNull(message = "Employee number cannot be null")
    private Long employeeNumber;

    @ManyToOne
    @JoinColumn(nullable = false)
    private RealStateAgency realStateAgency;

    public Agent() {
        super();
        this.setRole(UserRole.AGENT);
    }

    public Agent(String username, String password, String email, String phone, LocalDateTime registrationDate, Boolean isActive, Long employeeNumber, RealStateAgency realStateAgency) {
        super(username, password, email, phone, UserRole.AGENT, registrationDate, isActive);
        this.employeeNumber = employeeNumber;
        this.realStateAgency = realStateAgency;
    }

    public Long getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(Long employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public RealStateAgency getRealStateAgency() {
        return realStateAgency;
    }

    public void setRealStateAgency(RealStateAgency realStateAgency) {
        this.realStateAgency = realStateAgency;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Agent agent = (Agent) o;
        return Objects.equals(employeeNumber, agent.employeeNumber) && Objects.equals(realStateAgency, agent.realStateAgency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(employeeNumber, realStateAgency);
    }

    @Override
    public String toString() {
        return "Agent{" +
                "employeeNumber=" + employeeNumber +
                ", realStateAgency=" + realStateAgency +
                "} " + super.toString();
    }
}
