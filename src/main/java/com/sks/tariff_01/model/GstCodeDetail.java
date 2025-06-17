package com.sks.tariff_01.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@XmlRootElement(name = "properties", namespace = "http://schemas.microsoft.com/ado/2007/08/dataservices/metadata")
@XmlAccessorType(XmlAccessType.FIELD)
public class GstCodeDetail {

    @XmlElement(name = "GSTCode", namespace = "http://schemas.microsoft.com/ado/2007/08/dataservices")
    private String gstCode;

    @XmlElement(name = "Description", namespace = "http://schemas.microsoft.com/ado/2007/08/dataservices")
    private String description;

    @XmlElement(name = "Language", namespace = "http://schemas.microsoft.com/ado/2007/08/dataservices")
    private String language;

    @XmlElement(name = "ExciseTaxRateCheckIndicator", namespace = "http://schemas.microsoft.com/ado/2007/08/dataservices")
    private Boolean exciseTaxRateCheckIndicator;

    @XmlElement(name = "GSTRateType", namespace = "http://schemas.microsoft.com/ado/2007/08/dataservices")
    private String gstRateType;

    @XmlElement(name = "InactiveIndicator", namespace = "http://schemas.microsoft.com/ado/2007/08/dataservices")
    private boolean inactiveIndicator;


    @XmlElement(name = "UpdateOn", namespace = "http://schemas.microsoft.com/ado/2007/08/dataservices")
    private String updateOn;


    // Include other fields as needed + getters/setters
}
