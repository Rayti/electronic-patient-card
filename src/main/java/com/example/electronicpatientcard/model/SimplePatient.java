package com.example.electronicpatientcard.model;

import lombok.Data;

@Data
public class SimplePatient {

    private String id;
    private String url;
    private String name;
    private String gender;
    private String birthDate;
    private String identifier;

    public SimplePatient(String id, String url, String name, String gender, String birthDate, String identifier) {
        this.id = id;
        this.url = url;
        this.name = name;
        this.gender = gender;
        this.birthDate = birthDate;
        this.identifier = identifier;
    }

}
