package com.rni.shabbat;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.util.Log;



public class Calendar extends Activity {
	
	public Calendar () {
		
	}
    public void addEventToCalendar(long milliseconds, int reminderMins) {
    	
		Log.d("string Tag", "Start addEventToCalendar");

    	String[] projection = new String[] { "_id", "name" };
    	
    	//Set URI according to SDK version
		String contentURI = "content://com.android.calendar";
        if (Integer.parseInt(Build.VERSION.SDK) < 8 )
        	contentURI = "content://calendar";
        
    	//Uri calendars = Uri.parse(contentURI+"/calendars");
    	Uri calendars = Uri.parse("content://calendar/calendars");
    	//Search for all Calendars which are active
    	Cursor managedCursor =
    	   managedQuery(calendars, projection,
    	   "selected=1", null, null);
    	
    	/*if (managedCursor.moveToFirst()) {
    		 String calName; 
    		 String calId; 
    		 int nameColumn = managedCursor.getColumnIndex("name"); 
    		 int idColumn = managedCursor.getColumnIndex("_id");
    		 do {
    		    calName = managedCursor.getString(nameColumn);
    		    calId = managedCursor.getString(idColumn);
    		    Log.v("string TAG","xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
    		    Log.v("string TAG","Name: "+calName);
    		 } while (managedCursor.moveToNext());
    		}*/
    	
		
    		//choose first (usually default) Calednar
    		managedCursor.moveToFirst();
     		int nameColumn = managedCursor.getColumnIndex("name"); 
    		int idColumn = managedCursor.getColumnIndex("_id");
    		String calName = managedCursor.getString(nameColumn);
    		Log.e("string Tag", "calName: "+calName);
    		String calId = managedCursor.getString(idColumn);
    		ContentValues event = new ContentValues();
    		event.put("calendar_id", calId);
    		event.put("title","Shabat");
    		event.put("dtstart", milliseconds);
    		event.put("dtend", milliseconds);
    		//event.put("transparency", 0);
    		//event.put("visibility", 0);
    		event.put("hasAlarm", 1);
    		
         	Uri eventsUri = Uri.parse(contentURI+"/events");
    		Uri url = getContentResolver().insert(eventsUri, event);
    		String eventid = url.getPathSegments().get(url.getPathSegments().size()-1);
    		event = new ContentValues();
       		event.put("event_id", eventid)	;
            event.put("minutes", reminderMins);
            event.put("method", 1);
       		eventsUri = Uri.parse(contentURI+"/reminders");

            url = getContentResolver().insert(eventsUri, event);
    }

}
