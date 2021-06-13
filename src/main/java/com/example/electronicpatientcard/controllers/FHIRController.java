package com.example.electronicpatientcard.controllers;

import com.example.electronicpatientcard.constants.Constant;
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
import org.springframework.web.bind.annotation.RequestParam;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.Temporal;
import java.util.Calendar;
import java.util.Date;
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
    public String mainView(Model model) {
        logger.info("Request GET on /");
        return "index";
    }

    @GetMapping("/patients")
    public String patientsView(Model model, @RequestParam(required = false) String name) {

        logger.info("Request GET on /patients");
        String finalName = name==null ? "" : name.toUpperCase();
        List<SimplePatient> simplePatientList = fhirService.getAllPatients().stream()
                .map(patient -> patientConverter.convertPatientToSimplePatient(patient))
                .filter(simplePatient -> simplePatient.getName().toUpperCase().contains(finalName))
                .collect(Collectors.toList());

        SimplePatientCache.updateCache(simplePatientList);

        model.addAttribute("patients", simplePatientList);

        return "patients";
    }

    @GetMapping("/patient/{id}")
    public String patientView(@PathVariable String id, Model model, @RequestParam(required = false) String start,
                              @RequestParam(required = false) String end) {
        logger.info("Request GET on /patient/" + id + "?start = " + start + "&end=" + end);
        DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
        Date startDate, endDate;
        // todo: make better default values for startDate&endDAte
        try {
            startDate = start == null ? dateFormat.parse("1970.01.01") : dateFormat.parse(start);
        } catch (ParseException e) {
            logger.error("Could not parse date " + start + " with format " + Constant.DATE_FORMAT);
            startDate = Date.from(Instant.MIN);
        }
        try {
            endDate = start == null ? Calendar.getInstance().getTime() : dateFormat.parse(end);
        } catch (ParseException e) {
            logger.error("Could not parse date " + end + " with format " + Constant.DATE_FORMAT);
            endDate = Date.from(Instant.MIN);
        }
        // todo: add filtering observations and statements based on startDate and endDate
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
            logger.info(String.valueOf(simpleObservations.size()));
            return "patient";
        }
        model.addAttribute("msg", "Patient does not exist - server must habe been updated.");
        return "error";
    }


}
