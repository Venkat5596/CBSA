package com.sks.tariff_01.service;

import com.rometools.rome.feed.synd.SyndContent;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import com.sks.tariff_01.config.GSTClient;
import com.sks.tariff_01.dto.GSTCodesDto;
import com.sks.tariff_01.entity.GSTCodes;
import com.sks.tariff_01.exception.custom.GSTNotFound;
import com.sks.tariff_01.mapper.MapperImpl;
import com.sks.tariff_01.model.GstCodeDetail;
import com.sks.tariff_01.repo.EnhancedGSTCodeRepo;
//import com.sks.tariff_01.repo.GSTCodeRepo;
import com.sks.tariff_01.unmarshiling.UnMarsh;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EnhancedAtomFeedService {

    private final GSTClient gstClient;
    private final UnMarsh unmarsh;
    private final EnhancedGSTCodeRepo repo;
    private final MapperImpl mapper;

    @Cacheable(value = "gstFeed", unless = "#result == null")
    public SyndFeed getFeed(String str) throws IOException {
        try {
            log.info("Fetching GST codes from external API");
            ResponseEntity<String> response=null;
            if(Objects.equals(str, "gst")){
                 response = gstClient.fetchGstCodes();
            }
            else{
                response = gstClient.tariffClariffication();
            }

            if (response.getBody() == null || response.getBody().isEmpty()) {
                throw new GSTNotFound("Empty response from GST API");
            }

            try (XmlReader reader = new XmlReader(
                    new ByteArrayInputStream(response.getBody().getBytes(StandardCharsets.UTF_8)))) {
                SyndFeedInput input = new SyndFeedInput();
                SyndFeed feed = input.build(reader);
                log.info("Successfully fetched {} GST entries", feed.getEntries().size());
                return feed;
            }
        } catch (FeedException e) {
            log.error("Failed to parse GST feed", e);
            throw new RuntimeException("Failed to parse GST feed", e);
        } catch (Exception e) {
            log.error("Failed to fetch GST codes", e);
            throw new RuntimeException("Failed to fetch GST codes", e);
        }
    }

    @Cacheable(value = "gstCodes", key = "#pageable.pageNumber + '-' + #pageable.pageSize")
    public Page<GstCodeDetail> getGstCodeDetails(Pageable pageable) throws Exception {
        log.info("Processing GST codes for page: {}, size: {}", pageable.getPageNumber(), pageable.getPageSize());

        List<GstCodeDetail> gstDetails = new ArrayList<>();
        SyndFeed feed = getFeed("gst");

        for (SyndEntry entry : feed.getEntries()) {
            try {
                SyndContent content = entry.getContents().get(0);
                String rawXml = content.getValue();
                GstCodeDetail detail = unmarsh.unmarshalGstDetails(rawXml);
                gstDetails.add(detail);
            } catch (Exception e) {
                log.warn("Failed to process entry: {}", entry.getTitle(), e);
            }
        }

        if (gstDetails.isEmpty()) {
            throw new GSTNotFound("No GST codes found");
        }

        log.info("Successfully processed {} GST code details", gstDetails.size());
        return new PageImpl<>(gstDetails, pageable, gstDetails.size());
    }

    @Transactional
    public void saveGstCodes(List<GstCodeDetail> details) {
        if (CollectionUtils.isEmpty(details)) {
            log.warn("No GST codes to save");
            return;
        }

        List<GSTCodes> entities = details.stream()
                .map(this::convertToEntity)
                .collect(Collectors.toList());

        try {
            repo.saveAll(entities);
            log.info("Successfully saved {} GST codes to database", entities.size());
        } catch (Exception e) {
            log.error("Failed to save GST codes to database", e);
            throw new RuntimeException("Failed to save GST codes", e);
        }
    }

    private GSTCodes convertToEntity(GstCodeDetail detail) {
        GSTCodes gstCode = new GSTCodes();
        gstCode.setGstCode(detail.getGstCode());
        gstCode.setDescription(detail.getDescription());
        gstCode.setLanguage(detail.getLanguage());
        gstCode.setExciseTaxRateCheckIndicator(detail.getExciseTaxRateCheckIndicator());
        gstCode.setGstRateType(detail.getGstRateType());
        gstCode.setInactiveIndicator(detail.isInactiveIndicator());
        gstCode.setUpdateOn(detail.getUpdateOn());
        return gstCode;
    }

    @Cacheable(value = "gstCodesDb", key = "#pageable.pageNumber + '-' + #pageable.pageSize")
    public Page<GSTCodes> getAllCodes(Pageable pageable) {
        log.info("Fetching GST codes from database - page: {}, size: {}",
                pageable.getPageNumber(), pageable.getPageSize());
        return repo.findAll(pageable);
    }

    @Cacheable(value = "gstRateType", key = "#gstRateType")
    public List<GSTCodes> findByGstRateType(String gstRateType) {
        log.info("Searching GST codes by rate type: {}", gstRateType);
        return repo.findAllByGstRateType(gstRateType);
    }

    @Cacheable(value = "gstSearch", key = "#keyword")
    public List<GSTCodesDto> searchByDescription(String keyword) {
        log.info("Searching GST codes by keyword: {}", keyword);
        List<GSTCodes> gstCodes = repo.searchByDescription(keyword);

        return gstCodes.stream()
                .map(mapper::map)
                .collect(Collectors.toList());
    }

    @CacheEvict(value = {"gstCodes", "gstCodesDb", "gstRateType", "gstSearch", "gstFeed"}, allEntries = true)
    public void clearAllCaches() {
        log.info("Clearing all GST-related caches");
    }

    public long getGstCodesCount() {
        return repo.count();
    }

    public List<String> getAllGstRateTypes() {
        return repo.findDistinctGstRateTypes();
    }
}
