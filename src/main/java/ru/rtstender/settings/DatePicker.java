package ru.rtstender.settings;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class DatePicker {

    public static String today(){
        Calendar calendar = new GregorianCalendar();
        SimpleDateFormat formattedDate = new SimpleDateFormat("dd.MM.yyyy");
        return formattedDate.format(calendar.getTime());
    }

    public static String yesterday(){
        Calendar calendar = new GregorianCalendar();
        SimpleDateFormat formattedDate = new SimpleDateFormat("dd.MM.yyyy");
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        return formattedDate.format(calendar.getTime());
    }

}
