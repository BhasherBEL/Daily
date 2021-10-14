package be.bhasher.daily.calendar;

import java.util.Date;
import java.util.Locale;

public enum Frequency {
    SECONDLY("SECONDLY", 1000),
    MINUTELY("MINUTELY", Frequency.SECONDLY.period*60),
    HOURLY("HOURLY", Frequency.MINUTELY.period*60),
    DAILY("DAILY", Frequency.HOURLY.period*24),
    WEEKLY("WEEKLY", Frequency.DAILY.period*7),
    MONTHLY("MONTHLY", -1),
    YEARLY("YEARLY", -1);

    private long period;
    private String name;

    Frequency(String name, long period){
        this.name = name;
        this.period = period;
    }

    public static Frequency parse(String name){
        if(name == null) return null;

        for(Frequency frequency: Frequency.values()){
            if(frequency.name.equals(name.toUpperCase(Locale.ROOT))) return frequency;
        }
        return null;
    }

    public long next(long previous){
        if(this.period != -1){
            return previous + this.period;
        }
        return -1;
    }
}
