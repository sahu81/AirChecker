package com.example.dessusdi.myfirstapp.model;

import java.util.ArrayList;

/**
 * Created by dessusdi on 30/01/2017.
 */
public class MessageObject {
    private long timestamp;
    private CityObject city;
    private ArrayList<AqcinObject> iaqi;

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

    public ArrayList<AqcinObject> getIaqi() {
        return iaqi;
    }

    public void setIaqi(ArrayList<AqcinObject> iaqi) {
        this.iaqi = iaqi;
    }
}
