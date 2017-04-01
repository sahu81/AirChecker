package com.example.dessusdi.myfirstapp.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dessusdi.myfirstapp.R;
import com.example.dessusdi.myfirstapp.models.air_quality.WaqiObject;

import lecho.lib.hellocharts.view.LineChartView;

/**
 * Created by dessusdi on 27/03/2017.
 * DESSUS Dimitri
 */

public class EvolutionFragment extends Fragment {

    private WaqiObject city;
    private LineChartView aqcinChart;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_evolution, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.aqcinChart = (LineChartView) view.findViewById(R.id.aqcinChart);
        this.aqcinChart.setInteractive(true);
        this.aqcinChart.setLineChartData(this.city.getForecastChartData());
    }

    public void setCity(WaqiObject city) {
        this.city = city;
    }
}
