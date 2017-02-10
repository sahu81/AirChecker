package com.example.dessusdi.myfirstapp.models;

import java.util.ArrayList;

/**
 * Created by dessusdi on 30/01/2017.
 * DESSUS Dimitri
 */
public class ForecastObject {
    private ArrayList<AqiObject> aqi;

    public ArrayList<AqiObject> getAqi() {
        return aqi;
    }

    public void setAqi(ArrayList<AqiObject> aqi) {
        this.aqi = aqi;
    }
}
