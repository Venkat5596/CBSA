package com.sks.tariff_01.controller;

import com.sks.tariff_01.dto.GSTCodePageDTO;
import com.sks.tariff_01.dto.GSTCodesDto;
import com.sks.tariff_01.entity.GSTCodes;
import com.sks.tariff_01.mapper.MapperImpl;
import com.sks.tariff_01.model.GstCodeDetail;
import com.sks.tariff_01.service.EnhancedAtomFeedService;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@Log4j2
public class GraphController {

    private final EnhancedAtomFeedService atomFeedService;
    private final MapperImpl mapper = new MapperImpl();

    public GraphController(EnhancedAtomFeedService atomFeedService) {
        this.atomFeedService = atomFeedService;
    }

    /**
     * GraphQL query to fetch GST codes as a paged response.
     *
     * @param page the current page number (zero-indexed)
     * @param size the size of the page
     * @return a DTO encapsulating the paged GST codes and pagination details.
     * @throws Exception if fetching or processing fails.
     */
    @QueryMapping
    public GSTCodePageDTO fetchGSTCodes(@Argument int page, @Argument int size) throws Exception {
        Pageable pageable = PageRequest.of(page, size);
        Page<GstCodeDetail> gstDetails = atomFeedService.getGstCodeDetails(pageable);

        // Save fetched GST codes for persistence (if required)
        atomFeedService.saveGstCodes(gstDetails.getContent());

        // Map GstCodeDetail objects to GSTCodes entities.
        List<GSTCodes> mappedCodes = gstDetails.getContent()
                .stream()
                .map(detail -> {
                    GSTCodes code = new GSTCodes();
                    code.setGstCode(detail.getGstCode());
                    code.setDescription(detail.getDescription());
                    code.setLanguage(detail.getLanguage());
                    code.setExciseTaxRateCheckIndicator(detail.getExciseTaxRateCheckIndicator());
                    code.setGstRateType(detail.getGstRateType());
                    code.setInactiveIndicator(detail.isInactiveIndicator());
                    code.setUpdateOn(detail.getUpdateOn());
                    return code;
                })
                .collect(Collectors.toList());

        return new GSTCodePageDTO(mappedCodes,
                gstDetails.getTotalPages(),
                gstDetails.getTotalElements(),
                gstDetails.getSize(),
                gstDetails.getNumber());
    }

    /**
     * GraphQL query to fetch GST codes filtered by GST rate type.
     *
     * @param gstRateType the GST rate type filter
     * @return list of GSTCodes matching the provided rate type.
     */
    @QueryMapping
    public List<GSTCodes> fetchGSTRateType(@Argument String gstRateType) {
        return atomFeedService.findByGstRateType(gstRateType);
    }

    /**
     * GraphQL query to search GST codes by keyword in their description.
     *
     * @param keyword the search keyword.
     * @return list of matching GSTCodesDto.
     */
    @QueryMapping
    public List<GSTCodesDto> searchByDescription(@Argument String keyword) {
        return atomFeedService.searchByDescription(keyword);
    }

    /**
     * GraphQL mutation to trigger synchronization of GST codes.
     *
     * @param page the page number for paginated fetch (zero-indexed)
     * @param size the page size to fetch per request
     * @return a DTO encapsulating synchronization results and pagination details.
     */
    @MutationMapping
    public GSTCodePageDTO syncGstCodes(@Argument int page, @Argument int size) {
        Pageable pageable = PageRequest.of(page, size);
        try {
            log.info("Starting GST codes sync");
            Page<GstCodeDetail> gstDetails = atomFeedService.getGstCodeDetails(pageable);

            // Save the GST codes obtained from the feed
            atomFeedService.saveGstCodes(gstDetails.getContent());

            log.info("GST codes sync completed successfully");

            // Optionally map to GSTCodes if required; here we demonstrate an empty mapping list,
            // though you might choose to return the persisted data.
            List<GSTCodes> mappedCodes = gstDetails.getContent()
                    .stream()
                    .map(detail -> {
                        GSTCodes code = new GSTCodes();
                        code.setGstCode(detail.getGstCode());
                        code.setDescription(detail.getDescription());
                        code.setLanguage(detail.getLanguage());
                        code.setExciseTaxRateCheckIndicator(detail.getExciseTaxRateCheckIndicator());
                        code.setGstRateType(detail.getGstRateType());
                        code.setInactiveIndicator(detail.isInactiveIndicator());
                        code.setUpdateOn(detail.getUpdateOn());
                        return code;
                    })
                    .collect(Collectors.toList());

            return new GSTCodePageDTO(mappedCodes,
                    gstDetails.getTotalPages(),
                    gstDetails.getTotalElements(),
                    gstDetails.getSize(),
                    gstDetails.getNumber());
        } catch (Exception e) {
            log.error("Failed to sync GST codes", e);
            throw new RuntimeException("Failed to sync GST codes", e);
        }
    }
}