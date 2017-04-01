package com.example.dessusdi.myfirstapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.dessusdi.myfirstapp.models.air_quality.GlobalObject;
import com.example.dessusdi.myfirstapp.models.air_quality.WaqiObject;
import com.example.dessusdi.myfirstapp.tools.BackgroundRefresher;
import com.example.dessusdi.myfirstapp.tools.RequestBuilder;
import com.google.gson.Gson;

import java.util.Random;

/**
 * Created by Dimitri on 01/04/2017.
 */

public class AirCheckerWidget extends AppWidgetProvider {

    private RequestQueue reQueue;

    @Override
    public void onUpdate(final Context context, final AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int count = appWidgetIds.length;
        reQueue = Volley.newRequestQueue(context);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        int favId = prefs.getInt("fav_city", 1000);

        for (int i = 0; i < count; i++) {
            final int widgetId = appWidgetIds[i];

            final RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);
            final Intent intent = new Intent(context, AirCheckerWidget.class);
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);

            StringRequest request = new StringRequest(com.android.volley.Request.Method.GET,
                    RequestBuilder.buildAirQualityURL(favId),
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            Gson gson = new Gson();
                            GlobalObject global = gson.fromJson(response, GlobalObject.class);
                            int aqi = global.getRxs().getObs().get(0).getMsg().getAqi();

                            remoteViews.setTextViewText(R.id.air_qualityWidgetTextView, String.valueOf(aqi));
                            remoteViews.setTextViewText(R.id.city_nameWidgetTextView, global.getRxs().getObs().get(0).getMsg().getCity().getName());
                            remoteViews.setInt(R.id.air_qualityWidgetTextView, "setBackgroundColor", Color.parseColor(WaqiObject.getColorCode(aqi)));
                            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                                    0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                            remoteViews.setOnClickPendingIntent(R.id.refreshWidgetButton, pendingIntent);

                            appWidgetManager.updateAppWidget(widgetId, remoteViews);
                        }
                    },

                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("WIDGET", "Error when fetching data");
                            appWidgetManager.updateAppWidget(widgetId, remoteViews);
                        }
                    });

            try {
                reQueue.add(request);
            } catch (Exception e) {
                appWidgetManager.updateAppWidget(widgetId, remoteViews);
            }
        }
    }
}
