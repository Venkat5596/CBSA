package com.sks.tariff_01.controller;

import com.sks.tariff_01.dto.GSTCodePageDTO;
import com.sks.tariff_01.dto.GSTCodesDto;
import com.sks.tariff_01.entity.GSTCodes;
import com.sks.tariff_01.mapper.MapperImpl;
import com.sks.tariff_01.model.GstCodeDetail;
import com.sks.tariff_01.service.AtomFeedService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class GraphController {
    private final AtomFeedService atomFeedService;
    private final MapperImpl mapper = new MapperImpl();
    public GraphController(AtomFeedService atomFeedService) {
        this.atomFeedService = atomFeedService;
    }

    @QueryMapping
    public GSTCodePageDTO fetchGSTCodes(@Argument int page, @Argument int size) throws Exception {
        Pageable pageable = PageRequest.of(page, size);
        Page<GstCodeDetail> gstDetails = atomFeedService.content(pageable);
        atomFeedService.savedGST(gstDetails.getContent());

        List<GSTCodes> mapped = gstDetails.getContent().stream().map(detail -> {

            GSTCodes code = new GSTCodes();
            code.setGstCode(detail.getGstCode());
            code.setDescription(detail.getDescription());
            code.setLanguage(detail.getLanguage());
            code.setExciseTaxRateCheckIndicator(detail.getExciseTaxRateCheckIndicator());
            code.setGstRateType(detail.getGstRateType());
            code.setInactiveIndicator(detail.isInactiveIndicator());
            code.setUpdateOn(detail.getUpdateOn());
            return code;
        }).toList();

        return new GSTCodePageDTO(mapped, gstDetails.getTotalPages(), gstDetails.getTotalElements(),
                gstDetails.getSize(), gstDetails.getNumber());
    }

    @QueryMapping
    public List<GSTCodes> fetchGSTRateType(@Argument String gstRateType) {

        return atomFeedService.findByGstRateType(gstRateType);
    }

    @QueryMapping
    public List<GSTCodesDto> searchByDescription(@Argument String keyword) {
        return atomFeedService.searchByDescription(keyword);
    }
}


