package com.example.electronicpatientcard.services;

import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.r4.model.MedicationStatement;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.springframework.stereotype.Service;


@Service
public class FHIRService {

    private static FhirContext context = FhirContext.forR4();

    public Patient getPatientByName(String name){

        return null;
    }

    public Observation getObservation(Patient patient){
        return null;
    }

    public MedicationStatement getMedicationStatement(Patient patient){
        return null;
    }




}
