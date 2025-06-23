package com.sks.tariff_01.controller;


import com.sks.tariff_01.model.ExciseTaxModel;
import com.sks.tariff_01.service.ExciseTariffService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/excise")
public class ExciseTariffController {
    private final ExciseTariffService exciseTariffService;

    public ExciseTariffController(ExciseTariffService exciseTariffService) {
        this.exciseTariffService = exciseTariffService;
    }

    @GetMapping("/get")
    public ResponseEntity<Page<ExciseTaxModel>> getExciseTariffDetails(@PageableDefault(size = 10) Pageable pageable) {
        Page<ExciseTaxModel> exciseTaxModelPage = exciseTariffService.getExciseTariffDetails(pageable);
        return ResponseEntity.ok(exciseTaxModelPage);

    }

}
