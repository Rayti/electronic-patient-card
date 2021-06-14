package com.example.electronicpatientcard.services;


import com.example.electronicpatientcard.model.SimpleMedicationRequest;
import org.hl7.fhir.r4.model.MedicationRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MedicationRequestConverter {

    private List<String> getDisplay(MedicationRequest mr){
        return mr.getMedicationCodeableConcept().getCoding()
                .stream()
                .map(coding -> coding.getDisplay())
                .collect(Collectors.toList());
    }

    public SimpleMedicationRequest convertMedicationRequestToSimpleMedicationRequest(MedicationRequest medicationRequest) {
        return new SimpleMedicationRequest(getDisplay(medicationRequest));
    }

}
