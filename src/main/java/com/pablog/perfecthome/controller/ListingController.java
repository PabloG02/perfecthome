package com.pablog.perfecthome.controller;

import com.pablog.perfecthome.dto.ListingDTO;
import com.pablog.perfecthome.entity.Listing;
import com.pablog.perfecthome.entity.Property;
import com.pablog.perfecthome.entity.RealStateAgency;
import com.pablog.perfecthome.service.ListingService;
import com.pablog.perfecthome.service.PropertyService;
import com.pablog.perfecthome.service.RealStateAgencyService;
import com.pablog.perfecthome.specification.ListingSpecificationBuilder;
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

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping(path = "/listings", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin
public class ListingController {

    private final ListingService listingService;
    private final PropertyService propertyService;
    private final RealStateAgencyService realStateAgencyService;

    @Autowired
    public ListingController(ListingService listingService, PropertyService propertyService, RealStateAgencyService realStateAgencyService) {
        this.listingService = listingService;
        this.propertyService = propertyService;
        this.realStateAgencyService = realStateAgencyService;
    }

    @GetMapping
    public ResponseEntity<Page<Listing>> getAllListings(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) BigDecimal price,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) LocalDateTime publicationDate,
            @RequestParam(required = false) LocalDateTime expirationDate,
            @PageableDefault(sort = "id", direction = Sort.Direction.ASC, size = 10) Pageable pageable) {

        ListingSpecificationBuilder builder = new ListingSpecificationBuilder()
                .withTitle(title)
                .withDescription(description)
                .withPrice(price)
                .withPriceBetween(minPrice, maxPrice)
                .withPublicationDate(publicationDate)
                .withExpirationDate(expirationDate);

        Page<Listing> listings = listingService.findAll(listingService.buildSpecification(builder), pageable);
        return new ResponseEntity<>(listings, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Listing> getListingById(@PathVariable Long id) {
        Optional<Listing> listing = listingService.findById(id);
        return listing.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Listing> createListing(@Valid @RequestBody ListingDTO listingDTO) {
        Property property = propertyService.findById(listingDTO.getPropertyId()).orElse(null);
        RealStateAgency publisher = realStateAgencyService.findById(listingDTO.getPublisherId()).orElse(null);

        Listing listing = new Listing(listingDTO.getTitle(), listingDTO.getDescription(), listingDTO.getPrice(),
                listingDTO.getPublicationDate(), property,publisher);

        if (listingDTO.getExpirationDate() != null) {
            listing.setExpirationDate(listingDTO.getExpirationDate());
        }

        Listing createdListing = listingService.save(listing);
        URI location = buildLocation(createdListing.getId());
        return ResponseEntity.created(location).body(createdListing);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Listing> updateListing(@PathVariable Long id, @Valid @RequestBody ListingDTO listingDTO) {
        Property property = propertyService.findById(listingDTO.getPropertyId()).orElse(null);
        RealStateAgency publisher = realStateAgencyService.findById(listingDTO.getPublisherId()).orElse(null);
        Listing listingDetails = new Listing(listingDTO.getId(), listingDTO.getTitle(), listingDTO.getDescription(),
                listingDTO.getPrice(), listingDTO.getPublicationDate(), listingDTO.getExpirationDate(), property, publisher);

        Optional<Listing> updatedListingOptional = listingService.update(id, listingDetails);
        if (updatedListingOptional.isPresent()) {
            Listing updatedListing = updatedListingOptional.get();
            if (!updatedListing.getId().equals(id)) { // Check if a new listing was created
                URI location = buildLocation(updatedListing.getId());
                return ResponseEntity.created(location).body(updatedListing); // Return 201 Created for new listing
            } else {
                return new ResponseEntity<>(updatedListing, HttpStatus.OK); // Return 200 OK for updated listing
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteListing(@PathVariable Long id) {
        if (listingService.findById(id).isPresent()) {
            listingService.deleteById(id);
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
