package be.bhasher.daily.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateTools {
    public static Date atStartOfDay(Date date) {
        LocalDateTime localDateTime = dateToLocalDateTime(date);
        LocalDateTime startOfDay = localDateTime.with(LocalTime.MIN);
        return localDateTimeToDate(startOfDay);
    }

    public static Date atEndOfDay(Date date) {
        LocalDateTime localDateTime = dateToLocalDateTime(date);
        LocalDateTime endOfDay = localDateTime.with(LocalTime.MAX);
        return localDateTimeToDate(endOfDay);
    }

    private static LocalDateTime dateToLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    private static Date localDateTimeToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static String parseLong(long time, String nearFormat, String farFormat){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);

        DateFormat dateFormat;
        if(calendar.getTime().getYear() == Calendar.getInstance().getTime().getYear()){
            dateFormat = new SimpleDateFormat(nearFormat, Locale.FRANCE);
        }else{
            dateFormat = new SimpleDateFormat(farFormat, Locale.FRANCE);
        }

        return dateFormat.format(calendar.getTime());
    }
}
