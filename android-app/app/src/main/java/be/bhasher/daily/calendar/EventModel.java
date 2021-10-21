package be.bhasher.daily.calendar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import be.bhasher.daily.api.AdeScheduler;
import be.bhasher.daily.utils.DateTools;

public class EventModel {
    public final long id;
    public final String name;
    public final long startTime;
    public final Calendar begin = Calendar.getInstance();
    public final Calendar end  = Calendar.getInstance();
    public final long endTime;
    public final int color;
    public final boolean allDay;
    public final String location;

    public EventModel(long id, String name, long startTime, long endTime, int color, boolean allDay, String location){
        this.id = id;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.color = color;
        this.allDay = allDay;
        this.location = location;

        this.begin.setTimeInMillis(startTime);
        this.end.setTimeInMillis(endTime);

        if(allDay){
            this.begin.set(Calendar.HOUR_OF_DAY, 0);
            this.begin.set(Calendar.MINUTE, 0);
            this.end.set(Calendar.HOUR_OF_DAY, 0);
            this.end.set(Calendar.MINUTE, 0);
            this.end.add(Calendar.MILLISECOND, -1);
        }
    }

    public boolean isOneDay(){
        return begin.get(Calendar.YEAR) == end.get(Calendar.YEAR)
                && begin.get(Calendar.MONTH) == end.get(Calendar.MONTH)
                && begin.get(Calendar.DAY_OF_MONTH) == end.get(Calendar.DAY_OF_MONTH);
    }

    public String getParsedTime(){
        if(isOneDay()) return parseTime(begin) + " - " + parseTime(end);
        else return parseTime(begin) + " - " + parseDate(end);
    }

    private static String parseTime(Calendar cal){
        return new SimpleDateFormat("HH:mm", Locale.FRANCE).format(cal.getTime());
    }

    private static String parseDate(Calendar cal){
        return new SimpleDateFormat("dd/MM HH:mm", Locale.FRANCE).format(cal.getTime());
    }

    public String searchAddress(){
        String adeAddress = AdeScheduler.adeScheduler.search(this.location);
        if(adeAddress != null) return adeAddress;
        return this.location;
    }
}
