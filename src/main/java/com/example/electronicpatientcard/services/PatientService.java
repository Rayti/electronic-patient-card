package com.example.electronicpatientcard.services;

import org.hl7.fhir.r4.model.Patient;
import org.springframework.stereotype.Service;

@Service
public class PatientService {

    public String getFullName(Patient patient){
        StringBuilder stringBuilder = new StringBuilder();
        patient.getName().forEach(humanName -> stringBuilder.append(humanName.getNameAsSingleString()));
        return stringBuilder.toString();
    }

    public String getGender(Patient patient){
        return patient.getGender().getDisplay();
    }

    public String getBirthDate(Patient patient){
        return patient.getBirthDate().toString();
    }

    //TODO
    public String getIdentifier(Patient patient){
        return "";
    }

}
