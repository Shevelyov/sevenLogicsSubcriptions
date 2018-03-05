package com.example.logics.sevenlogicssubscriptions;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateUtilsHelper {

    public static String formatDate(Calendar calendar, SimpleDateFormat sdf){
        sdf.setCalendar(calendar);
        String date = sdf.format(calendar.getTime());
        return date;
    }
}
