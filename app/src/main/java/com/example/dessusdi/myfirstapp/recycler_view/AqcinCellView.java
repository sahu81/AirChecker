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
    final private TextView city_nameTextView;
    final private TextView air_qualityTextView;
    final private TextView gpsTextView;
    final private TextView minTextView;
    final private TextView maxTextView;
    final private ImageButton shareImageButton;
    final private ImageButton refreshImageButton;
    final private ImageButton bellImageButton;

    /**
     * @param itemView cell view
     */
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

    /**
     * Click global listener
     * @param v button view
     */
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
            this.waqi.setGlobalObject(null);
            this.waqi.fetchData();
        } else if (v == this.bellImageButton) {
            // Play a sound according to the air quality level
            MediaPlayer mPlayer = MediaPlayer.create(context, this.waqi.getSoundResId());
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mPlayer.start();
        }
    }

    /**
     * Waqi object setter
     * @param myObject WaqiObject containing city informations
     * @see WaqiObject
     */
    public void setWaqiObject(WaqiObject myObject){
        this.waqi = myObject;
        this.bindDataWithCell();
    }

    /**
     * Bind data with cell.
     * This private method assign data to UI components.
     */
    private void bindDataWithCell() {
        city_nameTextView.setText(this.waqi.getName());
        air_qualityTextView.setText(String.valueOf(this.waqi.getAirQuality()));
        gpsTextView.setText(this.waqi.getGPSCoordinate());
        minTextView.setText(this.waqi.getMinTemp());
        maxTextView.setText(this.waqi.getMaxTemp());
        air_qualityTextView.setBackgroundColor(Color.parseColor(WaqiObject.getColorCode(this.waqi.getAirQuality())));
    }

    /**
     * Context setter
     * @param context context of the activity
     */
    public void setContext(Context context) {
        this.context = context;
    }

}
