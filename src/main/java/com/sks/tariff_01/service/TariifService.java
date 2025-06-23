package com.sks.tariff_01.service;

import com.rometools.rome.feed.synd.SyndContent;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import com.sks.tariff_01.config.GSTClient;
import com.sks.tariff_01.model.TariffModel;
import com.sks.tariff_01.unmarshiling.UnMarsh;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
@RequiredArgsConstructor
public class TariifService {

    private static final Logger log = LogManager.getLogger(TariifService.class);
    private final GSTClient gstClient;
    private final UnMarsh unmarsh;

    // IT IS USED TO CALL THE EXTERNAL API TO FETCH THE TARIF DETAILS
    public SyndFeed getFeed()
    {
        try {
            log.info("Fetching Tariff Clarification from external API");
            ResponseEntity<String> response=gstClient.tariffClariffication();

            try(XmlReader reader = new XmlReader(
                    new ByteArrayInputStream(Objects.requireNonNull(response.getBody()).getBytes(StandardCharsets.UTF_8)))){
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


    public Page<TariffModel> getTariffDetails(Pageable pageable) throws IOException {

        List<TariffModel> tariffModel = new ArrayList<>();
        SyndFeed feed = getFeed();

        for (SyndEntry entry : feed.getEntries()) {
            try {
                SyndContent content = entry.getContents().get(0);
                String rawXml = content.getValue();
                TariffModel detail = unmarsh.unmarshalTariffDetails(rawXml);
                tariffModel.add(detail);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
            log.info("Tariff details processed successfully");
            return new PageImpl<>(tariffModel, pageable, tariffModel.size());


    }

}
