package com.example.electronicpatientcard.services;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.gclient.StringClientParam;
import com.example.electronicpatientcard.constants.Constant;
import com.example.electronicpatientcard.model.SimplePatient;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.MedicationRequest;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class FHIRServiceTest {

    private static FHIRService fhirService;

    @BeforeAll
    static void beforeAll() {

        fhirService = new FHIRService();
    }


    @Test
    void checkMedicationRequests(){
    }

    @Test
    void checkObservations(){

        FhirContext context = FhirContext.forR4();
        String serverBase = Constant.LOCAL_SERVER_URL_R4;
        IGenericClient client = context.newRestfulGenericClient(serverBase);

        PatientConverter patientConverter = new PatientConverter();

        Bundle result1 = client
                .search()
                .forResource(Patient.class)
                .returnBundle(Bundle.class)
                .execute();


        Bundle result2 = client
                .search()
                .forResource(Observation.class)
                //.where(new StringClientParam("patient").matches().value("http://hapi.fhir.org/baseR4/Patient/1206248/_history/1"))
                .returnBundle(Bundle.class)
                .execute();

        List<SimplePatient> simplePatients = new ArrayList<>();

        result1.getEntry().forEach(bundleEntryComponent -> {
            Patient patient = (Patient)bundleEntryComponent.getResource();
            simplePatients.add(patientConverter.convertPatientToSimplePatient(patient));
        });


        List<Observation> observations = new ArrayList<>();

        result2.getEntry().forEach(bundleEntryComponent -> {
            Observation observation = (Observation)bundleEntryComponent.getResource();
            observations.add(observation);
        });



        List<String> answers = new ArrayList<>();

        simplePatients.forEach(simplePatient -> {
            System.out.println(simplePatient.getUrl());
            client.search().forResource(Observation.class)
                    //.where(new StringClientParam("patient").matches().value(simplePatient.getUrl()))
                    .where(Observation.SUBJECT.hasId("Patient/" + simplePatient.getId()))
                    .returnBundle(Bundle.class)
                    .execute()
                    .getEntry()
                    .forEach(bundleEntryComponent -> {
                        Observation observation = (Observation)bundleEntryComponent.getResource();
                        answers.add("Simple patient id = " + simplePatient.getId() + "; observation url = " + observation.getId());
                        //System.out.println("Simple patient id = " + simplePatient.getId() + "; observation url = " + observation.getId());
                    });

        });

        System.out.println("PATIENTS IDS ############################################ SIZE = " + simplePatients.size());


        simplePatients.forEach(simplePatient -> System.out.println(simplePatient.getId() + "; " + simplePatient.getUrl()));

        System.out.println("PATIENTS AND THEIR OBSERVATIONS ############################################ SIZE = " + answers.size());
        answers.forEach(System.out::println);


        System.out.println("OBSERVATIONS ############################################ SIZE = " + observations.size());

        observations.forEach(observation -> System.out.println(observation.getSubject().getReference() + "; " + observation.getId()));
    }

    @Test
    void getAllPatients() {
    }

    @Test
    void getPatientByName() {
    }

    @Test
    void getObservations() {
    }

    @Test
    void getMedicationStatement() {
    }
}