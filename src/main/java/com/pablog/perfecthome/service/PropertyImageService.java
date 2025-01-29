package com.pablog.perfecthome.service;

import com.pablog.perfecthome.entity.PropertyImage;
import com.pablog.perfecthome.repository.PropertyImageRepository;
import com.pablog.perfecthome.specification.PropertyImageSpecificationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PropertyImageService {

    private final PropertyImageRepository propertyImageRepository;

    @Autowired
    public PropertyImageService(PropertyImageRepository propertyImageRepository) {
        this.propertyImageRepository = propertyImageRepository;
    }

    public List<PropertyImage> findAll() {
        return propertyImageRepository.findAll();
    }

    public Page<PropertyImage> findAll(Pageable pageable) {
        return propertyImageRepository.findAll(pageable);
    }

    public Optional<PropertyImage> findById(Long id) {
        return propertyImageRepository.findById(id);
    }

    public PropertyImage save(PropertyImage propertyImage) {
        return propertyImageRepository.save(propertyImage);
    }

    public void deleteById(Long id) {
        propertyImageRepository.deleteById(id);
    }

    public Page<PropertyImage> findAll(Specification<PropertyImage> spec, Pageable pageable) {
        return propertyImageRepository.findAll(spec, pageable);
    }

    public Specification<PropertyImage> buildSpecification(PropertyImageSpecificationBuilder builder) {
        return builder.build();
    }
}
