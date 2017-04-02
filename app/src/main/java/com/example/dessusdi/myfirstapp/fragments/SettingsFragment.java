package com.example.dessusdi.myfirstapp.fragments;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dessusdi.myfirstapp.R;
import com.example.dessusdi.myfirstapp.tools.LanguageUpdater;
import com.example.dessusdi.myfirstapp.tools.ThemeUpdater;

/**
 * Created by Dimitri on 01/03/2017.
 */

public class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {

    private LanguageUpdater langUpdater;
    private ThemeUpdater themeUpdater;
    private ListPreference languagePreference;
    private ListPreference themePreferences;

    /**
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings_global);

        // Adding listeners to ListPreference
        this.languagePreference = (ListPreference)findPreference("language_preference");
        this.themePreferences   = (ListPreference)findPreference("theme_preference");
        this.langUpdater        = new LanguageUpdater(this.getActivity(), this.getPreferenceManager().getSharedPreferences());
        this.themeUpdater       = new ThemeUpdater(this.getActivity(), this.getPreferenceManager().getSharedPreferences());

        // Loading from shared prefs current locale
        this.languagePreference.setSummary(this.langUpdater.getSavedLocale());
        this.themePreferences.setSummary(getResources().getString(getResources().getIdentifier(this.themeUpdater.getSavedTheme(), "string", getActivity().getPackageName())));

        // Setting up listeners on ListPreference
        languagePreference.setOnPreferenceChangeListener(this);
        themePreferences.setOnPreferenceChangeListener(this);
    }

    /**
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        assert view != null;
        view.setBackgroundColor(getResources().getColor(android.R.color.white));

        return view;
    }

    /**
     * Listener on preference change
     * @param preference
     * @param newValue
     * @return
     */
    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if(preference == this.languagePreference) {
            this.langUpdater.buildLanguageConfiguration(newValue.toString(), true);
            return true;
        } else if(preference == this.themePreferences) {
            this.themeUpdater.setTheme(newValue.toString());
            this.themeUpdater.restartActivities();
            this.themePreferences.setSummary(getResources().getString(getResources().getIdentifier(newValue.toString(), "string", getActivity().getPackageName())));
            return true;
        } else {
            return false;
        }
    }
}
