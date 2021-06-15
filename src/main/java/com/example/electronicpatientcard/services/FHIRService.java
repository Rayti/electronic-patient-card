package com.example.electronicpatientcard.services;

import ca.uhn.fhir.context.FhirContext;

import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.gclient.StringClientParam;
import com.example.electronicpatientcard.constants.Constant;
import com.example.electronicpatientcard.model.SimpleMedicationRequest;
import com.example.electronicpatientcard.model.SimpleObservation;
import com.example.electronicpatientcard.model.SimplePatient;
import org.hl7.fhir.r4.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class FHIRService {

    private FhirContext context;
    private IGenericClient client;
    private String serverBase;

    private PatientConverter patientConverter;
    private ObservationConverter observationConverter;
    private MedicationRequestConverter medicationRequestConverter;


    public FHIRService() {
        this.context = FhirContext.forR4();
        this.serverBase = Constant.LOCAL_SERVER_URL_R4;
        this.client = context.newRestfulGenericClient(serverBase);
    }

    public List<SimplePatient> getAllPatients(){
        Bundle results = client
                .search()
                .forResource(Patient.class)
                .returnBundle(Bundle.class)
                .execute();

        return  results.getEntry()
                .stream()
                .map(bundleEntryComponent -> {
                    Patient patient = (Patient)bundleEntryComponent.getResource();
                    return patientConverter.convertPatientToSimplePatient(patient);
                })
                .collect(Collectors.toList());
    }

    public SimplePatient getPatient(String id){
        Bundle result = client
                .search()
                .forResource(Patient.class)
                .where(Patient.LINK.hasId(id))
                .returnBundle(Bundle.class)
                .execute();

        Optional<Bundle.BundleEntryComponent> optionalPatient = result.getEntry().stream().findFirst();

        if (optionalPatient.isPresent()) {
            Patient patient = (Patient)optionalPatient.get().getResource();
            return patientConverter.convertPatientToSimplePatient(patient);
        }
        return null;
    }

    public List<SimplePatient> getPatientsWithNames(String name){
        return this.getAllPatients().stream()
                .filter(simplePatient -> simplePatient.getName().toUpperCase().contains(name))
                .collect(Collectors.toList());
    }

    public List<SimpleObservation> getObservations(String id){
        Bundle result = client
                .search()
                .forResource(Observation.class)
                .where(Observation.SUBJECT.hasId("Patient/" + id))
                .returnBundle(Bundle.class)
                .execute();

        return result.getEntry().stream()
                .map(bundleEntryComponent -> {
                    Observation observation = (Observation)bundleEntryComponent.getResource();
                    return observationConverter.convertObservationToSimpleObservation(observation);
                })
                .collect(Collectors.toList());
    }

    public List<SimpleObservation> getObservations(String id, String code) {
            return getObservations(id)
                    .stream()
                    .filter(simpleObservation -> simpleObservation.getCodingDisplays().get(0).getCode().equalsIgnoreCase(code))
                    .collect(Collectors.toList());
    }

    public List<SimpleObservation> getObservationsWithDateBoundaries(List<SimpleObservation> simpleObservations,
                                                                     Date start,
                                                                     Date end) {
        return simpleObservations
                .stream()
                .filter(simpleObservation ->
                        simpleObservation.getDate().after(start) && simpleObservation.getDate().before(end))
                .collect(Collectors.toList());
    }


    public List<SimpleMedicationRequest> getMedicationRequest(String id) {
        Bundle result = client
                .search()
                .forResource(MedicationRequest.class)
                .where(MedicationRequest.SUBJECT.hasId("Patient/" + id))
                .returnBundle(Bundle.class)
                .execute();

        return result.getEntry().stream()
                .map(bundleEntryComponent -> {
                    MedicationRequest mr = (MedicationRequest)bundleEntryComponent.getResource();
                    return medicationRequestConverter.convertMedicationRequestToSimpleMedicationRequest(mr);
                })
                .collect(Collectors.toList());
    }

    public List<List<Object>> getPlotObservationData(String patient, String code){
        List<SimpleObservation> observations = getObservations(patient, code);
        Integer dateMock = 0;
        List<List<Object>> result = new ArrayList<>();
        for (SimpleObservation observation: observations
             ) {
            result.add(Arrays.asList(dateMock.toString(), observation.getSimpleValueQuantity().getValue()));
            dateMock++;
        }
        return result;
    }

    @Autowired
    public void setPatientConverter(PatientConverter patientConverter) {
        this.patientConverter = patientConverter;
    }

    @Autowired
    public void setObservationConverter(ObservationConverter observationConverter) {
        this.observationConverter = observationConverter;
    }

    @Autowired
    public void setMedicationRequestConverter(MedicationRequestConverter medicationRequestConverter) {
        this.medicationRequestConverter = medicationRequestConverter;
    }
}
