package com.pablog.perfecthome.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ListingDTO {

    private Long id;

    @NotBlank(message = "Title cannot be blank")
    private String title;

    @NotBlank(message = "Description cannot be blank")
    @Length(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    @NotNull(message = "Price cannot be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private BigDecimal price;

    @NotNull(message = "Publication date cannot be null")
    @PastOrPresent(message = "Publication date must be in the past or present")
    private LocalDateTime publicationDate;

    private LocalDateTime expirationDate;

    @NotNull(message = "Property ID cannot be null")
    private Long propertyId;

    @NotNull(message = "Publisher ID cannot be null")
    private Long publisherId;

    public ListingDTO() {
    }

    public ListingDTO(String title, String description, BigDecimal price, LocalDateTime publicationDate, Long propertyId, Long publisherId) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.publicationDate = publicationDate;
        this.propertyId = propertyId;
        this.publisherId = publisherId;
    }

    public ListingDTO(Long id, String title, String description, BigDecimal price, LocalDateTime publicationDate, LocalDateTime expirationDate, Long propertyId, Long publisherId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.publicationDate = publicationDate;
        this.expirationDate = expirationDate;
        this.propertyId = propertyId;
        this.publisherId = publisherId;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public LocalDateTime getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(LocalDateTime publicationDate) {
        this.publicationDate = publicationDate;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Long getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(Long propertyId) {
        this.propertyId = propertyId;
    }

    public Long getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(Long publisherId) {
        this.publisherId = publisherId;
    }
}
