package be.bhasher.daily.calendar;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class ReoccurrenceRule {

    final private HashMap<String, String> arguments = new HashMap<>();
    private Calendar start = Calendar.getInstance();
    private Calendar end = Calendar.getInstance();
    private Calendar current = Calendar.getInstance();
    private Calendar ignoreBefore = Calendar.getInstance();
    private Calendar until = Calendar.getInstance();

    public ReoccurrenceRule(long start, long end, String rrule, long ignoreBefore){
        this.start.setTimeInMillis(start);
        this.current.setTimeInMillis(start);
        this.end.setTimeInMillis(end);
        this.ignoreBefore.setTimeInMillis(ignoreBefore);

        if(rrule == null) return;

        rrule = rrule.toUpperCase(Locale.ROOT);
        if(rrule.startsWith("RRULE:")) rrule = rrule.substring(6);

        for(String part: rrule.split(";")){
            if(!part.contains("=")) continue;
            String[] item = part.split("=");
            if(item.length != 2) continue;
            this.arguments.put(item[0], item[1]);
        }
    }

    public Calendar getCurrent(){
        return this.current;
    }

    public Calendar next() {
        if(!arguments.containsKey("FREQ")) return null;
        switch (Frequency.parse(arguments.get("FREQ"))){
            case SECONDLY:
                current.add(Calendar.SECOND, 1);
                break;
            case MINUTELY:
                current.add(Calendar.MINUTE, 1);
                break;
            case HOURLY:
                current.add(Calendar.HOUR, 1);
                break;
            case DAILY:
                current.add(Calendar.DATE, 1);
                break;
            case WEEKLY:
                current.add(Calendar.DATE, 7);
                break;
            case MONTHLY:
                current.add(Calendar.MONTH, 1);
                break;
            case YEARLY:
                current.add(Calendar.YEAR, 1);
                break;
            default:
                return null;
        }
        return current;
    }
}

//freq, dtstart=None, interval=1, wkst=None, count=None, until=None, bysetpos=None, bymonth=None, bymonthday=None, byyearday=None, byeaster=None, byweekno=None, byweekday=None, byhour=None, byminute=None, bysecond=None, cache=False