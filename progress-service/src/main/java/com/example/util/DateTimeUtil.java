package com.example.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    public static String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) return "N/A";
        return dateTime.format(formatter);
    }
    
    public static boolean isWithinWorkingHours() {
        LocalDateTime now = LocalDateTime.now();
        int hour = now.getHour();
        return hour >= 9 && hour <= 17;
    }
    
    public static boolean isTimeToSubmitStatus() {
        LocalDateTime now = LocalDateTime.now();
        int hour = now.getHour();
        int minute = now.getMinute();
        return hour == 17 && minute >= 0 && minute < 60;
    }
}
