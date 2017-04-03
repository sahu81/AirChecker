package com.example.dessusdi.myfirstapp.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.dessusdi.myfirstapp.MainActivity;
import com.example.dessusdi.myfirstapp.R;
import com.example.dessusdi.myfirstapp.models.air_quality.GlobalObject;
import com.example.dessusdi.myfirstapp.models.air_quality.WaqiObject;
import com.example.dessusdi.myfirstapp.tools.RequestBuilder;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class BackgroundRefresher extends Service {

    private static final String TAG     = "Background";
    private static final int delay      = 1800000; // Delay between each search query in ms (15 min here)
    private static final int limit      = 100; // Trigger notification when limit reached
    private final Handler mHandler      = new Handler();
    private Timer mTimer                = null;
    private final List<WaqiObject> cities           = new ArrayList<>();
    private final List<Integer> notificationsFired  = new ArrayList<>();
    private RequestQueue reQueue;

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        if (mTimer != null)
            mTimer.cancel();
        else
            mTimer = new Timer();

        // Clear notifications fired array
        this.notificationsFired.clear();
        // Launch refresher task service timer
        mTimer.scheduleAtFixedRate(new TimeDisplay(), 1000, delay);
    }

    /**
     * Service destroyed
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        mTimer.cancel();
        Toast.makeText(this, R.string.service_killed, Toast.LENGTH_SHORT).show();
    }

    private class TimeDisplay extends TimerTask {
        @Override
        public void run() {
            // Fetching data...
            mHandler.post(new Runnable() {
                /**
                 * Checking cities data every x sec.
                 * @see WaqiObject
                 */
                @Override
                public void run() {
                    Toast.makeText(BackgroundRefresher.this, R.string.service_checking, Toast.LENGTH_SHORT).show();
                    cities.clear();
                    cities.addAll(WaqiObject.listAll(WaqiObject.class));

                    Log.d(TAG, "Checking cities on background task...");
                    for (WaqiObject cityObject : cities) {
                        this.retrieveAirQuality(cityObject.getIdentifier());
                    }
                }

                /**
                 * Retrieve air quality for specific city identifier
                 * @param identifier city identifier
                 */
                private void retrieveAirQuality(final int identifier) {
                    // Assigning RequestQueue
                    reQueue = Volley.newRequestQueue(BackgroundRefresher.this);
                    // Build and launch request
                    StringRequest request = new StringRequest(com.android.volley.Request.Method.GET,
                            RequestBuilder.buildAirQualityURL(identifier),
                            new Response.Listener<String>() {

                                @Override
                                public void onResponse(String response) {
                                    // Parsing JSON string

                                    GlobalObject global = null;
                                    Gson gson = new Gson();
                                    try {
                                        global = gson.fromJson(response, GlobalObject.class);
                                    } catch (IllegalStateException | JsonSyntaxException exception){
                                        Log.d(TAG, "error when parsing GlobalObject");
                                    }

                                    if(global == null)
                                        return;

                                    int index = global.getRxs().getObs().size() - 1;
                                    int threshold = global.getRxs().getObs().get(index).getMsg().getAqi();

                                    // Checking if air level greater/equal than limit
                                    if (threshold >= limit) {
                                        // Air quality is greater/equal
                                        // Checking if notification already fired
                                        if (!notificationsFired.contains(identifier)) {
                                            // Send alert push notification
                                            sendAlertPushNotification(global.getRxs().getObs().get(index).getMsg().getCity().getName(), threshold);
                                            Log.d(TAG, "Notification fired !");
                                            notificationsFired.add(identifier);
                                        }
                                    } else {
                                        // Removing identifier from fired push array
                                        if (notificationsFired.contains(identifier))
                                            notificationsFired.remove(identifier);
                                    }

                                }
                            },

                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.d(TAG, "Rate limit exceeded, trying again in " + delay);
                                }
                            });

                    try {
                        reQueue.add(request);
                    } catch (Exception e) {
                        Log.e(TAG, e.toString());
                    }
                }

                /**
                 * Fire a push notification.
                 * @param city name of the city
                 * @param threshold air quality of the city
                 */
                private void sendAlertPushNotification(String city, int threshold) {
                    NotificationCompat.Builder b = new NotificationCompat.Builder(BackgroundRefresher.this);

                    Integer pushIdentifier = (int) (long) System.currentTimeMillis() / 1000;

                    Intent notificationIntent = new Intent(BackgroundRefresher.this, MainActivity.class);
                    PendingIntent contentIntent = PendingIntent.getActivity(BackgroundRefresher.this, 0, notificationIntent, 0);

                    b.setAutoCancel(true)
                            .setDefaults(Notification.DEFAULT_ALL)
                            .setWhen(System.currentTimeMillis())
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle(getString(R.string.push_alert_title))
                            .setContentText(String.format(getString(R.string.push_alert_content), city, threshold))
                            .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                            .setContentIntent(contentIntent);

                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.notify(pushIdentifier, b.build());
                }
            });
        }
    }
}
