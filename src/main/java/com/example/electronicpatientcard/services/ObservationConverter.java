package com.example.electronicpatientcard.services;

import com.example.electronicpatientcard.model.SimpleCodingDisplay;
import com.example.electronicpatientcard.model.SimpleObservation;
import com.example.electronicpatientcard.model.SimpleValueQuantity;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Quantity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ObservationConverter {

    private String getId(Observation observation){
        return observation.getIdElement().getIdPart();
    }

    public List<SimpleCodingDisplay> getCodingDisplay(Observation observation) {
        return observation.getCode().getCoding()
                .stream()
                .map(coding -> new SimpleCodingDisplay(coding.getCode(), coding.getDisplay()))
                .collect(Collectors.toList());
    }

    private SimpleValueQuantity getValueQuantity(Observation observation){
        if(observation.getValue() instanceof Quantity){
            return new SimpleValueQuantity(
                    observation.getValueQuantity().getValue().longValue(),
                    observation.getValueQuantity().getUnit());
        }
        return null;
    }

    public SimpleObservation convertObservationToSimpleObservation(Observation observation){
        return new SimpleObservation(
                getId(observation),
                getCodingDisplay(observation),
                getValueQuantity(observation));
    }

}
