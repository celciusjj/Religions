package com.celcius.religions.utils;
import com.celcius.religions.Religions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GFG {
    private final Religions plugin = Religions.getPlugin(Religions.class);
    // Function to print difference in
    // time start_date and end_date
        public String findDifference(Date start_date,
                   Date end_date) {
            // parse method is used to parse
            // the text from a string to
            // produce the date
            Date d1 = (start_date);
            Date d2 = (end_date);

            // Calucalte time difference
            // in milliseconds
            long difference_In_Time
                    = d2.getTime() - d1.getTime();

            // Calucalte time difference in
            // seconds, minutes, hours, years,
            // and days
            long difference_In_Seconds
                    = (difference_In_Time
                    / 1000)
                    % 60;

            long difference_In_Minutes
                    = (difference_In_Time
                    / (1000 * 60))
                    % 60;

            long difference_In_Hours
                    = (difference_In_Time
                    / (1000 * 60 * 60))
                    % 24;

            long difference_In_Years
                    = (difference_In_Time
                    / (1000l * 60 * 60 * 24 * 365));

            long difference_In_Days
                    = (difference_In_Time
                    / (1000 * 60 * 60 * 24))
                    % 365;

            // Print the date difference in
            // years, in days, in hours, in
            // minutes, and in seconds


            String difference;
            /*
            if(plugin.getConfig().getString("transactionLanguage").equalsIgnoreCase("es")){
                if(difference_In_Days == 0) {
                    if (difference_In_Hours == 0) {
                        difference = "Hace " + difference_In_Minutes + " minutos";
                    } else {
                        difference = "Hace " + difference_In_Hours
                                + " horas";
                    }
                }else{
                    difference = "Hace "+ difference_In_Days + " dias";
                }
                return difference;
            }else{
                if(difference_In_Days == 0) {
                    if (difference_In_Hours == 0) {
                        difference = difference_In_Minutes + " min ago";
                    } else {
                        difference = difference_In_Hours + " hours ago";
                    }
                }else{
                    difference = difference_In_Days + " days ago";
                }
                return difference;
            }

             */
            if(difference_In_Days == 0) {
                if (difference_In_Hours == 0) {
                    difference = difference_In_Minutes + " " + plugin.getLang().getString("minutes");
                } else {
                    difference = difference_In_Hours + " " + plugin.getLang().getString("hours");
                }
            }else{
                difference = difference_In_Days + " " + plugin.getLang().getString("days");;
            }
            return difference;
        }
}

