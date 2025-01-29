package com.pablog.perfecthome.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Listing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "Title cannot be blank")
    private String title;

    @Column(nullable = false, length = 1000)
    @NotBlank(message = "Description cannot be blank")
    @Length(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    @Column(nullable = false)
    @NotNull(message = "Price cannot be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private BigDecimal price;

    @Column(nullable = false)
    @NotNull(message = "Publication date cannot be null")
    @PastOrPresent(message = "Publication date must be in the past or present")
    private LocalDateTime publicationDate;

    private LocalDateTime expirationDate;

    @ManyToOne
    @JoinColumn(nullable = false)
    @NotNull(message = "Property cannot be null")
    private Property property;

    @ManyToOne
    @JoinColumn(nullable = false)
    @NotNull(message = "Publisher cannot be null")
    private RealStateAgency publisher;

    public Listing() {
    }

    public Listing(String title, String description, BigDecimal price, LocalDateTime publicationDate, Property property, RealStateAgency publisher) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.publicationDate = publicationDate;
        this.property = property;
        this.publisher = publisher;
    }

    public Listing(Long id, String title, String description, BigDecimal price, LocalDateTime publicationDate, LocalDateTime expirationDate, Property property, RealStateAgency publisher) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.publicationDate = publicationDate;
        this.expirationDate = expirationDate;
        this.property = property;
        this.publisher = publisher;
    }

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

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    public RealStateAgency getPublisher() {
        return publisher;
    }

    public void setPublisher(RealStateAgency publisher) {
        this.publisher = publisher;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Listing listing = (Listing) o;
        return Objects.equals(id, listing.id) && Objects.equals(title, listing.title) && Objects.equals(description, listing.description) && Objects.equals(price, listing.price) && Objects.equals(publicationDate, listing.publicationDate) && Objects.equals(expirationDate, listing.expirationDate) && Objects.equals(property, listing.property) && Objects.equals(publisher, listing.publisher);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, price, publicationDate, expirationDate, property, publisher);
    }

    @Override
    public String toString() {
        return "Listing{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", publicationDate=" + publicationDate +
                ", expirationDate=" + expirationDate +
                ", property=" + property +
                ", publisher=" + publisher +
                '}';
    }
}
