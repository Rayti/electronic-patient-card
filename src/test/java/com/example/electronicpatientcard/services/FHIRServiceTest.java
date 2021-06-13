package com.example.electronicpatientcard.services;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.gclient.StringClientParam;
import com.example.electronicpatientcard.constants.Constant;
import com.example.electronicpatientcard.model.SimplePatient;
import org.hl7.fhir.r4.model.Bundle;
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
    void check(){

        FhirContext context = FhirContext.forR4();
        String serverBase = Constant.REMOTE_SERVER_URL_R4;
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


        List<String> answers = new ArrayList<>();

        simplePatients.forEach(simplePatient -> {
            System.out.println(simplePatient.getUrl());
            client.search().forResource(Observation.class)
                    .where(new StringClientParam("patient").matches().value(simplePatient.getUrl()))
                    .returnBundle(Bundle.class)
                    .execute()
                    .getEntry()
                    .forEach(bundleEntryComponent -> {
                        Observation observation = (Observation)bundleEntryComponent.getResource();
                        answers.add("Simple patient id = " + simplePatient.getId() + "; observation url = " + observation.getId());
                        //System.out.println("Simple patient id = " + simplePatient.getId() + "; observation url = " + observation.getId());
                    });

        });

        System.out.println("PATIENTS IDS ############################################");


        simplePatients.forEach(simplePatient -> System.out.println(simplePatient.getId()));

        System.out.println("PATIENTS AND THEIR OBSERVATIONS ############################################");
        answers.forEach(System.out::println);


        System.out.println("OBSERVATIONS ############################################");

        result2.getEntry().forEach(bundleEntryComponent -> {
            Observation observation = (Observation)bundleEntryComponent.getResource();
            System.out.println(observation.getId());
        });
    }

    @Test
    void getAllPatients() {
        List<Patient> patients = fhirService.getAllPatients();
        System.out.println(patients.get(0).getId());
        System.out.println(patients.get(0).getIdBase());
        System.out.println(patients.get(0).getIdElement().getIdPart());



    }

    @Test
    void getPatientByName() {


    }

    @Test
    void getObservations() {

        List<Observation> list = new ArrayList<>();
        PatientConverter converter = new PatientConverter();

        fhirService.getAllPatients().forEach(patient -> {
            SimplePatient simplePatient = converter.convertPatientToSimplePatient(patient);
            System.out.println(simplePatient);
        });
        //fhirService.getAllPatients().forEach(patient -> list.addAll(fhirService.getObservations(converter.convertPatientToSimplePatient(patient).getUrl())));
        System.out.println("num of observations = " + list.size());
    }

    @Test
    void getMedicationStatement() {
    }
}