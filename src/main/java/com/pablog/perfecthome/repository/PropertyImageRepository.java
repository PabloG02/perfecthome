package com.pablog.perfecthome.repository;

import com.pablog.perfecthome.entity.PropertyImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PropertyImageRepository extends JpaRepository<PropertyImage, Long>, JpaSpecificationExecutor<PropertyImage> {
    void deleteByPropertyId(Long propertyId);
}
