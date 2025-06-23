package com.sks.tariff_01.service;

import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import com.sks.tariff_01.config.GSTClient;
import com.sks.tariff_01.model.ExciseTaxModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Component
@Log4j2
@RequiredArgsConstructor
public class CommonService {

    private final GSTClient gstClient;



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
