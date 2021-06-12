package com.example.electronicpatientcard.services;

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


    }

    @Test
    void getPatientByName() {


    }

    @Test
    void getObservations() {

        List<Observation> list = new ArrayList<>();
        fhirService.getAllPatients().forEach(patient -> list.addAll(fhirService.getObservations(patient)));
        System.out.println("num of observations = " + list.size());
    }

    @Test
    void getMedicationStatement() {
    }
}