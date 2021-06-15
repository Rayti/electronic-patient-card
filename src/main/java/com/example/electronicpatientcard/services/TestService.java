package com.example.electronicpatientcard.services;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import com.example.electronicpatientcard.constants.Constant;
import com.example.electronicpatientcard.model.SimpleObservation;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class TestService {
    FHIRService service;
    public void doTest() {
        FhirContext context = FhirContext.forR4();

        String serverBase = Constant.LOCAL_SERVER_URL_R4;

        IGenericClient client = context.newRestfulGenericClient(serverBase);

        Bundle results = client
                .search()
                .forResource(Patient.class)
                .returnBundle(Bundle.class)
                .execute();

        results.getEntry().stream()
                .forEach(bundleEntryComponent -> {
                    Patient patient = (Patient)bundleEntryComponent.getResource();
                    patient.getName().forEach(humanName -> System.out.println(humanName.getNameAsSingleString()));
                });


        System.out.println(results.getEntry().size() + " patients found");



    }


}
