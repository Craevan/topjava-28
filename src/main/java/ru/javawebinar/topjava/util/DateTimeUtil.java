package ru.javawebinar.topjava.util;

import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static boolean isBetweenHalfOpen(LocalTime lt, LocalTime startTime, LocalTime endTime) {
        return (startTime == null || lt.compareTo(startTime) >= 0) && (endTime == null || lt.compareTo(endTime) < 0);
    }

    public static boolean isInside(LocalDate ldt, LocalDate startDate, LocalDate endDate) {
        return (startDate == null || !ldt.isBefore(startDate)) && (endDate == null || !ldt.isAfter(endDate));
    }

    public static String toString(LocalDateTime ldt) {
        return ldt == null ? "" : ldt.format(DATE_TIME_FORMATTER);
    }

    public static LocalDate parseLocalDate(String param) {
        return StringUtils.hasLength(param) ? LocalDate.parse(param) : null;
    }

    public static LocalTime parseLocalTime(String param) {
        return StringUtils.hasLength(param) ? LocalTime.parse(param) : null;
    }
}
