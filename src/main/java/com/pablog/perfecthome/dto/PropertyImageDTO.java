package com.pablog.perfecthome.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class PropertyImageDTO {

    private Long id;

    @NotBlank(message = "URL cannot be blank")
    private String url;

    @NotBlank(message = "Description cannot be blank")
    private String description;

    private LocalDateTime uploadDate;

    @NotNull(message = "Property ID cannot be null")
    private Long propertyId;

    public PropertyImageDTO() {
    }

    public PropertyImageDTO(Long id, String url, String description, LocalDateTime uploadDate, Long propertyId) {
        this.id = id;
        this.url = url;
        this.description = description;
        this.uploadDate = uploadDate;
        this.propertyId = propertyId;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(LocalDateTime uploadDate) {
        this.uploadDate = uploadDate;
    }

    public Long getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(Long propertyId) {
        this.propertyId = propertyId;
    }
}
