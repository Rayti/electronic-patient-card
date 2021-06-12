package com.example.electronicpatientcard.controllers;

import com.example.electronicpatientcard.model.SimplePatient;
import com.example.electronicpatientcard.services.FHIRService;
import com.example.electronicpatientcard.services.PatientConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class FHIRController {


    FHIRService fhirService;
    PatientConverter patientConverter;

    @Autowired
    public FHIRController(FHIRService fhirService, PatientConverter patientConverter) {
        this.fhirService = fhirService;
        this.patientConverter = patientConverter;
    }

    @GetMapping("/")
    public String mainView(Model model){
        return "index";
    }

    @GetMapping("/patients")
    public String patientsView(Model model){

        List<SimplePatient> simplePatientList = fhirService.getAllPatients().stream()
                .map(patient -> patientConverter.convertPatientToSimplePatient(patient))
                .collect(Collectors.toList());

        model.addAttribute("patients", simplePatientList);

        return "patients";
    }

    @GetMapping("/patient/{name}")
    public String patientView(@PathVariable String name, Model model){




        return "patient";
    }



}
