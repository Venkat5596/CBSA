package com.sks.tariff_01.model;


import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;

import static com.sks.tariff_01.model.xml.XML_HEADER;
import static com.sks.tariff_01.model.xml.XML_NAMESPACE;

@Getter
@Setter
@XmlRootElement(name = "properties", namespace = XML_HEADER)
@XmlAccessorType(XmlAccessType.FIELD)
public class TariffModel {

    @XmlElement(name = "TariffNumber", namespace = XML_NAMESPACE)
    private String TariffNumber;

    @XmlElement(name = "AsOfDate", namespace = XML_NAMESPACE)
    private String AsOfDate;

    @XmlElement(name = "Language", namespace = XML_NAMESPACE)
    private String Language;

    @XmlElement(name = "SpecialClassificationIndicator", namespace = XML_NAMESPACE)
    private String SpecialClassificationIndicator;

    @XmlElement(name = "TariffNumberValidStartDate", namespace = XML_NAMESPACE)
    private String TariffNumberValidStartDate;

    @XmlElement(name = "TariffNumberValidEndDate", namespace = XML_NAMESPACE)
    private String TariffNumberValidEndDate;

    @XmlElement(name = "TariffNumberChangeReason", namespace = XML_NAMESPACE)
    private String TariffNumberChangeReason;

    @XmlElement(name = "TariffItemNumber", namespace = XML_NAMESPACE)
    private String TariffItemNumber;

    @XmlElement(name = "TariffItemEffectiveDate", namespace = XML_NAMESPACE)
    private String TariffItemEffectiveDate;

    @XmlElement(name = "TariffItemExpiryDate", namespace = XML_NAMESPACE)
    private String TariffItemExpiryDate;

    @XmlElement(name = "TariffItemChangeReasonCode", namespace = XML_NAMESPACE)
    private String TariffItemChangeReasonCode;

    @XmlElement(name = "AreaCode1", namespace = XML_NAMESPACE)
    private String AreaCode1;

    @XmlElement(name = "AreaCode2", namespace = XML_NAMESPACE)
    private String AreaCode2;

    @XmlElement(name = "AreaCode3", namespace = XML_NAMESPACE)
    private String AreaCode3;

    @XmlElement(name = "UnitOfMeasureCode", namespace = XML_NAMESPACE)
    private String UnitOfMeasureCode;

    @XmlElement(name = "ExchangeDateFlag", namespace = XML_NAMESPACE)
    private boolean ExchangeDateFlag;

    @XmlElement(name = "PermitRequiredIndicator", namespace = XML_NAMESPACE)
    private boolean PermitRequiredIndicator;

    @XmlElement(name = "QuotaIndicator", namespace = XML_NAMESPACE)
    private boolean QuotaIndicator;

    @XmlElement(name = "GSTIndicator", namespace = XML_NAMESPACE)
    private boolean GSTIndicator;

    @XmlElement(name = "InactiveIndicator", namespace = XML_NAMESPACE)
    private boolean InactiveIndicator;

    @XmlElement(name = "Description", namespace = XML_NAMESPACE)
    private String Description;

    @XmlElement(name = "UpdateOn", namespace = XML_NAMESPACE)
    private String UpdateOn;
}
