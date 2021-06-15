package com.example.electronicpatientcard.controllers;

import com.example.electronicpatientcard.constants.Constant;
import com.example.electronicpatientcard.model.SimpleMedicationRequest;
import com.example.electronicpatientcard.model.SimpleObservation;
import com.example.electronicpatientcard.model.SimplePatient;
import com.example.electronicpatientcard.model.SimplePatientCache;
import com.example.electronicpatientcard.services.FHIRService;
import com.example.electronicpatientcard.services.MedicationRequestConverter;
import com.example.electronicpatientcard.services.ObservationConverter;
import com.example.electronicpatientcard.services.PatientConverter;
import org.hl7.fhir.r4.model.MedicationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class FHIRController {


    FHIRService fhirService;
    PatientConverter patientConverter;
    ObservationConverter observationConverter;
    MedicationRequestConverter medicationRequestConverter;

    Logger logger = LoggerFactory.getLogger(FHIRController.class);

    @Autowired
    public FHIRController(FHIRService fhirService,
                          PatientConverter patientConverter,
                          ObservationConverter observationConverter,
                          MedicationRequestConverter medicationRequestConverter) {
        this.fhirService = fhirService;
        this.patientConverter = patientConverter;
        this.observationConverter = observationConverter;
        this.medicationRequestConverter = medicationRequestConverter;
    }

    @GetMapping("/")
    public String mainView(Model model) {
        logger.info("Request GET on /");
        return "index";
    }

    @GetMapping("/patients")
    public String patientsView(Model model, @RequestParam(required = false) String name) {
        logger.info("Request GET on /patients");
        String finalName = name==null ? "" : name.toUpperCase();
        List<SimplePatient> simplePatientList = fhirService.getPatientsWithNames(finalName);

        Cache.updateSimplePatientCache(simplePatientList);

        model.addAttribute("patients", simplePatientList);

        return "patients";
    }



/*
    TODO when no date provided on /patient/{id} and clicked submit -> ArithmeticException is thrown with info: "long overflow"
        on line 90 or 96 depending on whether only start or end is passed
*/

    @GetMapping("/patient/{id}")
    public String patientView(@PathVariable String id, Model model, @RequestParam(required = false) String start,
                              @RequestParam(required = false) String end) {
        logger.info("Request GET on /patient/" + id + "?start = " + start + "&end=" + end);

        Date startDate, endDate;
        try {
            startDate = DateHandler.parseToDate(start, Constant.DEFAULT_START_DATE_HTML_INPUT_FORMAT);
            endDate = DateHandler.parseToDate(end, Constant.DEFAULT_END_DATE_HTML_INPUT_FORMAT);
        } catch (ParseException e) {
            model.addAttribute("Error with parsing dates");
            logger.error("Error with parsing dates");
            return "error";
        }

        Optional<SimplePatient> optionalSimplePatient = Cache.getSimplePatientCache().stream()
                .filter(simplePatient -> simplePatient.getId().equalsIgnoreCase(id))
                .findFirst();

        // todo: make this code to be selected by clicking

        List<List<Object>> plottedObservations = fhirService.getPlotObservationData(id, "29463-7");
        model.addAttribute("chartData", plottedObservations);
        model.addAttribute("chartTitle", "TEST DUPA TEST");
        model.addAttribute("chartUnit", "TEST UNIT");

        if (optionalSimplePatient.isPresent()) {
            SimplePatient patient = optionalSimplePatient.get();

            List<SimpleObservation> simpleObservations = fhirService.getObservations(patient.getId());

            simpleObservations = fhirService.getObservationsWithDateBoundaries(simpleObservations, startDate, endDate);

            List<SimpleMedicationRequest> medicationRequests = fhirService.getMedicationRequest(patient.getId());

            model.addAttribute("medications", medicationRequests);
            model.addAttribute("observations", simpleObservations);
            model.addAttribute("patient", patient);
            model.addAttribute("startDate", DateHandler.parseToString(startDate));
            model.addAttribute("endDate", DateHandler.parseToString(endDate));
            return "patient";
        }
        model.addAttribute("msg", "Patient does not exist - server must have been updated.");
        return "error";
    }


    @GetMapping("/devcheck")
    public String devCheck(Model model) {
        logger.info("Request GET on /devcheck");

        List<SimplePatient> simplePatientList = fhirService.getAllPatients();

        Cache.updateSimplePatientCache(simplePatientList);

        List<String> patientsWithObservations = new ArrayList<>();

        simplePatientList.forEach(simplePatient -> {
            if (fhirService.getObservations(simplePatient.getId()).size() > 0){
                patientsWithObservations.add(simplePatient.getId());
            }
        });

        model.addAttribute("patients", simplePatientList);
        model.addAttribute("patientsWithObservations", patientsWithObservations);

        return "patients";
    }

    @GetMapping("/test")
    public String testEndpoint(Model model){
        List<SimpleObservation> l = fhirService.getObservations("5c818f3d-7051-4b86-8203-1dc624a91804");
        List<SimpleObservation> list = fhirService.getObservations("b426b062-8273-4b93-a907-de3176c0567d", "29463-7");
        List<List<Object>> obs = fhirService.getPlotObservationData("b426b062-8273-4b93-a907-de3176c0567d", "29463-7");
        for (SimpleObservation s: list
        ) {
            System.out.println(s.toString());
        }
        return "error";
    }

}
