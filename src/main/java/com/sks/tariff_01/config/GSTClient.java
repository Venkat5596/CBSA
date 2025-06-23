package com.sks.tariff_01.config;


import org.springframework.http.ResponseEntity;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange(url = "https://ccapi-ipacc.cbsa-asfc.cloud-nuage.canada.ca/v1/tariff-srv")
public interface GSTClient {

    // This maps to GET https://.../v1/tariff-srv/gstCodes
    @GetExchange("/gstCodes")
    ResponseEntity<String> fetchGstCodes();

    @GetExchange("/tariffClassifications")
    ResponseEntity<String> tariffClariffication();
    @GetExchange("/exciseTaxes")
    ResponseEntity<String> exciseTaxes();
}
