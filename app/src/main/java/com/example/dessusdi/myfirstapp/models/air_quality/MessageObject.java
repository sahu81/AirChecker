package com.example.dessusdi.myfirstapp.models.air_quality;

import java.util.ArrayList;

/**
 * Created by dessusdi on 30/01/2017.
 * DESSUS Dimitri
 */
public class MessageObject {
    private long timestamp;
    private CityObject city;
    private ForecastObject forecast;
    private ArrayList<IaqiObject> iaqi;
    private int aqi;

    public ForecastObject getForecast() {
        return forecast;
    }

    public int getAqi() {
        return aqi;
    }

    public CityObject getCity() {
        return city;
    }

    public void setCity(CityObject city) {
        this.city = city;
    }

    public ArrayList<IaqiObject> getIaqi() {
        return iaqi;
    }
}
