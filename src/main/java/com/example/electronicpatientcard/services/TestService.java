package com.example.electronicpatientcard.services;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;

public class TestService {

    public void doTest() {
        FhirContext context = FhirContext.forR4();

        String serverBase = "http://hapi.fhir.org/baseR4";

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
