package com.example.dessusdi.myfirstapp;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

/**
 * Created by dessusdi on 13/02/2017.
 * DESSUS Dimitri
 */

public class SettingsActivity extends PreferenceActivity {

    public SettingsActivity() {}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Replace headers to global settings
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new GlobalFragment()).commit();
    }

    public static class GlobalFragment extends PreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_global);
        }
    }
}
