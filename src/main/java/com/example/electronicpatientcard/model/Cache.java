package com.example.electronicpatientcard.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class Cache {

    private static Logger logger = LoggerFactory.getLogger(Cache.class);

    private static List<SimplePatient> simplePatientCache = new ArrayList<>();

    public static void updateSimplePatientCache(List<SimplePatient> simplePatients){
        logger.info("SimplePatientCache updated");
        simplePatientCache = simplePatients;
    }

    private static List<SimpleObservation> simpleObservationCache = new ArrayList<>();

    public static void updateSimpleObservationCache(List<SimpleObservation> simpleObservations) {
        logger.info("SimpleObservationCache updated");
        simpleObservationCache = simpleObservationCache;

    }

    public static List<SimplePatient> getSimplePatientCache(){
        return simplePatientCache;
    }

    public static List<SimpleObservation> getSimpleObservationCache() {
        return simpleObservationCache;
    }
}
