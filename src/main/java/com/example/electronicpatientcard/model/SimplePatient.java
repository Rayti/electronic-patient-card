package com.example.electronicpatientcard.model;

import lombok.Data;
import org.hl7.fhir.r4.model.Patient;

@Data
public class SimplePatient {

    private String name;
    private String gender;
    private String birthDate;
    private String identifier;

    public SimplePatient(String name, String gender, String birthDate, String identifier) {
        this.name = name;
        this.gender = gender;
        this.birthDate = birthDate;
        this.identifier = identifier;
    }

}
