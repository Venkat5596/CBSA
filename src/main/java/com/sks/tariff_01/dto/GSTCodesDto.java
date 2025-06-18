package com.sks.tariff_01.dto;

import com.sks.tariff_01.entity.GSTCodes;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link GSTCodes}
 */
//@Value
@Getter
@Setter
public class GSTCodesDto implements Serializable {
    String gstCode;
    String description;
    String language;
    Boolean exciseTaxRateCheckIndicator;
    String gstRateType;
    boolean inactiveIndicator;
    String updateOn;
}