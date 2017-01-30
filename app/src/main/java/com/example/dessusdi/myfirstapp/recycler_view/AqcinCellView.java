package com.example.dessusdi.myfirstapp.recycler_view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.dessusdi.myfirstapp.R;
import com.example.dessusdi.myfirstapp.model.WaqiObject;

/**
 * Created by dessusdi on 30/01/2017.
 */
public class AqcinCellView extends RecyclerView.ViewHolder{

    private TextView city_nameTextView;
    private TextView air_qualityTextView;
    private TextView gpsTextView;
    private TextView minTextView;
    private TextView maxTextView;

    public AqcinCellView(View itemView) {
        super(itemView);

        air_qualityTextView = (TextView) itemView.findViewById(R.id.air_qualityTextView);
        city_nameTextView = (TextView) itemView.findViewById(R.id.city_nameTextView);
        gpsTextView = (TextView) itemView.findViewById(R.id.gpsTextView);
        minTextView = (TextView) itemView.findViewById(R.id.minTextView);
        maxTextView = (TextView) itemView.findViewById(R.id.maxTextView);
    }

    public void bind(WaqiObject myObject){
        city_nameTextView.setText(myObject.getName());
        air_qualityTextView.setText(myObject.getAirQuality());
        gpsTextView.setText(myObject.getGPSCoordinate());
        minTextView.setText(myObject.getMinTemp());
        maxTextView.setText(myObject.getMaxTemp());
    }

}
