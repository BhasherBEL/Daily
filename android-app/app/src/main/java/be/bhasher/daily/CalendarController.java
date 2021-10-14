package be.bhasher.daily;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.CalendarContract;

import androidx.annotation.RequiresApi;

import java.util.Locale;

public class CalendarController {

    public static final String[] PROJECTION = new String[]{
            CalendarContract.Calendars._ID,
            CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,
            CalendarContract.Calendars.ACCOUNT_NAME,
            CalendarContract.Calendars.ACCOUNT_TYPE,
            CalendarContract.Calendars.CALENDAR_COLOR
    };

    public CalendarController(Context context){
        ContentResolver cr = context.getContentResolver();
        Uri uri = CalendarContract.Calendars.CONTENT_URI;
        Cursor calCursor = cr.query(
                uri,
                PROJECTION,
                CalendarContract.Calendars.VISIBLE + " = 1",
                null,
                CalendarContract.Calendars._ID + " ASC"
        );
        calCursor.moveToFirst();
        do {
            long id = calCursor.getLong(0);
            String displayName = calCursor.getString(1);
        }while(calCursor.moveToNext());
    }

}
