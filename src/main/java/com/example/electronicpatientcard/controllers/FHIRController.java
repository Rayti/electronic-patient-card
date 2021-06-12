package com.example.electronicpatientcard.controllers;

import com.example.electronicpatientcard.services.FHIRService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FHIRController {


    FHIRService fhirService;

    @Autowired
    public FHIRController(FHIRService fhirService) {
        this.fhirService = fhirService;
    }

    @GetMapping("/")
    public String mainView(Model model){
        return "index";
    }

    @GetMapping("/patients")
    public String patientsView(Model model){
        return "patients";
    }



}
