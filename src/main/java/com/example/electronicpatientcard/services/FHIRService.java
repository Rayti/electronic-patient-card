package com.example.electronicpatientcard.services;

import ca.uhn.fhir.context.FhirContext;

import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.gclient.StringClientParam;
import com.example.electronicpatientcard.constants.Constant;
import org.hl7.fhir.r4.model.*;
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
        this.serverBase = Constant.LOCAL_SERVER_URL_R4;
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

    public List<Observation> getObservations(String id){
        Bundle result = client
                .search()
                .forResource(Observation.class)
                .where(Observation.SUBJECT.hasId("Patient/" + id))
                .returnBundle(Bundle.class)
                .execute();

        return result.getEntry().stream()
                .map(bundleEntryComponent -> (Observation)bundleEntryComponent.getResource())
                .collect(Collectors.toList());
    }


    public List<MedicationRequest> getMedicationRequest(String id) {
        Bundle result = client
                .search()
                .forResource(MedicationRequest.class)
                .where(MedicationRequest.SUBJECT.hasId("Patient/" + id))
                .returnBundle(Bundle.class)
                .execute();

        return result.getEntry().stream()
                .map(bundleEntryComponent -> (MedicationRequest)bundleEntryComponent.getResource())
                .collect(Collectors.toList());
    }




}
