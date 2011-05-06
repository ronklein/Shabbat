package com.rni.shabbat;

import kankan.wheel.R;
import android.app.ListActivity;
import android.os.Bundle;
import android.provider.Contacts.People;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

public class ShabbatListAdapter extends ListActivity {

     @Override
     protected void onCreate(Bundle savedInstanceState){
         super.onCreate(savedInstanceState);

         // We'll define a custom screen layout here (the one shown above), but
         // typically, you could just use the standard ListActivity layout.
     //    setContentView(R.layout.custom_list_activity_view);

         // Query for all people contacts using the Contacts.People convenience class.
         // Put a managed wrapper around the retrieved cursor so we don't have to worry about
         // requerying or closing it as the activity changes state.
      //   mCursor = this.getContentResolver().query(People.CONTENT_URI, null, null, null, null);
      //   startManagingCursor(mCursor);

         // Now create a new list adapter bound to the cursor.
         // SimpleListAdapter is designed for binding to a Cursor.
         
 		Object[] rows = {"Entry: 17:35", "Exit: 18:24", "Sunrise: 06:25", "Sunset: 18:33", "more info ...", "more info ...", "more info ..."};

         ListAdapter adapter = new ArrayAdapter(
                 this, // Context.
                 R.layout.location_time_info,  // Specify the row template to use (here, two columns bound to the two retrieved cursor
 rows);
         /*
                 mCursor,                                              // Pass in the cursor to bind to.
                 new String[] {People.NAME, People.COMPANY},           // Array of cursor columns to bind to.
                 new int[] {android.R.id.text1, android.R.id.text2});  // Parallel array of which template objects to bind to those columns.
*/
         // Bind to our new adapter.
         setListAdapter(adapter);
     }
 }