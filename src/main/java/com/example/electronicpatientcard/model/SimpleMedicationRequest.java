package com.example.electronicpatientcard.model;

import lombok.Data;
import org.hl7.fhir.r4.model.MedicationRequest;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class SimpleMedicationRequest {

    private List<String> displays;

    public SimpleMedicationRequest(List<String> displays) {
        this.displays = displays;
    }
}
