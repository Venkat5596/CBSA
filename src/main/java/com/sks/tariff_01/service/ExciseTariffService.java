package com.sks.tariff_01.service;

import com.rometools.rome.feed.synd.SyndContent;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import com.sks.tariff_01.config.GSTClient;
import com.sks.tariff_01.model.ExciseTaxModel;
import com.sks.tariff_01.unmarshiling.UnMarsh;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Log4j2
public class ExciseTariffService {

    private final CommonService commonService;
    private final UnMarsh unmarsh;
    private final GSTClient gstClient;

    public ExciseTariffService(CommonService commonService, UnMarsh unmarsh, GSTClient gstClient) {
        this.commonService = commonService;
        this.unmarsh = unmarsh;
        this.gstClient = gstClient;
    }

    public Page<ExciseTaxModel> getExciseTariffDetails(Pageable pageable) {

        List<ExciseTaxModel> exciseTaxModel = new ArrayList<>();

        SyndFeed feed = getFeed();

        for (SyndEntry entry : feed.getEntries()) {
            try {
                SyndContent content = entry.getContents().get(0);
                String rawXml = content.getValue();
                ExciseTaxModel model = unmarsh.unmarshalExciseTax(rawXml);
                exciseTaxModel.add(model);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return new PageImpl<>(exciseTaxModel, pageable, exciseTaxModel.size());
    }

    public SyndFeed getFeed() {
        try {
            log.info("Fetching Tariff Clarification from external API");
            ResponseEntity<String> response = gstClient.exciseTaxes();

            try (XmlReader reader = new XmlReader(
                    new ByteArrayInputStream(Objects.requireNonNull(response.getBody()).getBytes(StandardCharsets.UTF_8)))) {
                SyndFeedInput input = new SyndFeedInput();
                SyndFeed feed = input.build(reader);
                log.info("Tariff Clarification fetched successfully");
                return feed;

            } catch (IOException | FeedException e) {
                throw new RuntimeException(e);
            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }

    }
}
