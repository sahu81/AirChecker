package com.example.dessusdi.myfirstapp;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.util.Log;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by dessusdi on 13/02/2017.
 * DESSUS Dimitri
 */

public class SettingsActivity extends PreferenceActivity {

    @Override
    protected boolean isValidFragment(String fragmentName) {
        return CustomizationFragment.class.getName().equals(fragmentName);
    }

    @Override
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.settings, target);
    }

    public static class CustomizationFragment extends PreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_customization);

        }
    }
}
