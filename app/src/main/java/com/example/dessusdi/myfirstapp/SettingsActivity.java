package com.example.dessusdi.myfirstapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.util.Log;

import com.example.dessusdi.myfirstapp.tools.LanguageUpdater;

import java.util.Locale;

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

    public static class GlobalFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {

        private LanguageUpdater langUpdater;
        private ListPreference languagePreference;
        private ListPreference themePreferences;

        public GlobalFragment() {}

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_global);

            // Adding listeners to ListPreference
            this.languagePreference  = (ListPreference)findPreference("language_preference");
            this.themePreferences    = (ListPreference)findPreference("theme_preference");
            this.langUpdater         = new LanguageUpdater(this.getActivity(), this.getPreferenceManager().getSharedPreferences());

            // Loading from shared prefs current locale
            this.languagePreference.setSummary(this.langUpdater.getSavedLocale());

            // Setting up listeners on ListPreference
            languagePreference.setOnPreferenceChangeListener(this);
            themePreferences.setOnPreferenceChangeListener(this);
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            if(preference == this.languagePreference) {
                this.langUpdater.buildLanguageConfiguration(newValue.toString(), true);
                return true;
            } else if(preference == this.themePreferences) {
                //TODO: Change programmatically theme
                return true;
            } else {
                return false;
            }
        }
    }


}
