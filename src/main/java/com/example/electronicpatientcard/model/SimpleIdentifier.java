package com.example.electronicpatientcard.model;

import lombok.Data;

@Data
public class SimpleIdentifier {

    private String text;
    private String value;

    public SimpleIdentifier(String text, String value) {
        this.text = text;
        this.value = value;
    }
}
