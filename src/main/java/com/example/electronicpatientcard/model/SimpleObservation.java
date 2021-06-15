package com.example.electronicpatientcard.model;


import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class SimpleObservation {

    private String id;
    private Date date;
    private List<SimpleCodingDisplay> codingDisplays;
    private SimpleValueQuantity simpleValueQuantity;

    public SimpleObservation(String id, Date date, List<SimpleCodingDisplay> codingDisplays, SimpleValueQuantity simpleValueQuantity) {
        this.id = id;
        this.date = date;
        this.codingDisplays = codingDisplays;
        this.simpleValueQuantity = simpleValueQuantity;
    }
}
