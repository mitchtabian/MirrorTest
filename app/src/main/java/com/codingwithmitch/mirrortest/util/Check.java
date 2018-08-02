package com.codingwithmitch.mirrortest.util;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Check {

    /**
     * Return true if the string is not empty
     * @param string
     * @return
     */
    public static boolean isStringEmpty(String string){
        return !string.isEmpty();
    }

    /**
     * Return true if string1 and string2 are identical
     * @param string1
     * @param string2
     * @return
     */
    public static boolean isStringsMatch(String string1, String string2){
        return string1.equals(string2);
    }

    public static boolean isValidFormat(String format, String value) {
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            date = sdf.parse(value);
            if (!value.equals(sdf.format(date))) {
                date = null;
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return date != null;
    }

    public static boolean isValidMonth(String month){
        return Integer.parseInt(month) <= 12;
    }

    public static boolean isValidDay(String day){
        return Integer.parseInt(day) < 31;
    }
}
