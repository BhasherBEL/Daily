package be.bhasher.daily.calendar;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.database.Observable;
import android.net.Uri;
import android.provider.CalendarContract;
import android.text.format.DateUtils;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Observer;

import be.bhasher.daily.utils.DateTools;

public class CalendarManager {
    public static final String[] EVENT_COLUMNS  = new String[]{
            CalendarContract.Instances._ID,
            CalendarContract.Instances.TITLE,
            CalendarContract.Instances.BEGIN,
            CalendarContract.Instances.END,
            CalendarContract.Instances.CALENDAR_COLOR,
            CalendarContract.Instances.ALL_DAY,
    };

    private static final Uri calendarUri = CalendarContract.CONTENT_URI;
    private static final Uri eventsUri = CalendarContract.Events.CONTENT_URI;
    private  static final Uri eventsWhenUri = Uri.withAppendedPath(calendarUri, "instances/when");

    private final ContentResolver contentResolver;

    public CalendarManager(Context context){
        contentResolver = context.getContentResolver();
    }

    private ArrayList<EventModel> getEventsByTime(long from, long to){
        Uri.Builder builder = eventsWhenUri.buildUpon();

        ContentUris.appendId(builder, from);
        ContentUris.appendId(builder, to);

        Cursor eventCursor = this.contentResolver.query(
                builder.build(),
                EVENT_COLUMNS,
                CalendarContract.Calendars.VISIBLE + " = 1",
                null,
                "startDay ASC, startMinute ASC"
        );

        ArrayList<EventModel> events = new ArrayList<>();

        if(eventCursor.getCount() > 0 && eventCursor.moveToFirst()) {
            do {
                long id = eventCursor.getLong(0);
                String title = eventCursor.getString(1);
                long start = eventCursor.getLong(2);
                long end = eventCursor.getLong(3);
                int color = eventCursor.getInt(4);
                boolean allDay = eventCursor.getInt(5) == 1;

                events.add(new EventModel(id, title, start, end, color, allDay));
            } while (eventCursor.moveToNext());
        }

        return events;
    }

    public ArrayList<EventModel> getEventsInRange(long dt_from, long dt_to){

        long now = new Date().getTime();

        long from = DateTools.atStartOfDay(new Date(now - dt_from)).getTime();
        long to = DateTools.atEndOfDay(new Date(now + dt_to)).getTime();

        return getEventsByTime(from, to);
    }

    private ArrayList<Object> addSeparator(ArrayList<EventModel> events){
        ArrayList<Object> eventsByDay = new ArrayList<>();

        String lastDay = "";
        for(EventModel event: events){
            String day = DateTools.parseCal(event.begin, "EEEE d MMMM", "d MMMM yyyy");

            if(!day.equals(lastDay)){
                eventsByDay.add(day);
                lastDay = day;
            }

            eventsByDay.add(event);
        }

        return eventsByDay;
    }

    public ArrayList<Object> getEventsInRangeWithSep(long dt_from, long dt_to){
        return addSeparator(getEventsInRange(dt_from, dt_to));
    }

    public ArrayList<Object> getEventsInRangeWithSep(long dt_to){
        return getEventsInRangeWithSep(0, dt_to);
    }
}
