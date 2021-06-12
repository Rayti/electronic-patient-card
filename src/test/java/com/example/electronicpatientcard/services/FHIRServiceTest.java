package com.example.electronicpatientcard.services;

import com.example.electronicpatientcard.model.SimplePatient;
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