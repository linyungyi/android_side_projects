package com.cht.android.music;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class RingtoneSettingsActivity extends PreferenceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.ringtone_settings);
		
        //TODO: Check if user is reading or modifying the settings.
        //TODO: Check which type of ringtone is being read or modified.
    }  
}
