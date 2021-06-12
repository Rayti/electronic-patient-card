package com.example.electronicpatientcard.model;


import lombok.Data;

import java.util.List;

@Data
public class SimpleObservation {

    private String id;
    private List<SimpleCodingDisplay> codingDisplays;
    private SimpleValueQuantity simpleValueQuantity;

    public SimpleObservation(String id, List<SimpleCodingDisplay> codingDisplays, SimpleValueQuantity simpleValueQuantity) {
        this.id = id;
        this.codingDisplays = codingDisplays;
        this.simpleValueQuantity = simpleValueQuantity;
    }
}
