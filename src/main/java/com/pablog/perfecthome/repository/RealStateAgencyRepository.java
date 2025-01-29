package com.pablog.perfecthome.repository;

import com.pablog.perfecthome.entity.RealStateAgency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RealStateAgencyRepository extends JpaRepository<RealStateAgency, Long>, JpaSpecificationExecutor<RealStateAgency> {
}
