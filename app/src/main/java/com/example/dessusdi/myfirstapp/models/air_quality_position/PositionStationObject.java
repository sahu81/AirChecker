package com.example.dessusdi.myfirstapp.models.air_quality_position;

/**
 * Created by dessusdi on 27/03/2017.
 * DESSUS Dimitri
 */
public class PositionStationObject {
    private Float lat;
    private Float lon;
    private int uid;
    private String aqi;

    public Float getLat() {
        return lat;
    }

    public void setLat(Float lat) {
        this.lat = lat;
    }

    public Float getLon() {
        return lon;
    }

    public void setLon(Float lon) {
        this.lon = lon;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getAqi() {
        return aqi;
    }

    public void setAqi(String aqi) {
        this.aqi = aqi;
    }
}
