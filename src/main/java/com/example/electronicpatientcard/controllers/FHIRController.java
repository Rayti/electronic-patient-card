package com.example.electronicpatientcard.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FHIRController {



    @GetMapping("/")
    public String mainView(Model model){
        return "index";
    }

}
