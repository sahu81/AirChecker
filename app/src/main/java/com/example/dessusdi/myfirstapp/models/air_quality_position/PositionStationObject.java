package com.example.dessusdi.myfirstapp.models.air_quality_position;

/**
 * Created by dessusdi on 27/03/2017.
 * DESSUS Dimitri
 */
public class PositionStationObject {
    private int aqi;
    private int idx;
    private String dominentpol;
    private PositionCityObject city;

    public int getAqi() {
        return aqi;
    }

    public void setAqi(int aqi) {
        this.aqi = aqi;
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public String getDominentpol() {
        return dominentpol;
    }

    public void setDominentpol(String dominentpol) {
        this.dominentpol = dominentpol;
    }

    public PositionCityObject getCity() {
        return city;
    }

    public void setCity(PositionCityObject city) {
        this.city = city;
    }
}
