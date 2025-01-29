package com.pablog.perfecthome.controller;

import com.pablog.perfecthome.dto.PropertyImageDTO;
import com.pablog.perfecthome.entity.PropertyImage;
import com.pablog.perfecthome.service.PropertyImageService;
import com.pablog.perfecthome.service.PropertyService;
import com.pablog.perfecthome.specification.PropertyImageSpecificationBuilder;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/property-images")
@CrossOrigin
public class PropertyImageController {

    private final PropertyImageService propertyImageService;
    private final PropertyService propertyService;

    @Autowired
    public PropertyImageController(PropertyImageService propertyImageService, PropertyService propertyService) {
        this.propertyImageService = propertyImageService;
        this.propertyService = propertyService;
    }

    @GetMapping
    public ResponseEntity<Page<PropertyImage>> getAllPropertyImages(
            @RequestParam(required = false) String url,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) LocalDateTime uploadDate,
            @RequestParam(required = false) Long propertyId,
            @PageableDefault(sort = "id", direction = Sort.Direction.ASC, size = 10) Pageable pageable) {

        PropertyImageSpecificationBuilder builder = new PropertyImageSpecificationBuilder()
                .withUrl(url)
                .withDescription(description)
                .withUploadDate(uploadDate)
                .withPropertyId(propertyId);

        Page<PropertyImage> propertyImages = propertyImageService.findAll(propertyImageService.buildSpecification(builder), pageable);
        return new ResponseEntity<>(propertyImages, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PropertyImage> getPropertyImageById(@PathVariable Long id) {
        Optional<PropertyImage> propertyImage = propertyImageService.findById(id);
        return propertyImage.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<PropertyImage> createPropertyImage(@Valid @RequestBody PropertyImageDTO propertyImageDTO) {
        PropertyImage propertyImage = new PropertyImage();
        propertyImage.setUrl(propertyImageDTO.getUrl());
        propertyImage.setDescription(propertyImageDTO.getDescription());
        propertyImage.setProperty(propertyService.findById(propertyImageDTO.getPropertyId()).orElse(null));

        PropertyImage createdPropertyImage = propertyImageService.save(propertyImage);
        URI location = buildLocation(createdPropertyImage.getId());
        return ResponseEntity.created(location).body(createdPropertyImage);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PropertyImage> updatePropertyImage(@PathVariable Long id, @Valid @RequestBody PropertyImageDTO propertyImageDTO) {
        Optional<PropertyImage> existingPropertyImage = propertyImageService.findById(id);
        if (existingPropertyImage.isPresent()) {
            PropertyImage propertyImage = existingPropertyImage.get();

            // Update fields from DTO
            propertyImage.setUrl(propertyImageDTO.getUrl());
            propertyImage.setDescription(propertyImageDTO.getDescription());
            // Set the property based on the propertyId in the DTO
            propertyImage.setProperty(propertyService.findById(propertyImageDTO.getPropertyId()).orElse(null));

            // Save the updated property image
            PropertyImage updatedPropertyImage = propertyImageService.save(propertyImage);
            return new ResponseEntity<>(updatedPropertyImage, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deletePropertyImage(@PathVariable Long id) {
        if (propertyImageService.findById(id).isPresent()) {
            propertyImageService.deleteById(id);
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
