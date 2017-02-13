package com.example.dessusdi.myfirstapp;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by dessusdi on 13/02/2017.
 * DESSUS Dimitri
 */
public class SettingsActivity extends PreferenceActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.settings);

    }
}
