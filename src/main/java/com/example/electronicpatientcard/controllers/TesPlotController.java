package com.example.electronicpatientcard.controllers;

import com.example.electronicpatientcard.services.PlotterService;
import com.example.electronicpatientcard.services.TestService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/test/plot")
public class TesPlotController {
    @GetMapping
    public String testPlot(Model model) {
        model.addAttribute("chartData", PlotterService.mockPlotData());
        return "testplot";
    }
}

