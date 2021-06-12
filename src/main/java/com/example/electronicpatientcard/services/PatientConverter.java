package com.example.electronicpatientcard.services;

import com.example.electronicpatientcard.constants.Constant;
import com.example.electronicpatientcard.model.SimplePatient;
import org.hl7.fhir.r4.model.Patient;
import org.springframework.stereotype.Service;

@Service
public class PatientConverter {

    private String getId(Patient patient){
        return patient.getIdElement().getIdPart();
    }

    private String getUrl(Patient patient) {
        return patient.getId();
    }

    private String getFullName(Patient patient){
        StringBuilder stringBuilder = new StringBuilder();
        patient.getName().forEach(humanName -> stringBuilder.append(humanName.getNameAsSingleString()));
        return stringBuilder.toString();
    }

    private String getGender(Patient patient){
        return patient.getGender() != null ? patient.getGender().getDisplay() : Constant.EMPTY_VALUE;
    }

    private String getBirthDate(Patient patient){
        return patient.getBirthDate() != null ? patient.getBirthDate().toString() : Constant.EMPTY_VALUE;
    }

    //TODO
    private String getIdentifier(Patient patient){
        return Constant.EMPTY_VALUE;
    }

    public SimplePatient convertPatientToSimplePatient(Patient patient){

        return new SimplePatient(
                getId(patient),
                getUrl(patient),
                getFullName(patient),
                getGender(patient),
                getBirthDate(patient),
                getIdentifier(patient)
        );
    }

}
