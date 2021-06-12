package com.example.electronicpatientcard.services;

import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
    void getObservation() {
    }

    @Test
    void getMedicationStatement() {
    }
}