package com.example.dessusdi.myfirstapp.tools;

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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.dessusdi.myfirstapp.R;
import com.example.dessusdi.myfirstapp.models.air_quality.GlobalObject;
import com.example.dessusdi.myfirstapp.models.air_quality.WaqiObject;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * Created by Dimitri on 01/04/2017.
 */

public class AirCheckerWidget extends AppWidgetProvider {

    private static final String TAG = "Widget";

    /**
     * @param context
     * @param appWidgetManager
     * @param appWidgetIds
     */
    @Override
    public void onUpdate(final Context context, final AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        RequestQueue reQueue = Volley.newRequestQueue(context);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        int favId = prefs.getInt("fav_city", 99999);

        Log.d(TAG, "identifier --> " + favId);
        for (final int widgetId : appWidgetIds) {
            final RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);
            final Intent intent = new Intent(context, AirCheckerWidget.class);
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                    0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.refreshWidgetButton, pendingIntent);

            // If favorite id set, perform request.
            if (favId != 99999) {
                StringRequest request = new StringRequest(Request.Method.GET,
                        RequestBuilder.buildAirQualityURL(favId),
                        new Response.Listener<String>() {

                            @Override
                            public void onResponse(String response) {

                                GlobalObject global = null;
                                Gson gson = new Gson();
                                try {
                                    global = gson.fromJson(response, GlobalObject.class);
                                } catch (IllegalStateException | JsonSyntaxException exception){
                                    Log.d(TAG, "error when parsing GlobalObject");
                                }

                                if(global != null) {
                                    int index = global.getRxs().getObs().size() - 1;
                                    int aqi = global.getRxs().getObs().get(index).getMsg().getAqi();

                                    remoteViews.setTextViewText(R.id.air_qualityWidgetTextView, String.valueOf(aqi));
                                    remoteViews.setTextViewText(R.id.city_nameWidgetTextView, global.getRxs().getObs().get(index).getMsg().getCity().getName());
                                    remoteViews.setInt(R.id.air_qualityWidgetTextView, "setBackgroundColor", Color.parseColor(WaqiObject.getColorCode(aqi)));
                                }

                                appWidgetManager.updateAppWidget(widgetId, remoteViews);
                            }
                        },

                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d(TAG, "Error when fetching data");
                                appWidgetManager.updateAppWidget(widgetId, remoteViews);
                            }
                        });

                try {
                    reQueue.add(request);
                } catch (Exception e) {
                    appWidgetManager.updateAppWidget(widgetId, remoteViews);
                }
            } else {
                remoteViews.setTextViewText(R.id.city_nameWidgetTextView, context.getString(R.string.no_fav));

                appWidgetManager.updateAppWidget(widgetId, remoteViews);
            }
        }
    }
}
