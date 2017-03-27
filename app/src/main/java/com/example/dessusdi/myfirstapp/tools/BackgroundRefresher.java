package com.example.dessusdi.myfirstapp.tools;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.example.dessusdi.myfirstapp.MainActivity;
import com.example.dessusdi.myfirstapp.R;
import com.example.dessusdi.myfirstapp.models.air_quality.WaqiObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class BackgroundRefresher extends IntentService {

    public static final int delay = 5000; // Delay between each search query
    private Handler mHandler = new Handler();
    private Timer mTimer = null;
    private List<WaqiObject> cities = new ArrayList<>();
    private AqcinRequestService async;

    public BackgroundRefresher() {
        super("BackgroundRefresher");

        this.async = new AqcinRequestService(this.getBaseContext());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        //Toast.makeText(BackgroundRefresher.this, R.string.service_checking, Toast.LENGTH_SHORT).show();

        if (mTimer != null)
            mTimer.cancel();
        else
            mTimer = new Timer();
        mTimer.scheduleAtFixedRate(new TimeDisplay(), 0, delay);

    }

    class TimeDisplay extends TimerTask {
        @Override
        public void run() {
            // Fetching data...
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(BackgroundRefresher.this, R.string.service_checking, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
