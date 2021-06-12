package com.example.electronicpatientcard.model;


import lombok.Data;

import java.util.List;

@Data
public class SimpleObservation {

    private List<String> codingDisplays;
    private SimpleValueQuantity simpleValueQuantity;

    public SimpleObservation(List<String> codingDisplays, SimpleValueQuantity simpleValueQuantity) {
        this.codingDisplays = codingDisplays;
        this.simpleValueQuantity = simpleValueQuantity;
    }
}
