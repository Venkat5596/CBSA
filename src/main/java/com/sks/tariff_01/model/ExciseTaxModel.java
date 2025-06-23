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
public class ExciseTaxModel {

    @XmlElement(name = "TariffNumber", namespace = XML_NAMESPACE)
    private String TariffNumber;
    @XmlElement(name = "ExciseTaxCode", namespace = XML_NAMESPACE)
    private String ExciseTaxCode;
    @XmlElement(name = "AsOfDate", namespace = XML_NAMESPACE)
    private String AsOfDate;
    @XmlElement(name = "Language", namespace = XML_NAMESPACE)
    private String Language;
    @XmlElement(name = "ExciseTaxCodeValidStartDate", namespace = XML_NAMESPACE)
    private String ExciseTaxCodeValidStartDate;
    @XmlElement(name = "ExciseTaxCodeValidEndDate", namespace = XML_NAMESPACE)
    private String ExciseTaxCodeValidEndDate;
  //  private String ExciseTaxCodeChangeReason;
    @XmlElement(name = "InactiveIndicator", namespace = XML_NAMESPACE)
    private String InactiveIndicator;
    @XmlElement(name = "UpdateOn", namespace = XML_NAMESPACE)
    private String UpdateOn;
}
