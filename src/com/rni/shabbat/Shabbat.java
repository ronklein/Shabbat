package com.rni.shabbat;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import kankan.wheel.R;
import kankan.wheel.widget.WheelView;
import net.sourceforge.zmanim.ZmanimCalendar;
import net.sourceforge.zmanim.util.GeoLocation;
import net.sourceforge.zmanim.util.Taarich;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class Shabbat extends Activity {
	
	private static final String TAG = "MainActivity";
	private WheelView minutes ;
	
    // Called when the activity is first created. 
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	Locale locale = new Locale("iw");
    	locale.setDefault(locale);
    	  Configuration config2 = new Configuration();
    	    config2.locale = locale;
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        //final WheelView minutes = (WheelView) findViewById(R.id.hour);
 /*       minutes = (WheelView) findViewById(R.id.hour);
        minutes.setViewAdapter(new NumericWheelAdapter(this, 0, 60));
        if (savedInstanceState != null) {
        	minutes.setCurrentItem(savedInstanceState.getInt("minutes"));
        }*/
        //minutes.setCurrentItem(10);
        this.getNextFriday();
        //this.addEventToCalendar();
        
		LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setCostAllowed(true);
		criteria.setPowerRequirement(Criteria.POWER_LOW);

		String provider = locationManager.getBestProvider(criteria, true);
		Location myLocation = locationManager.getLastKnownLocation(provider);

		double latitude = myLocation.getLatitude();
		double longitude = myLocation.getLongitude();
		double elevation = myLocation.getAltitude();
		
		
		String s = String.format("Lat: %2.6f, Long: %2.6f, Alt: %5.6f", latitude,longitude,elevation);
		
		Log.e("Shabat",s);
        if (elevation<0) 
        	elevation=0;
		
		String locationName = "Israel";
		//double latitude = 31.30; //latitude of Israel
		//double longitude = 34.45; //longitude of Israel
		//double elevation = 0; //optional elevation
		//use a Valid Olson Database timezone listed in java.util.TimeZone.getAvailableIDs()
		TimeZone timeZone = TimeZone.getTimeZone("Asia/Jerusalem");
		//create the location object
		GeoLocation location = new GeoLocation(locationName, latitude, longitude, elevation, timeZone);
		//create the ZmanimCalendar
		ZmanimCalendar zc = new ZmanimCalendar(location);
		//optionally set the internal calendar
		//zc.getCalendar().set(1969, Calendar.FEBRUARY, 8);
		
		Log.e("Shabat","Today's Zmanim for " + locationName);
		Log.e("Shabat","Sunrise: " + zc.getSunrise()); //output sunrise
		Log.e("Shabat","Sof Zman Shema GRA: " + zc.getSofZmanShmaGRA()); //output Sof Zman Shema GRA
		Log.e("Shabat","Sunset: " + zc.getSunset()); //output sunset
		
		Calendar c = zc.getCalendar();
		c.setTimeZone(timeZone);
		zc.setCalendar(c);
		
	    String hebDate = Taarich.GetHebrewDate( c, "Hebrew");
	    
		TextView tv = (TextView) findViewById(R.id.hebDateTV);
        tv.setText(hebDate);	
	    
		tv = (TextView) findViewById(R.id.alosValTV);
        tv.setText(TimeOfDay(zc.getAlosHashachar()));
        
        
        tv = (TextView) findViewById(R.id.sunriseValTV);
        tv.setText(TimeOfDay(zc.getSunrise()));
        tv = (TextView) findViewById(R.id.shkiahValTV);
        tv.setText(TimeOfDay(zc.getSunset()));

        tv = (TextView) findViewById(R.id.minchaGedolaValTV);
        tv.setText(TimeOfDay(zc.getMinchaGedola()));        
        tv = (TextView) findViewById(R.id.minchaKetanaValTV);
        tv.setText(TimeOfDay(zc.getMinchaKetana()));
 
        tv = (TextView) findViewById(R.id.tziatValTV);
        tv.setText(TimeOfDay(zc.getTzais()));        
        tv = (TextView) findViewById(R.id.tziat72ValTV);
        tv.setText(TimeOfDay(zc.getTzais72()));
        
        tv = (TextView) findViewById(R.id.chatzotValTV);
        tv.setText(TimeOfDay(zc.getChatzos()));        
  
        tv = (TextView) findViewById(R.id.sofZmanShemaMGValTV);
        tv.setText(TimeOfDay(zc.getSofZmanShmaMGA()));  
        tv = (TextView) findViewById(R.id.sofZmanShemaGRValTV);
        tv.setText(TimeOfDay(zc.getSofZmanShmaGRA())); 
        
        tv = (TextView) findViewById(R.id.sofZmanShemaTefilaMGValTV);
        tv.setText(TimeOfDay(zc.getSofZmanTfilaMGA())); 
        tv = (TextView) findViewById(R.id.sofZmanShemaTefilaGRValTV);
        tv.setText(TimeOfDay(zc.getSofZmanTfilaGRA())); 
 
        tv = (TextView) findViewById(R.id.candleLightingValTV);
        tv.setText(TimeOfDay(zc.getCandelLighting())); 

        tv = (TextView) findViewById(R.id.plagHaminchaValTV);
        tv.setText(TimeOfDay(zc.getPlagHamincha())); 
        
        
        
        
        Log.d(TAG, "starting service");
        Button bindBtn = (Button)findViewById(R.id.bindBtn);
        bindBtn.setOnClickListener(new OnClickListener(){
        @Override
        public void onClick(View arg0) {
        startService(new Intent(Shabbat.this,
        BackgroundService.class));
        }});
        
        Button unbindBtn = (Button)findViewById(R.id.unbindBtn);
        unbindBtn.setOnClickListener(new OnClickListener(){
        @Override
        public void onClick(View arg0) {
        stopService(new Intent(Shabbat.this,
        BackgroundService.class));
        }});
        

    }
    private String TimeOfDay(java.util.Date date)
    {
        if(date != null)
        {
            GregorianCalendar gc = new GregorianCalendar();
            String Time="";
            Boolean IsPM;
            int hour, minute,second;
            IsPM = false;
            gc.setTime(date);
            hour = gc.get(GregorianCalendar.HOUR_OF_DAY);

            //Take 12 off numbers greater than 12, and mark as PM
            /*
            if (hour > 12)
            {
                IsPM = true;
                hour = hour - 12;
            }
            */       	
        
            minute =  gc.get(GregorianCalendar.MINUTE);
            second = gc.get(GregorianCalendar.SECOND);

            Time =Integer.toString(hour) + ":" +  StringifyTwoDigits(minute); //+ ":" +  StringifyTwoDigits(second);
          
            /*if (IsPM == true)
                Time = Time + " PM";
            else
                 Time = Time + " AM";
			*/
            return Time;
        }
        //In some places some time are not available every day, so "N/A" is shown.
            //(I.E. , in Alaska there isn't sunrise and sunset every day.)
        else
            return "N/A";
    }
     
    //Returns a string representing the given number, with at least 2 digits (a "0" is added if neded)
    private String StringifyTwoDigits(int num){
        String str = "";
        str = Integer.toString(num);
        if (str.length() == 1)
            str = "0" + Integer.toString(num);
        return str;
       
    }
 
    
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
       super.onSaveInstanceState(savedInstanceState);

      // Save UI state changes to the savedInstanceState.
      // This bundle will be passed to onCreate if the process is
      // killed and restarted.
  //    savedInstanceState.putInt("minutes", minutes.getCurrentItem());
      //savedInstanceState.putDouble("myDouble", 1.9);
      //savedInstanceState.putInt("MyInt", 1);
      //savedInstanceState.putString("MyString", "Welcome back to Android");
      // etc.
      Toast.makeText(this, "Activity state saved", Toast.LENGTH_LONG).show();
    }
    
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
      super.onRestoreInstanceState(savedInstanceState);
      // Restore UI state from the savedInstanceState.
      // This bundle has also been passed to onCreate.
  //    boolean myBoolean = savedInstanceState.getBoolean("MyBoolean");
  //    double myDouble = savedInstanceState.getDouble("myDouble");
     // int myInt = savedInstanceState.getInt("minutes");
      	//minutes.setCurrentItem(myInt);
  //    String myString = savedInstanceState.getString("MyString");
    }


    
    public void addEventToCalendar() {
    	String[] projection = new String[] { "_id", "name" };
    	Uri calendars = Uri.parse("content://calendar/calendars");
    	     
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
    		String contentURI = "content://com.android.calendar";
	        if (Integer.parseInt(Build.VERSION.SDK) < 8 )
	        	contentURI = "content://calendar";		
    	
    		managedCursor.moveToFirst();
     		int nameColumn = managedCursor.getColumnIndex("name"); 
    		int idColumn = managedCursor.getColumnIndex("_id");
    		String calName = managedCursor.getString(nameColumn);
    		String calId = managedCursor.getString(idColumn);
    		ContentValues event = new ContentValues();
    		event.put("calendar_id", calId);
    		event.put("title","Shabat");
    		event.put("dtstart", Calendar.getInstance().getTimeInMillis() + 60 * 1000 * 12);
    		event.put("dtend", Calendar.getInstance().getTimeInMillis()+ 60 * 1000 * 60);
    		//event.put("transparency", 0);
    		//event.put("visibility", 0);
    		event.put("hasAlarm", 1);
    		    		
         	Uri eventsUri = Uri.parse(contentURI+"/events");
    		Uri url = getContentResolver().insert(eventsUri, event);
    		String eventid = url.getPathSegments().get(url.getPathSegments().size()-1);
    		event = new ContentValues();
       		event.put("event_id", eventid)	;
            event.put("minutes", 10);
            event.put("method", 1);
       		eventsUri = Uri.parse(contentURI+"/reminders");

            url = getContentResolver().insert(eventsUri, event);
    }
    
    public  Date getNextFriday(){
    	Calendar cal = Calendar.getInstance();
    	Date today = cal.getTime();
    	int day = cal.get(Calendar.DAY_OF_WEEK); 
    	int fridayDay = cal.get(Calendar.FRIDAY);
    	
        //TextView tv = (TextView) findViewById(R.id.dayofweek);
        java.text.DateFormat dateFormat =
            android.text.format.DateFormat.getDateFormat(getApplicationContext());
        
        //tv.setText(dateFormat.format(today));
        Time t = new Time();
        t.setToNow();
        //tv.setText("FriDay: "+String.valueOf(Time.FRIDAY)+" - Today:"+String.valueOf(t.weekDay));
        
    	//long today_ms = today.getTime();
    	return today;
    	//long target_ms = date.getTime();
    	//return (target_ms - today_ms)/(1000 * 60 * 60 * 24);
    	}    
}