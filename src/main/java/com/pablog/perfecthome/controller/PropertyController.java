package com.pablog.perfecthome.controller;

import com.pablog.perfecthome.entity.Property;
import com.pablog.perfecthome.enums.PropertyType;
import com.pablog.perfecthome.service.PropertyService;
import com.pablog.perfecthome.specification.PropertySpecificationBuilder;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.Year;
import java.util.Optional;

@RestController
@RequestMapping(path = "/properties", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin
public class PropertyController {

    private final PropertyService propertyService;

    @Autowired
    public PropertyController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    @GetMapping
    public ResponseEntity<Page<Property>> getAllProperties(
            @RequestParam(required = false) String address,
            @RequestParam(required = false) Double size,
            @RequestParam(required = false) Integer rooms,
            @RequestParam(required = false) Integer bathrooms,
            @RequestParam(required = false) Boolean hasGarage,
            @RequestParam(required = false) Boolean hasElevator,
            @RequestParam(required = false) Year constructionYear,
            @RequestParam(required = false) PropertyType propertyType,
            @PageableDefault(sort = "id", direction = Sort.Direction.ASC, size = 10) Pageable pageable) {

        PropertySpecificationBuilder builder = new PropertySpecificationBuilder()
                .withAddress(address)
                .withSize(size)
                .withRooms(rooms)
                .withBathrooms(bathrooms)
                .withGarage(hasGarage)
                .withElevator(hasElevator)
                .withConstructionYear(constructionYear)
                .withPropertyType(propertyType);

        Page<Property> properties = propertyService.findAll(propertyService.buildSpecification(builder), pageable);
        return new ResponseEntity<>(properties, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Property> getPropertyById(@PathVariable Long id) {
        Optional<Property> property = propertyService.findById(id);
        return property.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Property> createProperty(@Valid @RequestBody Property property) {
        Property createdProperty = propertyService.save(property);
        URI location = buildLocation(createdProperty.getId());
        return ResponseEntity.created(location).body(createdProperty);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Property> updateProperty(@PathVariable Long id, @Valid @RequestBody Property propertyDetails) {
        Optional<Property> existingProperty = propertyService.findById(id);
        if (existingProperty.isPresent()) {
            Property property = existingProperty.get();
            // Update fields you want to allow updating
            property.setAddress(propertyDetails.getAddress());
            property.setSize(propertyDetails.getSize());
            property.setRooms(propertyDetails.getRooms());
            property.setBathrooms(propertyDetails.getBathrooms());
            property.setHasGarage(propertyDetails.getHasGarage());
            property.setHasElevator(propertyDetails.getHasElevator());
            property.setConstructionYear(propertyDetails.getConstructionYear());
            property.setPropertyType(propertyDetails.getPropertyType());
            Property updatedProperty = propertyService.save(property);
            return new ResponseEntity<>(updatedProperty, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteProperty(@PathVariable Long id) {
        if (propertyService.findById(id).isPresent()) {
            propertyService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    private URI buildLocation(Long id) {
        return ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
    }
}