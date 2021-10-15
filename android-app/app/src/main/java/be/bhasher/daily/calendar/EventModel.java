package be.bhasher.daily.calendar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import be.bhasher.daily.utils.DateTools;

public class EventModel {
    private final long id;
    private final String name;
    private final long startTime;
    private final long endTime;
    private final int color;

    public EventModel(long id, String name, long startTime, long endTime, int color){
        this.id = id;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.color = color;
    }

    public String getName(){
        return name;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public int getColor() {
        return color;
    }

    public boolean isFinish() {
        return endTime < System.currentTimeMillis();
    }

    public String getParsedTime(){
        return parseTime(startTime) + " - " + parseTime(endTime);
    }

    private static String parseTime(long time){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);

        DateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.FRANCE);

        return dateFormat.format(calendar.getTime());
    }

    public String getDate(String nearFormat, String farFormat){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(this.startTime);

        DateFormat dateFormat;
        if(calendar.getTime().getYear() == Calendar.getInstance().getTime().getYear()){
             dateFormat = new SimpleDateFormat(nearFormat, Locale.FRANCE);
        }else{
            dateFormat = new SimpleDateFormat(farFormat, Locale.FRANCE);
        }

        return dateFormat.format(calendar.getTime());
    }

    public String getStartTime(String format){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(this.startTime);

        DateFormat dateFormat = new SimpleDateFormat(format, Locale.FRANCE);

        return dateFormat.format(calendar.getTime());

    }
}