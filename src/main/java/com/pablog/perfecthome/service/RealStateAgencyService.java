package com.pablog.perfecthome.service;

import com.pablog.perfecthome.entity.RealStateAgency;
import com.pablog.perfecthome.repository.ListingRepository;
import com.pablog.perfecthome.repository.RealStateAgencyRepository;
import com.pablog.perfecthome.specification.RealStateAgencySpecificationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RealStateAgencyService {

    private final RealStateAgencyRepository realStateAgencyRepository;
    private final ListingRepository listingRepository;

    @Autowired
    public RealStateAgencyService(RealStateAgencyRepository realStateAgencyRepository, ListingRepository listingRepository) {
        this.realStateAgencyRepository = realStateAgencyRepository;
        this.listingRepository = listingRepository;
    }

    public List<RealStateAgency> findAll() {
        return realStateAgencyRepository.findAll();
    }

    public Page<RealStateAgency> findAll(Pageable pageable) {
        return realStateAgencyRepository.findAll(pageable);
    }

    public Optional<RealStateAgency> findById(Long id) {
        return realStateAgencyRepository.findById(id);
    }

    public RealStateAgency save(RealStateAgency realStateAgency) {
        return realStateAgencyRepository.save(realStateAgency);
    }

    // Soft delete -> The listings and transactions associated must be kept
    public void deleteById(Long id) {
        RealStateAgency realStateAgency = realStateAgencyRepository.findById(id).get();
        realStateAgency.setDisabled(true);
        realStateAgencyRepository.save(realStateAgency);

        // Set the listings associated to the agency as expired
        listingRepository.findAllByPublisher(realStateAgency).forEach(listing -> {
            listing.setExpirationDate(LocalDateTime.now());
        });
    }

    public Page<RealStateAgency> findAll(Specification<RealStateAgency> spec, Pageable pageable) {
        return realStateAgencyRepository.findAll(spec, pageable);
    }

    public Specification<RealStateAgency> buildSpecification(RealStateAgencySpecificationBuilder builder) {
        return builder.build();
    }
}
