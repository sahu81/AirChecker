package com.example.dessusdi.myfirstapp.recycler_view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.dessusdi.myfirstapp.R;
import com.example.dessusdi.myfirstapp.models.air_quality.WaqiObject;

/**
 * Created by dessusdi on 30/01/2017.
 * DESSUS Dimitri
 */
public class AqcinCellView extends RecyclerView.ViewHolder implements View.OnClickListener {

    private WaqiObject waqi;
    private Context context;
    private TextView city_nameTextView;
    private TextView air_qualityTextView;
    private TextView gpsTextView;
    private TextView minTextView;
    private TextView maxTextView;
    private ImageButton shareImageButton;
    private ImageButton refreshImageButton;
    private ImageButton bellImageButton;

    public AqcinCellView(View itemView) {
        super(itemView);

        // Assigning layout elements
        air_qualityTextView     = (TextView) itemView.findViewById(R.id.air_qualityTextView);
        city_nameTextView       = (TextView) itemView.findViewById(R.id.city_nameTextView);
        gpsTextView             = (TextView) itemView.findViewById(R.id.gpsTextView);
        minTextView             = (TextView) itemView.findViewById(R.id.minTextView);
        maxTextView             = (TextView) itemView.findViewById(R.id.maxTextView);
        shareImageButton        = (ImageButton) itemView.findViewById(R.id.shareButton);
        refreshImageButton      = (ImageButton) itemView.findViewById(R.id.refreshButton);
        bellImageButton         = (ImageButton) itemView.findViewById(R.id.soundButton);

        // Setting listener on buttons
        shareImageButton.setOnClickListener(this);
        refreshImageButton.setOnClickListener(this);
        bellImageButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // Ensure to call properly action according to button
        if (v == this.shareImageButton) {
            String subject  = (this.context.getResources().getString(R.string.share_air_quality_mail_subject));
            String body     = String.format(this.context.getResources().getString(R.string.share_air_quality_mail_body),
                    this.waqi.getName(),
                    this.waqi.getAirQuality(),
                    this.context.getResources().getString(R.string.app_name));

            Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
            emailIntent.setType("message/rfc822");
            emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
            emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, body);
            this.context.startActivity(Intent.createChooser(emailIntent, "Email:"));
        } else if (v == this.refreshImageButton) {
            this.waqi.fetchData();
        } else {
            // Play a sound according to the air quality level
            MediaPlayer mPlayer = MediaPlayer.create(context, this.waqi.getSoundResId());
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mPlayer.start();
        }
    }

    public void setWaqiObject(WaqiObject myObject){
        this.waqi = myObject;
        this.bindDataWithCell();
    }

    private void bindDataWithCell() {
        city_nameTextView.setText(this.waqi.getName());
        air_qualityTextView.setText(String.valueOf(this.waqi.getAirQuality()));
        gpsTextView.setText(this.waqi.getGPSCoordinate());
        minTextView.setText(this.waqi.getMinTemp());
        maxTextView.setText(this.waqi.getMaxTemp());
        air_qualityTextView.setBackgroundColor(Color.parseColor(this.waqi.getColorCode()));
    }

    public void setContext(Context context) {
        this.context = context;
    }

}
