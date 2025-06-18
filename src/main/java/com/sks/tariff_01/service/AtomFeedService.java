package com.sks.tariff_01.service;

import com.rometools.rome.feed.synd.SyndContent;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
//import com.sks.tariff_01.config.GSTClient;
import com.sks.tariff_01.dto.GSTCodesDto;
import com.sks.tariff_01.entity.GSTCodes;
import com.sks.tariff_01.exception.custom.GSTNotFound;
import com.sks.tariff_01.mapper.MapperImpl;
import com.sks.tariff_01.model.GstCodeDetail;
import com.sks.tariff_01.repo.GSTCodeRepo;
import com.sks.tariff_01.unmarshiling.UnMarsh;
//import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
//@RequiredArgsConstructor
public class AtomFeedService {
    private final RestTemplate restTemplate ;
    private final UnMarsh unmarsh;
    private final GSTCodeRepo repo;
    private final MapperImpl mapper = new MapperImpl();
    //private final GSTClient client;

    @SuppressWarnings("unused")
    private final String url;

    public AtomFeedService(RestTemplate restTemplate, UnMarsh unmarsh, GSTCodeRepo repo) {
        this.restTemplate = restTemplate;
        this.unmarsh = unmarsh;
        this.repo = repo;
       // this.client = client;
        url = "https://ccapi-ipacc.cbsa-asfc.cloud-nuage.canada.ca/v1/tariff-srv/gstCodes";
    }


    public SyndFeed getFeed() throws IOException {
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
        //ResponseEntity<String> response = client.fetchGstCodes();
        try (XmlReader reader = new XmlReader(new ByteArrayInputStream(Objects.requireNonNull(response.getBody()).getBytes(StandardCharsets.UTF_8)))) {
            SyndFeedInput input = new SyndFeedInput();
            return input.build(reader);
        } catch (FeedException e) {
            throw new RuntimeException(e);
        }
    }





    @Cacheable("gstCodes")
    public Page<GstCodeDetail> content(@PageableDefault(size = 5) Pageable pageable) throws Exception {

        List<GstCodeDetail> saved = new ArrayList<>();
        for (SyndEntry e : getFeed().getEntries()) {
            System.out.println("----------------------------");
            SyndContent content = e.getContents().get(0);
            String rawXml = content.getValue();
            GstCodeDetail detail = unmarsh.unmarshalGstDetails(rawXml);

            saved.add(detail);
        }
        if (saved.isEmpty()) {
            throw new GSTNotFound("No data found");
        }
        return new PageImpl<>(saved, pageable, saved.size());
    }


    public void savedGST(List<GstCodeDetail> details) {
        List<GSTCodes> list = new ArrayList<>();
        for (GstCodeDetail detail : details) {
            GSTCodes gstCode = new GSTCodes();
            gstCode.setGstCode(detail.getGstCode());
            gstCode.setDescription(detail.getDescription());
            gstCode.setLanguage(detail.getLanguage());
            gstCode.setExciseTaxRateCheckIndicator(detail.getExciseTaxRateCheckIndicator());
            gstCode.setGstRateType(detail.getGstRateType());
            gstCode.setInactiveIndicator(detail.isInactiveIndicator());
            gstCode.setUpdateOn(detail.getUpdateOn());

            list.add(gstCode);
        }

        repo.saveAll(list);
    }

    @Cacheable("gstCodes")
    public Page<GSTCodes> getAllCodes( Pageable pageable) {
        return repo.findAll(pageable);
    }

    @Cacheable("gstRateType")
    public List<GSTCodes> findByGstRateType(String gstRateType) {
        return repo.findAllByGstRateType( gstRateType);
    }


    public List<GSTCodesDto> searchByDescription(String keyword) {

        List<GSTCodes> gstCodes = repo.searchByDescription(keyword);
        List<GSTCodesDto> mapped = new ArrayList<>();
        for (GSTCodes gstCode : gstCodes) {
          mapped.add(  mapper.map(gstCode));
        }

        return mapped;
    }
}