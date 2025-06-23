package com.sks.tariff_01.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
//import jakarta.xml.bind.annotation.XmlElement;
import lombok.Getter;
import lombok.Setter;

//import static com.sks.tariff_01.model.xml.XML_NAMESPACE;

@Entity
@Setter
@Getter
public class TariffEntity {

    @Id
    private String TariffNumber;

    private String AsOfDate;


    private String Language;


    private String SpecialClassificationIndicator;


    private String TariffNumberValidStartDate;


    private String TariffNumberValidEndDate;


    private String TariffNumberChangeReason;


    private String TariffItemNumber;


    private String TariffItemEffectiveDate;

    private String TariffItemExpiryDate;


    private String TariffItemChangeReasonCode;


    private String AreaCode1;


    private String AreaCode2;


    private String AreaCode3;


    private String UnitOfMeasureCode;

    private boolean ExchangeDateFlag;

    private boolean PermitRequiredIndicator;


    private boolean QuotaIndicator;


    private boolean GSTIndicator;


    private boolean InactiveIndicator;


    private String Description;


    private String UpdateOn;
}
