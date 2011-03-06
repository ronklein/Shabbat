package com.rni.shabbat;

// BackgroundService.java
import java.util.Timer;
import java.util.TimerTask;

import com.rni.shabat.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.text.format.Time;
import android.util.Log;

public class BackgroundService extends Service
{
	private Timer timer = new Timer(); 
	private Context context;

	private NotificationManager notificationMgr;
	@Override
	public void onCreate() {
		super.onCreate();
		this.context = this.getApplicationContext();

		notificationMgr =(NotificationManager)getSystemService(
				NOTIFICATION_SERVICE);
		//Set notification message on serviced start
		displayNotificationMessage( context.getString(R.string.airplane_text));
		
		//Thread thr = new Thread(null, new ServiceWorker(), "BackgroundService");
		//thr.start();
		
		/*timer.scheduleAtFixedRate( new TimerTask() {

			public void run() {

			//Do whatever you want to do every �INTERVAL�
				Log.v("string Tag", "Testing123");
			}

			}, 0, 1000);
		 */
		//Read more: http://www.brighthub.com/mobile/google-android/articles/34861.aspx#ixzz1F119fqdo
		timer.schedule(new ServiceWorker(), 0, 10000);	
		
	}
	
	class ServiceWorker extends TimerTask {
		
		SharedPreferences prefs = PreferenceManager
        .getDefaultSharedPreferences(getBaseContext());
		

		
		public void run () {
			
			Log.v("string Tag", "Testing123");
			Time t = new Time();
			t.setToNow();
			if (t.weekDay==Time.FRIDAY) {
				Log.v("string Tag", "FRIDAY");
		        boolean isEnabled = Settings.System.getInt(context.getContentResolver(), Settings.System.AIRPLANE_MODE_ON, 0) == 1;
		        Log.v("string Tag","Before Flight Mode: "+String.valueOf(isEnabled));
		        if (!isEnabled) {
				    Settings.System.putInt(context.getContentResolver(), Settings.System.AIRPLANE_MODE_ON, 1);
		        }		        
		        Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
		        intent.putExtra("state", !isEnabled);
		        context.sendBroadcast(intent);
		        Log.v("string Tag","After Flight Mode: "+String.valueOf(isEnabled));
				
			} else if (t.weekDay==Time.SATURDAY) {
				Log.v("string Tag", "SATURDAY");
				
		        boolean isEnabled = Settings.System.getInt(context.getContentResolver(), Settings.System.AIRPLANE_MODE_ON, 0) == 1;
		        Log.v("string Tag","Before Flight Mode: "+String.valueOf(isEnabled));
		        if (isEnabled) {
				    Settings.System.putInt(context.getContentResolver(), Settings.System.AIRPLANE_MODE_ON, 0);
		        }		        
		        Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
		        intent.putExtra("state", !isEnabled);
		        context.sendBroadcast(intent);
		        Log.v("string Tag","After Flight Mode: "+String.valueOf(isEnabled));
			} else {
				boolean CalendarPreference = prefs.getBoolean("Calendar_Option", true);
				Log.e("string Tag", "P: "+String.valueOf(CalendarPreference));
				if (CalendarPreference) {
					Log.e("string Tag", "Adding to calendar");
					com.rni.shabbat.Calendar cal = new  com.rni.shabbat.Calendar();
					cal.addEventToCalendar(java.util.Calendar.getInstance().getTimeInMillis() + 60 * 1000 * 12, 11);
					Log.e("string Tag", "Adding to calendar Ended");
				}
				
			}
		}
	
	}
	/*
	class ServiceWorker implements Runnable
	{
		public void run() {
			// do background processing here...
			// stop the service when done...
			// BackgroundService.this.stopSelf();
	       try {
	            // all the stuff we want our Thread to do goes here
	            Thread.sleep(3000);

	            // signaling things to the outside world goes like this
	            threadHandler.sendEmptyMessage(0);
				Log.v("string Tag", "Testing123");

	        } catch (InterruptedException e) {
	            //don't forget to deal with the Exception !!!!!
	        }
		}
		
	    // Receives Thread's messages, interprets them and acts on the
	    // current Activity as needed
	    private Handler threadHandler = new Handler() {
	        public void handleMessage(android.os.Message msg) {
	            // whenever the Thread notifies this handler we have
	            // only this behavior
	            //threadModifiedText.setText("my text changed by the thread");
	        }
	    };
	}*/
	@Override
	public void onDestroy()
	{
		if (timer != null){
			timer.cancel();
			} 
		displayNotificationMessage("stopping Background Service");
		super.onDestroy();
	}
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
	}
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	private void displayNotificationMessage(String message)
	{
		Notification notification = new Notification(R.drawable.icon,
				message,System.currentTimeMillis());
		PendingIntent contentIntent =
			PendingIntent.getActivity(this, 0, new Intent(this, ShabbatTabWidget.class), 0);
		notification.setLatestEventInfo(this, "Background Service",message,
				contentIntent); 
		notificationMgr.notify(R.id.app_notification_id, notification);
	}
}