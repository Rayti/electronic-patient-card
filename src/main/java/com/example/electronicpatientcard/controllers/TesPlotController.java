package com.example.electronicpatientcard.controllers;

import com.example.electronicpatientcard.services.FHIRService;
import com.example.electronicpatientcard.services.PlotterService;
import com.example.electronicpatientcard.services.TestService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TesPlotController {





    public String testPlot(Model model) {
        model.addAttribute("chartData", PlotterService.mockPlotData());
        return "testplot";
    }

    public void test(Model model) {
        TestService testService = new TestService();
        testService.doTest();
    }
}

