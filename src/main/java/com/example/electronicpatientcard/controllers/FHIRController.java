package com.example.electronicpatientcard.controllers;

import com.example.electronicpatientcard.model.SimpleObservation;
import com.example.electronicpatientcard.model.SimplePatient;
import com.example.electronicpatientcard.model.SimplePatientCache;
import com.example.electronicpatientcard.services.FHIRService;
import com.example.electronicpatientcard.services.ObservationConverter;
import com.example.electronicpatientcard.services.PatientConverter;
import org.hl7.fhir.r4.model.Observation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class FHIRController {


    FHIRService fhirService;
    PatientConverter patientConverter;
    ObservationConverter observationConverter;

    Logger logger = LoggerFactory.getLogger(FHIRController.class);

    @Autowired
    public FHIRController(FHIRService fhirService,
                          PatientConverter patientConverter,
                          ObservationConverter observationConverter) {
        this.fhirService = fhirService;
        this.patientConverter = patientConverter;
        this.observationConverter = observationConverter;
    }

    @GetMapping("/")
    public String mainView(Model model){
        logger.info("Request GET on /");
        return "index";
    }

    @GetMapping("/patients")
    public String patientsView(Model model){
        logger.info("Request GET on /patients");
        List<SimplePatient> simplePatientList = fhirService.getAllPatients().stream()
                .map(patient -> patientConverter.convertPatientToSimplePatient(patient))
                .collect(Collectors.toList());

        SimplePatientCache.updateCache(simplePatientList);

        model.addAttribute("patients", simplePatientList);

        return "patients";
    }

    @GetMapping("/patient/{id}")
    public String patientView(@PathVariable String id, Model model){
        logger.info("Request GET on /patient/" + id);

        Optional<SimplePatient> optionalSimplePatient = SimplePatientCache.getCache().stream()
                .filter(simplePatient -> simplePatient.getId().equalsIgnoreCase(id))
                .findFirst();

        if (optionalSimplePatient.isPresent()) {
            SimplePatient patient = optionalSimplePatient.get();
            List<SimpleObservation> simpleObservations = fhirService
                    .getObservations(patient.getUrl())
                    .stream()
                    .map(observation -> observationConverter.convertObservationToSimpleObservation(observation))
                    .collect(Collectors.toList());
            model.addAttribute("observations", simpleObservations);
            model.addAttribute("patient", patient);
            return "patient";
        }
        model.addAttribute("msg", "Patient does not exist - server must habe been updated.");
        return "error";
    }



}
