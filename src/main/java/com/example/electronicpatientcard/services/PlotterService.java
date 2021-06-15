package com.example.electronicpatientcard.services;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;



@Service
public class PlotterService {

    public static List<List<Object>> mockPlotData(){
        Random random = new Random();
        List<List<Object>> returns = new ArrayList();
        List<Object> elem = new ArrayList<>();
        elem.add("a");
        elem.add(1);
        List<Object> elem2 = Arrays.asList("b", 2);
        //returns.add(new ArrayList<Object>("a", 1));

        return(Arrays.asList(Arrays.asList("'a'", 1), Arrays.asList("'b'", 2))
        );
    }
}
