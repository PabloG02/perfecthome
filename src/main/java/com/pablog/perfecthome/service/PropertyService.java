package com.pablog.perfecthome.service;

import com.pablog.perfecthome.entity.Property;
import com.pablog.perfecthome.repository.ListingRepository;
import com.pablog.perfecthome.repository.PropertyImageRepository;
import com.pablog.perfecthome.repository.PropertyRepository;
import com.pablog.perfecthome.specification.PropertySpecificationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class PropertyService {

    private final PropertyRepository propertyRepository;
    private final PropertyImageRepository propertyImageRepository;
    private final ListingRepository listingRepository;

    @Autowired
    public PropertyService(PropertyRepository propertyRepository, PropertyImageRepository propertyImageRepository, ListingRepository listingRepository) {
        this.propertyRepository = propertyRepository;
        this.propertyImageRepository = propertyImageRepository;
        this.listingRepository = listingRepository;
    }

    public List<Property> findAll() {
        return propertyRepository.findAll();
    }

    public Page<Property> findAll(Pageable pageable) {
        return propertyRepository.findAll(pageable);
    }

    public Optional<Property> findById(Long id) {
        return propertyRepository.findById(id);
    }

    public Property save(Property property) {
        return propertyRepository.save(property);
    }

    public void deleteById(Long id) {
        // Only allow deletion if there are no listings associated with the property
        Property property = propertyRepository.findById(id).get();
        if (listingRepository.findByProperty(property).isEmpty()) {
            propertyImageRepository.deleteByPropertyId(property.getId());
            propertyRepository.deleteById(id);
        } else {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Cannot delete property with associated listings");
        }
    }

    public Page<Property> findAll(Specification<Property> spec, Pageable pageable) {
        return propertyRepository.findAll(spec, pageable);
    }

    public Specification<Property> buildSpecification(PropertySpecificationBuilder builder) {
        return builder.build();
    }
}