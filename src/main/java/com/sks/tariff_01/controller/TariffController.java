package com.sks.tariff_01.controller;


import com.sks.tariff_01.model.TariffModel;
import com.sks.tariff_01.service.TariifService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/tariff")
public class TariffController {

    private final TariifService tariifService;

    public TariffController(TariifService tariifService) {
        this.tariifService = tariifService;
    }

    @GetMapping("/get")
    public ResponseEntity<Page<TariffModel>> getTariff(@PageableDefault(size = 20) Pageable pageable) throws IOException {

        Page<TariffModel> tariffModel = tariifService.getTariffDetails(pageable);
        return ResponseEntity.ok(tariffModel);


    }
}
