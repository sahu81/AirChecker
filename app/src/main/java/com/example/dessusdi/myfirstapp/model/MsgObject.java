package com.example.dessusdi.myfirstapp.model;

/**
 * Created by dessusdi on 30/01/2017.
 */
public class MsgObject {
    private double timestamp;
    private CityObject cityObject;

    public double getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(double timestamp) {
        this.timestamp = timestamp;
    }

    public CityObject getCityObject() {
        return cityObject;
    }

    public void setCityObject(CityObject cityObject) {
        this.cityObject = cityObject;
    }
}
