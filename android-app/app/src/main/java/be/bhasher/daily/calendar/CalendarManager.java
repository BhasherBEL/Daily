package be.bhasher.daily.calendar;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.util.Pair;

import java.util.ArrayList;
import java.util.Date;
import java.util.Stack;

import be.bhasher.daily.EventModel;

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

    public ArrayList<EventModel> getEventsByTime(long dt_from, long dt_to){
        Uri.Builder builder = eventsWhenUri.buildUpon();
        long now = new Date().getTime();

        ContentUris.appendId(builder, now - dt_from);
        ContentUris.appendId(builder, now + dt_to);

        Cursor eventCursor = this.contentResolver.query(
                builder.build(),
                EVENT_COLUMNS,
                CalendarContract.Calendars.VISIBLE + " = 1",
                null,
                "startDay ASC, startMinute ASC"
        );

        ArrayList<EventModel> events = new ArrayList<>();

        if(eventCursor.getCount() > 0 && eventCursor.moveToFirst()){
            do{
                long id = eventCursor.getLong(0);
                String title = eventCursor.getString(1);
                long start = eventCursor.getLong(2);
                long end = eventCursor.getLong(3);
                int color = eventCursor.getInt(4);

                events.add(new EventModel(id, title, start, end, color));
            }while(eventCursor.moveToNext());
        }

        return events;
    }

    public ArrayList<EventModel> getEventsByTime(long to){
        return getEventsByTime(0, to);
    }

    public ArrayList<Object> addSeparators(ArrayList<EventModel> events){
        ArrayList<Object> items = new ArrayList<>();
        Stack<Pair<Integer, String>> stack = new Stack<>();

        String previousDate = "";
        for(int i=0;i<events.size();i++){
            items.add(events.get(i));
            if(events.get(i).getDate("EEEE d MMMM", "d MMMM yyyy").equals(previousDate)) continue;
            else{
                previousDate = events.get(i).getDate("EEEE d MMMM", "d MMMM yyyy");
                stack.add(new Pair<>(i, previousDate));
            }
        }

        while(!stack.empty()){
            Pair<Integer, String> pair = stack.pop();
            items.add(pair.first, pair.second);
        }

        return items;
    }
}
