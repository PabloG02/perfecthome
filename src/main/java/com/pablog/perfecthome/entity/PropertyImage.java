package com.pablog.perfecthome.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class PropertyImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "URL cannot be blank")
    private String url;

    @Column(nullable = false)
    @NotBlank(message = "Description cannot be blank")
    private String description;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime uploadDate;

    @ManyToOne
    @JoinColumn(nullable = false, updatable = false)
    @NotNull(message = "Property cannot be null")
    private Property property;

    public PropertyImage() {
    }

    public PropertyImage(String url, String description, LocalDateTime uploadDate, Property property) {
        this.url = url;
        this.description = description;
        this.uploadDate = uploadDate;
        this.property = property;
    }

    public PropertyImage(Long id, String url, String description, LocalDateTime uploadDate, Property property) {
        this.id = id;
        this.url = url;
        this.description = description;
        this.uploadDate = uploadDate;
        this.property = property;
    }

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

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PropertyImage that = (PropertyImage) o;
        return Objects.equals(id, that.id) && Objects.equals(url, that.url) && Objects.equals(description, that.description) && Objects.equals(uploadDate, that.uploadDate) && Objects.equals(property, that.property);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, url, description, uploadDate, property);
    }

    @Override
    public String toString() {
        return "PropertyImage{" +
                "id=" + id +
                ", url='" + url + '\'' +
                ", description='" + description + '\'' +
                ", uploadDate=" + uploadDate +
                ", property=" + property +
                '}';
    }
}
