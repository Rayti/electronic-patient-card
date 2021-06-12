package com.example.electronicpatientcard.services;

import com.example.electronicpatientcard.model.SimpleObservation;
import com.example.electronicpatientcard.model.SimpleValueQuantity;
import jdk.internal.util.xml.impl.Pair;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Observation;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ObservationConverter {

    public List<String> getCodingDisplay(Observation observation) {
        return observation.getCode().getCoding()
                .stream()
                .map(Coding::getDisplay)
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
                getCodingDisplay(observation),
                getValueQuantity(observation));
    }

}
