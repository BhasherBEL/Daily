package be.bhasher.daily;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.CalendarContract;
import android.util.Pair;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Stack;

import be.bhasher.daily.api.APIS;
import be.bhasher.daily.api.IRail;
import be.bhasher.daily.calendar.ReoccurrenceRule;

public class EventsController {

    public final Date MAX_DATE = new Date();

    public static final String[] PROJECTION = new String[]{
            CalendarContract.Events._ID,
            CalendarContract.Events.TITLE,
            CalendarContract.Events.DTSTART,
            CalendarContract.Events.DTEND,
            CalendarContract.Events.CALENDAR_COLOR,
            CalendarContract.Events.RRULE,
    };
    private final Cursor cursor;

    public EventsController(Context context){
        MAX_DATE.setDate(MAX_DATE.getDate()+31536000);

        System.out.println((new Date()).getDate() + " - " + MAX_DATE.getDate());

        ContentResolver cr = context.getContentResolver();
        Uri uri = CalendarContract.Events.CONTENT_URI;
        Cursor eventsCursor = cr.query(
                uri,
                PROJECTION,
                CalendarContract.Events.VISIBLE + " = 1",
                null,
                CalendarContract.Events._ID + " ASC"
        );
        this.cursor = eventsCursor;
    }

    public ArrayList<EventModel> getEvents(){
        ArrayList<EventModel> events = new ArrayList<>();

        this.cursor.moveToFirst();
        int i = 0;
        try{
            do {
                long id = this.cursor.getLong(0);
                String title = this.cursor.getString(1);
                long start = this.cursor.getLong(2);
                long end = this.cursor.getLong(3);
                int color = this.cursor.getInt(4);
                String rrule = this.cursor.getString(5);

                ReoccurrenceRule rule = new ReoccurrenceRule(start, end, rrule, (new Date()).getTime());
                EventModel eventModel = new EventModel(title, start, end, color);

                if(!eventModel.isFinish()) events.add(eventModel);

                i++;
            }while(this.cursor.moveToNext());
        }catch (CursorIndexOutOfBoundsException e){
            if(BuildConfig.DEBUG) System.err.println(e.getMessage());
        }

        return events;
    }

    public ArrayList<Object> getSortedEvents(){
        ArrayList<EventModel> events = getEvents();
        events.sort(new Comparator<EventModel>() {
            @Override
            public int compare(EventModel eventModel, EventModel t1) {
                return (int) ((eventModel.getStartTime() - t1.getStartTime())/1000);
            }
        });

        HashMap<String, String> arguments = new HashMap<>();

        arguments.put("date", events.get(0).getDate("ddMMyy", "ddMMyy"));
        arguments.put("time", events.get(0).getStartTime("HHmm"));
        arguments.put("from", "boitsfort");
        arguments.put("to", "louvain-la-neuve");
        arguments.put("timeSel", "arrival");
        arguments.put("format", "json");
        arguments.put("fast", "true");

        IRail iRail = new IRail();
        iRail.request(APIS.CONNECTION, arguments);

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
