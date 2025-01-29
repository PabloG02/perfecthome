package com.pablog.perfecthome.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class AgentDTO {

    @NotBlank(message = "Username cannot be blank")
    @Size(max = 30, message = "Username cannot exceed 30 characters")
    private String username;

    @NotBlank(message = "Password cannot be blank")
    private String password;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email should be valid")
    private String email;

    @Size(max = 15, message = "Phone number cannot exceed 15 characters")
    private String phone;

    @NotNull(message = "isActive cannot be null")
    private Boolean isActive;

    @NotNull(message = "Employee number cannot be null")
    private Long employeeNumber;

    @NotNull(message = "RealStateAgency ID cannot be null")
    private Long realStateAgencyId;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Long getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(Long employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public Long getRealStateAgencyId() {
        return realStateAgencyId;
    }

    public void setRealStateAgencyId(Long realStateAgencyId) {
        this.realStateAgencyId = realStateAgencyId;
    }

}