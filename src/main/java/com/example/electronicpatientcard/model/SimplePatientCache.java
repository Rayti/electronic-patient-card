package com.example.electronicpatientcard.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class SimplePatientCache {

    private static Logger logger = LoggerFactory.getLogger(SimplePatientCache.class);

    private static List<SimplePatient> cache = new ArrayList<>();

    public static void updateCache(List<SimplePatient> simplePatients){
        logger.info("SimplePatientCache updated");
        cache = simplePatients;
    }


    public static List<SimplePatient> getCache(){
        return cache;
    }

}
