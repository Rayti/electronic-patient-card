package com.example.electronicpatientcard.services;

import ca.uhn.fhir.context.FhirContext;

import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.gclient.StringClientParam;
import com.example.electronicpatientcard.constants.Constant;
import com.example.electronicpatientcard.model.SimpleMedicationRequest;
import com.example.electronicpatientcard.model.SimpleObservation;
import com.example.electronicpatientcard.model.SimplePatient;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.r4.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
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

    public List<SimplePatient> getAllPatients() {
        Bundle results = client
                .search()
                .forResource(Patient.class)
                .returnBundle(Bundle.class)
                .execute();

        List<SimplePatient> simplePatientList = new ArrayList<>(bundleToSimplePatientList(results));

        while (results.getLink(IBaseBundle.LINK_NEXT) != null) {
            results = client
                    .loadPage()
                    .next(results)
                    .execute();
            simplePatientList.addAll(bundleToSimplePatientList(results));
        }

        return  simplePatientList;
    }

    private List<SimplePatient> bundleToSimplePatientList(Bundle result){
        return result.getEntry()
                .stream()
                .map(bundleEntryComponent -> {
                    Patient patient = (Patient) bundleEntryComponent.getResource();
                    return patientConverter.convertPatientToSimplePatient(patient);
                })
                .collect(Collectors.toList());
    }

    public List<SimplePatient> getPatientsWithNames(String name) {
        return this.getAllPatients().stream()
                .filter(simplePatient -> simplePatient.getName().toUpperCase().contains(name))
                .collect(Collectors.toList());
    }

    public List<SimpleObservation> getObservations(String id) {
        Bundle result = client
                .search()
                .forResource(Observation.class)
                .where(Observation.SUBJECT.hasId("Patient/" + id))
                .returnBundle(Bundle.class)
                .execute();

        List<SimpleObservation> simpleObservationList = new ArrayList<>(bundleToSimpleObservationList(result));

        while (result.getLink(IBaseBundle.LINK_NEXT) != null) {
            result = client
                    .loadPage()
                    .next(result)
                    .execute();
            simpleObservationList.addAll(bundleToSimpleObservationList(result));
        }

        return simpleObservationList;
    }

    private List<SimpleObservation> bundleToSimpleObservationList(Bundle bundle){
        return bundle.getEntry().stream()
                .map(bundleEntryComponent -> {
                    Observation observation = (Observation) bundleEntryComponent.getResource();
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

        List<SimpleMedicationRequest> simpleMedicationRequestList = new ArrayList<>(
                bundleToSimpleMediactionRequest(result));

        while (result.getLink(IBaseBundle.LINK_NEXT) != null) {
            result = client
                    .loadPage()
                    .next(result)
                    .execute();
            simpleMedicationRequestList.addAll(bundleToSimpleMediactionRequest(result));
        }

        return simpleMedicationRequestList;
    }

    private List<SimpleMedicationRequest> bundleToSimpleMediactionRequest(Bundle bundle){
        return bundle.getEntry().stream()
                .map(bundleEntryComponent -> {
                    MedicationRequest mr = (MedicationRequest) bundleEntryComponent.getResource();
                    return medicationRequestConverter.convertMedicationRequestToSimpleMedicationRequest(mr);
                })
                .collect(Collectors.toList());
    }

    public List<List<Object>> getPlotObservationData(String patient, String code) {
        List<SimpleObservation> observations = getObservations(patient, code);
        List<List<Object>> result = new ArrayList<>();
        for (SimpleObservation observation : observations
        ) {
            result.add(Arrays.asList(DateHandler.parseToString(observation.getDate()),
                    //observation.getValue() == null ? 2137 : observation.getValue()));
                    observation.getValue()));
        }
        return result;
    }

    public Map<String, List<List<Object>>> getPlotObservationData(String patient) {
        List<SimpleObservation> simpleObservations = getObservations(patient);
        Map<String, List<List<Object>>> jsonResult = new HashMap<>();
        for (SimpleObservation simpleObservation :
                simpleObservations) {
            String code = simpleObservation.getCode();
            if (jsonResult.get(code) == null) {
                List<List<Object>> observations = getPlotObservationData(patient, code);
                jsonResult.put(code, observations);
            }
        }
        return jsonResult;
    }

    public Map<String, String> getDisplays(String patient) {
        Map<String, String> result = new HashMap<>();
        List<SimpleObservation> simpleObservations = getObservations(patient);
        for (SimpleObservation simpleObservation :
                simpleObservations) {
            result.put(simpleObservation.getCode(), simpleObservation.getDisplay());
        }
        return result;
    }

    public Map<String, String> getUnits(String patient) {
        Map<String, String> result = new HashMap<>();
        List<SimpleObservation> simpleObservations = getObservations(patient);
        for (SimpleObservation simpleObservation :
                simpleObservations) {
            result.put(simpleObservation.getCode(), simpleObservation.getUnit());
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
