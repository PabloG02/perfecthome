package com.pablog.perfecthome.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Objects;

@Entity
public class RealStateAgency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "Name cannot be blank")
    private String name;

    @Column(nullable = false)
    @NotBlank(message = "Description cannot be blank")
    private String description;

    @Column(nullable = false)
    @NotBlank(message = "Address cannot be blank")
    private String address;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Phone cannot be blank")
    private String phone;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email should be valid")
    private String email;

    private String website;
    private String logoUrl;

    @Column(nullable = false, unique = true, length = 9)
    @NotBlank(message = "CIF cannot be blank")
    @Size(min = 9, max = 9, message = "CIF must be 9 characters long")
    private String cif;

    @Column(nullable = false)
    @NotNull(message = "Verification status must be specified")
    private Boolean isVerified;

    @Column(nullable = false)
    private Boolean isDisabled;

    public RealStateAgency() {
    }

    public RealStateAgency(String name, String description, String address, String phone, String email, String website, String logoUrl, String cif, Boolean isVerified) {
        this.name = name;
        this.description = description;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.website = website;
        this.logoUrl = logoUrl;
        this.cif = cif;
        this.isVerified = isVerified;
        this.isDisabled = false;
    }

    public RealStateAgency(Long id, String name, String description, String address, String phone, String email, String website, String logoUrl, String cif, Boolean isVerified, Boolean isDisabled) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.website = website;
        this.logoUrl = logoUrl;
        this.cif = cif;
        this.isVerified = isVerified;
        this.isDisabled = isDisabled;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getCif() {
        return cif;
    }

    public void setCif(String cif) {
        this.cif = cif;
    }

    public Boolean getVerified() {
        return isVerified;
    }

    public void setVerified(Boolean verified) {
        isVerified = verified;
    }

    public Boolean getDisabled() {
        return isDisabled;
    }

    public void setDisabled(Boolean disabled) {
        isDisabled = disabled;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        RealStateAgency that = (RealStateAgency) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(address, that.address) && Objects.equals(phone, that.phone) && Objects.equals(email, that.email) && Objects.equals(website, that.website) && Objects.equals(logoUrl, that.logoUrl) && Objects.equals(cif, that.cif) && Objects.equals(isVerified, that.isVerified) && Objects.equals(isDisabled, that.isDisabled);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, address, phone, email, website, logoUrl, cif, isVerified, isDisabled);
    }

    @Override
    public String toString() {
        return "RealStateAgency{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", website='" + website + '\'' +
                ", logoUrl='" + logoUrl + '\'' +
                ", cif='" + cif + '\'' +
                ", isVerified=" + isVerified +
                ", isDisabled=" + isDisabled +
                '}';
    }
}
