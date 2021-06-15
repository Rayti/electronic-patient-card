package com.example.electronicpatientcard.model;

import lombok.Data;

@Data
public class SimpleCodingDisplay {
    private String code;
    private String display;

    public SimpleCodingDisplay(String code, String display) {
        this.code = code;
        this.display = display;
    }
}
