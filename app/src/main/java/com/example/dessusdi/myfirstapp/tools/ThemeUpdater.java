package com.example.dessusdi.myfirstapp.tools;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.example.dessusdi.myfirstapp.MainActivity;
import com.example.dessusdi.myfirstapp.R;

import java.util.Locale;

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
        TaskStackBuilder.create(activity)
                .addNextIntent(new Intent(activity, MainActivity.class))
                .addNextIntent(activity.getIntent())
                .startActivities();
    }

    public void loadSavedTheme() {
        this.setTheme(getSavedTheme());
    }

    public String getSavedTheme() {
        return this.sharedPreferences.getString("theme_preference", context.getResources().getString(R.string.theme_light_alias));
    }
}
