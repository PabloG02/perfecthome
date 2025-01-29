package com.pablog.perfecthome.controller;

import com.pablog.perfecthome.entity.RealStateAgency;
import com.pablog.perfecthome.service.RealStateAgencyService;
import com.pablog.perfecthome.specification.RealStateAgencySpecificationBuilder;
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
import java.util.Optional;

@RestController
@RequestMapping(path = "/real-state-agencies", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin
public class RealStateAgencyController {

    private final RealStateAgencyService realStateAgencyService;

    @Autowired
    public RealStateAgencyController(RealStateAgencyService realStateAgencyService) {
        this.realStateAgencyService = realStateAgencyService;
    }

    @GetMapping
    public ResponseEntity<Page<RealStateAgency>> getAllRealStateAgencies(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String website,
            @RequestParam(required = false) String cif,
            @RequestParam(required = false) Boolean isVerified,
            @RequestParam(required = false) Boolean isDisabled,
            @PageableDefault(sort = "id", direction = Sort.Direction.ASC, size = 10) Pageable pageable) {

        RealStateAgencySpecificationBuilder builder = new RealStateAgencySpecificationBuilder()
                .withName(name)
                .withDescription(description)
                .withAddress(address)
                .withPhone(phone)
                .withEmail(email)
                .withWebsite(website)
                .withCif(cif)
                .withIsVerified(isVerified)
                .withIsDisabled(isDisabled);

        Page<RealStateAgency> realStateAgencies = realStateAgencyService.findAll(realStateAgencyService.buildSpecification(builder), pageable);
        return new ResponseEntity<>(realStateAgencies, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RealStateAgency> getRealStateAgencyById(@PathVariable Long id) {
        Optional<RealStateAgency> realStateAgency = realStateAgencyService.findById(id);
        return realStateAgency.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<RealStateAgency> createRealStateAgency(@Valid @RequestBody RealStateAgency realStateAgency) {
        RealStateAgency createdRealStateAgency = realStateAgencyService.save(realStateAgency);
        URI location = buildLocation(createdRealStateAgency.getId());
        return ResponseEntity.created(location).body(createdRealStateAgency);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RealStateAgency> updateRealStateAgency(@PathVariable Long id, @Valid @RequestBody RealStateAgency realStateAgencyDetails) {
        Optional<RealStateAgency> existingRealStateAgency = realStateAgencyService.findById(id);
        if (existingRealStateAgency.isPresent()) {
            RealStateAgency realStateAgency = existingRealStateAgency.get();
            realStateAgency.setName(realStateAgencyDetails.getName());
            realStateAgency.setDescription(realStateAgencyDetails.getDescription());
            realStateAgency.setAddress(realStateAgencyDetails.getAddress());
            realStateAgency.setPhone(realStateAgencyDetails.getPhone());
            realStateAgency.setEmail(realStateAgencyDetails.getEmail());
            realStateAgency.setWebsite(realStateAgencyDetails.getWebsite());
            realStateAgency.setLogoUrl(realStateAgencyDetails.getLogoUrl());
            realStateAgency.setCif(realStateAgencyDetails.getCif());
            realStateAgency.setVerified(realStateAgencyDetails.getVerified());
            RealStateAgency updatedRealStateAgency = realStateAgencyService.save(realStateAgency);
            return new ResponseEntity<>(updatedRealStateAgency, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteRealStateAgency(@PathVariable Long id) {
        if (realStateAgencyService.findById(id).isPresent()) {
            realStateAgencyService.deleteById(id);
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
