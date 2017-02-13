package com.example.dessusdi.myfirstapp.tools;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import com.example.dessusdi.myfirstapp.R;

import java.util.Timer;
import java.util.TimerTask;

public class BackgroundRefresher extends Service {

    public static final int delay   = 5000; // Delay between each search query
    private Handler mHandler        = new Handler();
    private Timer mTimer            = null;

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
        mTimer.scheduleAtFixedRate(new TimeDisplay(), 0, delay);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mTimer.cancel();
        Toast.makeText(this, R.string.service_killed, Toast.LENGTH_SHORT).show();
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
