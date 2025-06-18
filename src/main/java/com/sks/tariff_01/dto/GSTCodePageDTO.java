package com.sks.tariff_01.dto;

import com.sks.tariff_01.entity.GSTCodes;
import jakarta.persistence.SequenceGenerators;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GSTCodePageDTO {
    private List<GSTCodes> content;
    private int totalPages;
    private long totalElements;
    private int size;
    private int number;

    public GSTCodePageDTO(List<GSTCodes> mapped, int totalPages, long totalElements, int size, int number) {
        this.content = mapped;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.size = size;
        this.number = number;
    }

    // Constructors, Getters, Setters
}
