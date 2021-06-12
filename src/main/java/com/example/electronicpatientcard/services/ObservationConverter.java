package com.example.electronicpatientcard.services;

import com.example.electronicpatientcard.model.SimpleCodingDisplay;
import com.example.electronicpatientcard.model.SimpleObservation;
import com.example.electronicpatientcard.model.SimpleValueQuantity;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Observation;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ObservationConverter {

    public String getId(Observation observation){
        return observation.getIdElement().getIdPart();
    }

    public List<SimpleCodingDisplay> getCodingDisplay(Observation observation) {
        return observation.getCode().getCoding()
                .stream()
                .map(coding -> new SimpleCodingDisplay(coding.getCode(), coding.getDisplay()))
                .collect(Collectors.toList());
    }

    public SimpleValueQuantity getValueQuantity(Observation observation){
        return new SimpleValueQuantity(
                observation.getValueQuantity().getValue().longValue(),
                observation.getValueQuantity().getUnit()
        );
    }

    public SimpleObservation convertObservationToSimpleObservation(Observation observation){
        return new SimpleObservation(
                getId(observation),
                getCodingDisplay(observation),
                getValueQuantity(observation));
    }

}
