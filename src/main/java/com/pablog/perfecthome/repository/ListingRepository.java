package com.pablog.perfecthome.repository;

import com.pablog.perfecthome.entity.Listing;
import com.pablog.perfecthome.entity.Property;
import com.pablog.perfecthome.entity.RealStateAgency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ListingRepository extends JpaRepository<Listing, Long>, JpaSpecificationExecutor<Listing> {

    List<Listing> findByProperty(Property property);

    List<Listing> findAllByPublisher(RealStateAgency publisher);
}
