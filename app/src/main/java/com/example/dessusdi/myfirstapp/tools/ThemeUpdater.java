package com.example.dessusdi.myfirstapp.tools;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.dessusdi.myfirstapp.R;

import java.util.Calendar;

/**
 * Created by Dimitri on 27/02/2017.
 */

public class ThemeUpdater {
    private SharedPreferences sharedPreferences;
    private Context context;

    public ThemeUpdater() {}

    public ThemeUpdater(Context context, SharedPreferences sharedPreferences) {
        this.context = context;
        this.sharedPreferences = sharedPreferences;
    }

    public void setTheme(String newTheme) {
        int theme;
        switch (newTheme) {
            case "theme_sunset":
                theme = R.style.SunsetTheme;
                break;
            case "theme_darken":
                theme = R.style.NightTheme;
                break;
            default:
                theme = R.style.AppTheme;
                break;
        }

        this.context.setTheme(theme);
    }

    public void restartActivities() {
        Activity activity = (Activity) context;
        Intent mIntent = activity.getIntent();
        activity.finish();
        activity.startActivity(mIntent);
    }

    public void loadSavedTheme() {
        // Check if auto night theme is enabled
        if(this.sharedPreferences.getBoolean("theme_auto_preference", true)) {
            int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
            if(hour >= 18 || (hour >= 0 && hour <= 6)) {
                // Set night mode
                this.context.setTheme(R.style.NightTheme);
                return;
            }
        }

        this.setTheme(getSavedTheme());
    }

    public String getSavedTheme() {
        return this.sharedPreferences.getString("theme_preference", context.getResources().getString(R.string.theme_light_alias));
    }
}
