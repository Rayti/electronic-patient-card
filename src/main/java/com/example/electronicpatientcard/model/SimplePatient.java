package com.example.electronicpatientcard.model;

import lombok.Data;

import java.util.List;

@Data
public class SimplePatient {

    private String id;
    private String url;
    private String name;
    private String gender;
    private String birthDate;
    private List<SimpleIdentifier> identifier;

    public SimplePatient(String id, String url, String name, String gender, String birthDate, List<SimpleIdentifier> identifier) {
        this.id = id;
        this.url = url;
        this.name = name;
        this.gender = gender;
        this.birthDate = birthDate;
        this.identifier = identifier;
    }

}
