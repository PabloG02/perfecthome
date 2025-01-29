package com.pablog.perfecthome.entity;

import com.pablog.perfecthome.enums.PropertyType;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Year;
import java.util.Objects;

@Entity
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "Address cannot be blank")
    private String address;

    @Column(nullable = false)
    @NotNull(message = "Size cannot be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Size must be greater than 0")
    private Double size;

    @Column(nullable = false)
    @NotNull(message = "Rooms cannot be null")
    @Min(value = 0, message = "Rooms must be at least 0")
    private Integer rooms;

    @Column(nullable = false)
    @NotNull(message = "Bathrooms cannot be null")
    @Min(value = 0, message = "Bathrooms must be at least 0")
    private Integer bathrooms;

    @Column(nullable = false)
    @NotNull(message = "Garage information must be specified")
    private Boolean hasGarage;

    @Column(nullable = false)
    @NotNull(message = "Elevator information must be specified")
    private Boolean hasElevator;

    @Column(nullable = false)
    @NotNull(message = "Construction year cannot be null")
    private Year constructionYear;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull(message = "Property type cannot be null")
    private PropertyType propertyType;

    public Property() {
    }

    public Property(String address, Double size, Integer rooms, Integer bathrooms, Boolean hasGarage, Boolean hasElevator, Year constructionYear, PropertyType propertyType) {
        this.address = address;
        this.size = size;
        this.rooms = rooms;
        this.bathrooms = bathrooms;
        this.hasGarage = hasGarage;
        this.hasElevator = hasElevator;
        this.constructionYear = constructionYear;
        this.propertyType = propertyType;
    }

    public Property(Long id, String address, Double size, Integer rooms, Integer bathrooms, Boolean hasGarage, Boolean hasElevator, Year constructionYear, PropertyType propertyType) {
        this.id = id;
        this.address = address;
        this.size = size;
        this.rooms = rooms;
        this.bathrooms = bathrooms;
        this.hasGarage = hasGarage;
        this.hasElevator = hasElevator;
        this.constructionYear = constructionYear;
        this.propertyType = propertyType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getSize() {
        return size;
    }

    public void setSize(Double size) {
        this.size = size;
    }

    public Integer getRooms() {
        return rooms;
    }

    public void setRooms(Integer rooms) {
        this.rooms = rooms;
    }

    public Integer getBathrooms() {
        return bathrooms;
    }

    public void setBathrooms(Integer bathrooms) {
        this.bathrooms = bathrooms;
    }

    public Boolean getHasGarage() {
        return hasGarage;
    }

    public void setHasGarage(Boolean hasGarage) {
        this.hasGarage = hasGarage;
    }

    public Boolean getHasElevator() {
        return hasElevator;
    }

    public void setHasElevator(Boolean hasElevator) {
        this.hasElevator = hasElevator;
    }

    public Year getConstructionYear() {
        return constructionYear;
    }

    public void setConstructionYear(Year constructionYear) {
        this.constructionYear = constructionYear;
    }

    public PropertyType getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(PropertyType propertyType) {
        this.propertyType = propertyType;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Property property = (Property) o;
        return Objects.equals(id, property.id) && Objects.equals(address, property.address) && Objects.equals(size, property.size) && Objects.equals(rooms, property.rooms) && Objects.equals(bathrooms, property.bathrooms) && Objects.equals(hasGarage, property.hasGarage) && Objects.equals(hasElevator, property.hasElevator) && Objects.equals(constructionYear, property.constructionYear) && propertyType == property.propertyType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, address, size, rooms, bathrooms, hasGarage, hasElevator, constructionYear, propertyType);
    }

    @Override
    public String toString() {
        return "Property{" +
                "id=" + id +
                ", address='" + address + '\'' +
                ", size=" + size +
                ", rooms=" + rooms +
                ", bathrooms=" + bathrooms +
                ", hasGarage=" + hasGarage +
                ", hasElevator=" + hasElevator +
                ", constructionYear=" + constructionYear +
                ", propertyType=" + propertyType +
                '}';
    }
}
