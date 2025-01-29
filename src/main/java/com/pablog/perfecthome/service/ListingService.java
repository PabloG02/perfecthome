package com.pablog.perfecthome.service;

import com.pablog.perfecthome.entity.Listing;
import com.pablog.perfecthome.repository.ListingRepository;
import com.pablog.perfecthome.specification.ListingSpecificationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ListingService {

    private final ListingRepository listingRepository;

    @Autowired
    public ListingService(ListingRepository listingRepository) {
        this.listingRepository = listingRepository;
    }

    public List<Listing> findAll() {
        return listingRepository.findAll();
    }

    public Page<Listing> findAll(Pageable pageable) {
        return listingRepository.findAll(pageable);
    }

    public Optional<Listing> findById(Long id) {
        return listingRepository.findById(id);
    }

    public Listing save(Listing listing) {
        // 1. Find existing active listings for the same property
        List<Listing> existingActiveListings = listingRepository.findByProperty(listing.getProperty());

        // 2. Check for date overlaps before creating a new listing
        for (Listing existingListing : existingActiveListings) {
            LocalDateTime existingPublicationDate = existingListing.getPublicationDate();
            LocalDateTime existingExpirationDate = existingListing.getExpirationDate();
            LocalDateTime newPublicationDate = listing.getPublicationDate();
            LocalDateTime newExpirationDate = listing.getExpirationDate();

            boolean overlap = false;

            // Case 1: New listing starts within existing listing's range
            // Example: Existing listing: 2021-01-01 to 2021-01-31, New listing: 2021-01-15 to 2021-02-15
            // Example: Existing listing: 2021-01-01 to null, New listing: 2021-01-15 to 2021-02-15
            if (newPublicationDate.isAfter(existingPublicationDate.minusSeconds(1)) && (existingExpirationDate != null && newPublicationDate.isBefore(existingExpirationDate.plusSeconds(1)))) {
                overlap = true;
            }
            // Case 2: New listing ends within existing listing's range
            // Example: Existing listing: 2021-01-01 to 2021-01-31, New listing: 2020-12-15 to 2021-01-15
            // Example: Existing listing: 2021-01-01 to null, New listing: 2020-12-15 to 2021-01-15
            else if (newExpirationDate != null && newExpirationDate.isAfter(existingPublicationDate.minusSeconds(1)) && (existingExpirationDate != null && newExpirationDate.isBefore(existingExpirationDate.plusSeconds(1)))) {
                overlap = true;
            }
            // Case 3: New listing encompasses existing listing's publication date
            // Example: Existing listing: 2021-01-15 to 2021-01-31, New listing: 2021-01-01 to 2021-02-15
            else if (newPublicationDate.isBefore(existingPublicationDate.plusSeconds(1)) && (newExpirationDate == null || (existingExpirationDate != null && newExpirationDate.isAfter(existingExpirationDate.minusSeconds(1))))) {
                overlap = true;
            }


            if (overlap) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Cannot create listing. Publication or Expiration date overlaps with existing active listing for the same property.");
            }
        }

        // 3. Soft delete existing active listing by setting their expiration date to now
        existingActiveListings.stream().filter(existingListing -> existingListing.getExpirationDate() == null).forEach(existingListing -> {
            if (existingListing.getPublicationDate().isBefore(listing.getPublicationDate().plusSeconds(1))) {
                existingListing.setExpirationDate(LocalDateTime.now());
                listingRepository.save(existingListing);
            }
        });

        // 4. Save the new listing
        return listingRepository.save(listing);
    }

    public Optional<Listing> update(Long id, Listing listingDetails) {
        Optional<Listing> existingListingOptional = listingRepository.findById(id);
        if (existingListingOptional.isPresent()) {
            Listing existingListing = existingListingOptional.get();

            // If price is changed, create a new listing with the new price and soft delete the existing listing to keep a history of price changes
            if (listingDetails.getPrice() != null && existingListing.getPrice().compareTo(listingDetails.getPrice()) != 0) {
                Listing newListing = new Listing();
                newListing.setTitle(listingDetails.getTitle());
                newListing.setDescription(listingDetails.getDescription());
                newListing.setPrice(listingDetails.getPrice());
                newListing.setPublicationDate(LocalDateTime.now());
                newListing.setProperty(listingDetails.getProperty());
                newListing.setPublisher(listingDetails.getPublisher());

                // Soft delete the existing listing (set expiration date)
                existingListing.setExpirationDate(LocalDateTime.now());
                listingRepository.save(existingListing); // Save the expired existing listing

                // Save the new listing
                Listing createdListing = listingRepository.save(newListing);
                return Optional.of(createdListing); // Return the newly created listing
            } else {
                // Price is not changed, or new price is null, proceed with updating other fields if needed.
                existingListing.setTitle(listingDetails.getTitle());
                existingListing.setDescription(listingDetails.getDescription());
                existingListing.setExpirationDate(listingDetails.getExpirationDate());

                Listing updatedListing = listingRepository.save(existingListing);
                return Optional.of(updatedListing); // Return the updated existing listing
            }
        } else {
            return Optional.empty(); // Listing not found
        }
    }

    public void deleteById(Long id) {
        Optional<Listing> listingOptional = listingRepository.findById(id);
        if (listingOptional.isPresent()) {
            Listing listing = listingOptional.get();
            if (listing.getExpirationDate() != null && listing.getExpirationDate().isBefore(LocalDateTime.now())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot delete expired listing.");
            }
            listing.setExpirationDate(LocalDateTime.now()); // Soft delete: set expiration date
            listingRepository.save(listing);
        }
    }

    public Page<Listing> findAll(Specification<Listing> spec, Pageable pageable) {
        return listingRepository.findAll(spec, pageable);
    }

    public Specification<Listing> buildSpecification(ListingSpecificationBuilder builder) {
        return builder.build();
    }
}