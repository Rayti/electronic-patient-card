package com.example.electronicpatientcard.services;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import com.example.electronicpatientcard.model.SimplePatient;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.MedicationStatement;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class FHIRService {

    private FhirContext context;
    private IGenericClient client;
    private String serverBase;

    public FHIRService() {
        this.context = FhirContext.forR4();
        this.serverBase = "http://hapi.fhir.org/baseR4";
        this.client = context.newRestfulGenericClient(serverBase);
    }

    public List<Patient> getAllPatients(){
        Bundle results = client
                .search()
                .forResource(Patient.class)
                .returnBundle(Bundle.class)
                .execute();

        return  results.getEntry()
                .stream()
                .map(bundleEntryComponent -> (Patient)bundleEntryComponent.getResource())
                .collect(Collectors.toList());
    }

    public Patient getPatientByName(String name){
        Bundle result = client
                .search()
                .forResource(Patient.class)
                .where(Patient.FAMILY.matches().values(name))
                .returnBundle(Bundle.class)
                .execute();

        return result.getEntry().size() > 0 ? (Patient)result.getEntry().get(0).getResource() : null;
    }

    public Observation getObservation(Patient patient){

        return null;
    }

    public MedicationStatement getMedicationStatement(Patient patient){
        return null;
    }




}
