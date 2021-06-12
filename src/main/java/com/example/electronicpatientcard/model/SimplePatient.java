package com.example.electronicpatientcard.model;

import lombok.Data;
import org.hl7.fhir.r4.model.Patient;

@Data
public class SimplePatient {

    private String id;
    private String name;
    private String gender;
    private String birthDate;
    private String identifier;

    public SimplePatient(String id, String name, String gender, String birthDate, String identifier) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.birthDate = birthDate;
        this.identifier = identifier;
    }

}
