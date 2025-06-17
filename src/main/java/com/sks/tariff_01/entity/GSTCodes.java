package com.sks.tariff_01.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class GSTCodes {


    @Id
    private String gstCode;

    private String description;

     private String language;

      private Boolean exciseTaxRateCheckIndicator;

     private String gstRateType;

       private boolean inactiveIndicator;


      private String updateOn;
}
