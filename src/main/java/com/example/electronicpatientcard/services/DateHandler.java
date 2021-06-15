package com.example.electronicpatientcard.services;

import com.example.electronicpatientcard.constants.Constant;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateHandler {

    private static String dateFormatVersion = Constant.HTML_INPUT_DATE_FORMAT;

    public static boolean isValidDate(String dateText) {
        return !(dateText == null || dateText.isEmpty());
    }

    public static Date parseToDate(String dateText, String backupDateText) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat(dateFormatVersion);
        if (isValidDate(dateText)) {
            return dateFormat.parse(dateText);
        }
        return dateFormat.parse(backupDateText);
    }

    public static Date getTodayDate(){
        return new Date();
    }

    public static String parseToString(Date date){
        DateFormat dateFormat = new SimpleDateFormat(dateFormatVersion);
        return dateFormat.format(date);
    }
}
