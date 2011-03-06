package kankan.wheel.widget;

import kankan.wheel.widget.adapters.NumericWheelAdapter;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.preference.Preference;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.TextView;


public class WheelPreference extends Preference 
                          implements kankan.wheel.widget.OnWheelChangedListener{

	 private int oldValue = 0;
	 
	 public WheelPreference(Context context) {
		  super(context);
		 }	
	 
	 public WheelPreference(Context context, AttributeSet attrs) {
		  super(context, attrs);
		 }
		 
	 public WheelPreference(Context context, AttributeSet attrs, int defStyle) {
		  super(context, attrs, defStyle);
	 }

@Override
protected View onCreateView(ViewGroup parent){
	   LinearLayout layout = new LinearLayout(getContext());
	   
	   LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(
               LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
params1.gravity = Gravity.LEFT;
params1.weight  = 1.0f;


LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
                80,
                LinearLayout.LayoutParams.WRAP_CONTENT);
params2.gravity = Gravity.RIGHT;


LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(
               30,
               LinearLayout.LayoutParams.WRAP_CONTENT);
params3.gravity = Gravity.CENTER;


layout.setPadding(15, 5, 10, 5);
layout.setOrientation(LinearLayout.HORIZONTAL);

TextView view = new TextView(getContext());
view.setText(getTitle());
view.setTextSize(18);
view.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
view.setGravity(Gravity.LEFT);
view.setLayoutParams(params1);


//SeekBar bar = new SeekBar(getContext());

//bar.setMax(maximum);
//bar.setProgress((int)this.oldValue);
//bar.setLayoutParams(params2);
//bar.setOnSeekBarChangeListener(this);
WheelView wheel = new WheelView(getContext());
wheel.setViewAdapter(new NumericWheelAdapter(getContext(), 0, 60));
Log.e("string TAG","dv2:"+String.valueOf(this.oldValue));
wheel.setLayoutParams(params2);
wheel.setCurrentItem(this.oldValue);
wheel.addChangingListener(this);



//this.monitorBox = new TextView(getContext());
//this.monitorBox.setTextSize(12);
//this.monitorBox.setTypeface(Typeface.MONOSPACE, Typeface.ITALIC);
//this.monitorBox.setLayoutParams(params3);
//this.monitorBox.setPadding(2, 5, 0, 0);
//this.monitorBox.setText(bar.getProgress()+"");


layout.addView(view);
layout.addView(wheel);
//layout.addView(this.monitorBox);
layout.setId(android.R.id.widget_frame);	   
return layout;

}

@Override
public void onDependencyChanged (Preference dependency, boolean disableDependent) {
	super.onDependencyChanged(dependency, disableDependent);
//	Log.e("string TAG","depvalue:"+String.valueOf(disableDependent));
	
}


@Override
protected Object onGetDefaultValue(TypedArray ta,int index){
 
  int dValue = (int)ta.getInt(index,10);
   
  return validateValue(dValue);
}


   @Override
   protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
    
    int temp = restoreValue ? getPersistedInt(10) : (Integer)defaultValue;
    
     if(!restoreValue)
       persistInt(temp);
     
     //SharedPreferences preferences =  android.preference.PreferenceManager.getDefaultSharedPreferences(getContext());
     //this.oldValue = preferences.getInt(getKey(), 0);
	  
    
     this.oldValue = temp;
     Log.e("string TAG","dv:"+String.valueOf(this.oldValue));

   }


   private int validateValue(int value){

 
     
    return value;  
   }

@Override
public void onChanged(WheelView wheel, int oldValue, int newValue) {
	
	updatePreference(newValue);
    notifyChanged();
}

private void updatePreference(int newValue){
	  
	  SharedPreferences.Editor editor =  getEditor();
	  editor.putInt(getKey(), newValue);
	  editor.commit();  

	 }

}