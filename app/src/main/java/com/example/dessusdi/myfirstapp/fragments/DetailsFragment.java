package com.example.dessusdi.myfirstapp.fragments;

import android.app.Fragment;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dessusdi.myfirstapp.R;
import com.example.dessusdi.myfirstapp.models.air_quality.WaqiObject;

/**
 * Created by Dimitri on 02/03/2017.
 */

public class DetailsFragment extends Fragment {

    private WaqiObject city;
    private ImageView cityPicture;
    private TextView cityDescription;
    private Button cityEvolution;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_details, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.cityPicture        = (ImageView) view.findViewById(R.id.cityPicture);
        this.cityDescription    = (TextView) view.findViewById(R.id.cityDescription);
        this.cityEvolution      = (Button) view.findViewById(R.id.cityEvolution);
    }

    public void setCity(WaqiObject city) {
        this.city = city;
        this.fetchEvolution();
    }

    private void fetchEvolution() {
        Log.d("DATA", "----> " + this.city.getName());
    }
}
