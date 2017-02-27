package com.example.dessusdi.myfirstapp;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.util.Log;

import com.example.dessusdi.myfirstapp.tools.LanguageUpdater;
import com.example.dessusdi.myfirstapp.tools.ThemeUpdater;

import java.lang.reflect.Field;
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
        private ThemeUpdater themeUpdater;
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
            this.themeUpdater        = new ThemeUpdater(this.getActivity(), this.getPreferenceManager().getSharedPreferences());

            // Loading from shared prefs current locale
            this.languagePreference.setSummary(this.langUpdater.getSavedLocale());
            this.themePreferences.setSummary(getResources().getString(getResources().getIdentifier(this.themeUpdater.getSavedTheme(), "string", getActivity().getPackageName())));

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
                this.getActivity().setTheme(this.themeUpdater.getTheme(newValue.toString()));
                return true;
            } else {
                return false;
            }
        }
    }


}
