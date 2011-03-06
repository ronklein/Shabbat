package com.rni.shabbat;

import com.rni.shabat.R;

import android.os.Bundle;
import android.preference.PreferenceActivity;


public class ShabbatPreference extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
    }
}