package com.example.dessusdi.myfirstapp.model;

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

    public void setForecast(ForecastObject forecast) {
        this.forecast = forecast;
    }

    public int getAqi() {
        return aqi;
    }

    public void setAqi(int aqi) {
        this.aqi = aqi;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
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

    public void setIaqi(ArrayList<IaqiObject> iaqi) {
        this.iaqi = iaqi;
    }
}
