package com.pablog.perfecthome.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class TransactionDTO {

    @NotBlank(message = "Username cannot be blank")
    @Size(max = 30, message = "Username cannot exceed 30 characters")
    private String username;

    @NotNull(message = "Listing ID cannot be null")
    private Long listingId;

    public TransactionDTO() {}

    public TransactionDTO(String username, Long listingId) {
        this.username = username;
        this.listingId = listingId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getListingId() {
        return listingId;
    }

    public void setListingId(Long listingId) {
        this.listingId = listingId;
    }
}
