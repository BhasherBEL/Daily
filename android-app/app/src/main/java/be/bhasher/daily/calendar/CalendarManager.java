package be.bhasher.daily.calendar;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.text.format.DateUtils;

import java.util.ArrayList;
import java.util.Date;

import be.bhasher.daily.utils.DateTools;

public class CalendarManager {
    public static final String[] EVENT_COLUMNS  = new String[]{
            CalendarContract.Events._ID,
            CalendarContract.Events.TITLE,
            CalendarContract.Events.DTSTART,
            CalendarContract.Events.DTEND,
            CalendarContract.Events.CALENDAR_COLOR,
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

                events.add(new EventModel(id, title, start, end, color));
            } while (eventCursor.moveToNext());
        }

        return events;
    }

    public ArrayList<Object> getEventsInRange(long dt_from, long dt_to){
        long now = new Date().getTime();

        long from = DateTools.atStartOfDay(new Date(now - dt_from)).getTime();
        long to = DateTools.atEndOfDay(new Date(now + dt_to)).getTime();

        ArrayList<Object> eventsByDay = new ArrayList<>();

        while(from < to){
            ArrayList<EventModel> events = getEventsByTime(from, DateTools.atEndOfDay(new Date(from)).getTime());
            if(!events.isEmpty()){
                eventsByDay.add(DateTools.parseLong(from, "EEEE d MMMM", "d MMMM yyyy"));
                eventsByDay.addAll(events);
            }
            from += DateUtils.DAY_IN_MILLIS;
        }

        return eventsByDay;
    }

    public ArrayList<Object> getEventsInRange(long to){
        return getEventsInRange(0, to);
    }
}
