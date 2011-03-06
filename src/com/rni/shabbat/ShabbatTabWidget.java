package com.rni.shabbat;

import com.rni.shabat.R;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.widget.TabHost;
import android.widget.Toast;


public class ShabbatTabWidget extends TabActivity {
	private TabHost tabHost;  // The activity TabHost

	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.tab);

	    Resources res = getResources(); // Resource object to get Drawables
	    //TabHost tabHost = getTabHost();  // The activity TabHost
	    tabHost = getTabHost();
	    TabHost.TabSpec spec;  // Resusable TabSpec for each tab
	    Intent intent;  // Reusable Intent for each tab

	    // Create an Intent to launch an Activity for the tab (to be reused)
	    intent = new Intent().setClass(this, ShabbatPreference.class);

	    // Initialize a TabSpec for each tab and add it to the TabHost
	    spec = tabHost.newTabSpec("preference").setIndicator("Preferences"/*,
	                      res.getDrawable(R.drawable.ic_tab_preference)*/)
	                  .setContent(intent);
	    tabHost.addTab(spec);

	    // Do the same for the other tabs
	    intent = new Intent().setClass(this, Shabbat.class);
	    spec = tabHost.newTabSpec("main").setIndicator("Shabat"/*,
	                      res.getDrawable(R.drawable.ic_tab_preference	)*/)
	                  .setContent(intent);
	    tabHost.addTab(spec);
	    
	    
	   // View view = getLocalActivityManager().startActivity("ArchiveActivity",
	
	//                new Intent(this, shabat.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)).getDecorView();
	
	       
	
       // setContentView(view);
	    

	    intent = new Intent().setClass(this, MapViewActivity.class);
	    spec = tabHost.newTabSpec("maps").setIndicator("Maps")
	                  .setContent(intent);
	    tabHost.addTab(spec);

	    if (savedInstanceState!=null) {
	    	tabHost.setCurrentTab(savedInstanceState.getInt("TabID", 0));
	    	Log.e("string TAG","This is working: "+String.valueOf(savedInstanceState.getInt("TabID")));
	    }
	    else 
	    	Log.e("string TAG","nulllllll");
	}
	
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

       savedInstanceState.putInt("TabID", tabHost.getCurrentTab());

       Log.e("string TAG","test:"+String.valueOf(tabHost.getCurrentTab()));

      // Save UI state changes to the savedInstanceState.
      // This bundle will be passed to onCreate if the process is
      // killed and restarted.
  //    savedInstanceState.putInt("minutes", minutes.getCurrentItem());
      //savedInstanceState.putDouble("myDouble", 1.9);
      //savedInstanceState.putInt("MyInt", 1);
      //savedInstanceState.putString("MyString", "Welcome back to Android");
      // etc.
      Toast.makeText(this, "2Activity state saved2", Toast.LENGTH_LONG).show();
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
      tabHost.setCurrentTab(savedInstanceState.getInt("TabID", 0));
      Log.e("string TAG","test2:"+String.valueOf(tabHost.getCurrentTab()));

      	
        
    }
}