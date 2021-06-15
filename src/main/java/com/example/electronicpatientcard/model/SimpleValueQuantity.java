package com.example.electronicpatientcard.model;


import lombok.Data;

@Data
public class SimpleValueQuantity {
    private Long value;
    private String unit;

    public SimpleValueQuantity(Long value, String unit) {
        this.value = value;
        this.unit = unit;
    }
}
