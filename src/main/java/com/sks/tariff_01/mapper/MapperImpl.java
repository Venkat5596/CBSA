package com.sks.tariff_01.mapper;

import com.sks.tariff_01.dto.GSTCodesDto;
import com.sks.tariff_01.entity.GSTCodes;

public class MapperImpl {

   public GSTCodesDto map(GSTCodes source) {
        GSTCodesDto gstCodesDto = new GSTCodesDto();
        gstCodesDto.setGstCode(source.getGstCode());
        gstCodesDto.setDescription(source.getDescription());
        gstCodesDto.setLanguage(source.getLanguage());
        gstCodesDto.setExciseTaxRateCheckIndicator(source.getExciseTaxRateCheckIndicator());
        gstCodesDto.setGstRateType(source.getGstRateType());
        gstCodesDto.setInactiveIndicator(source.isInactiveIndicator());
        gstCodesDto.setUpdateOn(source.getUpdateOn());
        return gstCodesDto;

    }
}
