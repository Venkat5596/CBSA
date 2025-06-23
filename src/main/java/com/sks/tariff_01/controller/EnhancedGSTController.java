package com.sks.tariff_01.controller;

import com.sks.tariff_01.dto.GSTCodesDto;
import com.sks.tariff_01.entity.GSTCodes;
import com.sks.tariff_01.exception.custom.GSTNotFound;
import com.sks.tariff_01.model.GstCodeDetail;
import com.sks.tariff_01.service.EnhancedAtomFeedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;

@RestController
@RequestMapping("/api/v1/gst")
//@RequiredArgsConstructor
@Validated
@Slf4j
public class EnhancedGSTController  {

    private final EnhancedAtomFeedService atomFeedService;

    public EnhancedGSTController(EnhancedAtomFeedService atomFeedService) {
//        super();
        this.atomFeedService = atomFeedService;
    }

    @PostMapping("/sync")
    public ResponseEntity<Page<GstCodeDetail>> syncGstCodes(
            @PageableDefault(size = 20) Pageable pageable) {
        try {
            log.info("Starting GST codes sync");
            Page<GstCodeDetail> gstDetails = atomFeedService.getGstCodeDetails(pageable);
            atomFeedService.saveGstCodes(gstDetails.getContent());
            log.info("GST codes sync completed successfully");
            return ResponseEntity.ok(gstDetails);
        } catch (Exception e) {
            log.error("Failed to sync GST codes", e);
            throw new RuntimeException("Failed to sync GST codes", e);
        }
    }

    @DeleteMapping("/cache")
    public ResponseEntity<String> clearCache() {
        log.info("Cache clear requested");
        atomFeedService.clearAllCaches();
        return ResponseEntity.ok("All caches cleared successfully");
    }

    @GetMapping(value = "/codes", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<GSTCodes>> getAllCodes(
            @PageableDefault(size = 20) Pageable pageable) {
        Page<GSTCodes> codes = atomFeedService.getAllCodes(pageable);
        if (codes.isEmpty()) {
            throw new GSTNotFound("No GST codes found in database");
        }
        return ResponseEntity.ok(codes);
    }

    @GetMapping("/search")
    public ResponseEntity<List<GSTCodesDto>> searchByDescription(
            @RequestParam("keyword") @NotBlank @Size(min = 2, max = 100) String keyword) {
        log.info("Searching GST codes by description: {}", keyword);
        List<GSTCodesDto> results = atomFeedService.searchByDescription(keyword.trim());
        return ResponseEntity.ok(results);
    }

    @GetMapping("/rate-type/{rateType}")
    public ResponseEntity<List<GSTCodes>> getByRateType(
            @PathVariable @NotBlank String rateType) {
        log.info("Fetching GST codes by rate type: {}", rateType);
        List<GSTCodes> codes = atomFeedService.findByGstRateType(rateType);
        return ResponseEntity.ok(codes);
    }

    @GetMapping("/rate-types")
    public ResponseEntity<List<String>> getAllRateTypes() {
        List<String> rateTypes = atomFeedService.getAllGstRateTypes();
        return ResponseEntity.ok(rateTypes);
    }

    @GetMapping("/stats/count")
    public ResponseEntity<Long> getGstCodesCount() {
        long count = atomFeedService.getGstCodesCount();
        return ResponseEntity.ok(count);
    }
}
