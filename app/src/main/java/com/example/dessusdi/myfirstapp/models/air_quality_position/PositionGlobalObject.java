package com.example.dessusdi.myfirstapp.models.air_quality_position;

import com.example.dessusdi.myfirstapp.models.air_quality.WaqiObject;

/**
 * Created by dessusdi on 27/03/2017.
 * DESSUS Dimitri
 */
public class PositionGlobalObject {
    private String status;
    private PositionStationObject data;

    private PositionStationObject getData() {
        return data;
    }

    public void setData(PositionStationObject data) {
        this.data = data;
    }

    /**
     * @return Station name
     */
    public String getName() {
        return this.getData().getCity().getName();
    }

    /**
     * @return Formatted location of the station
     */
    public String getGPSCoordinate() {
        return String.format("GPS : %.6f - %.6f", this.getData().getCity().getGeo().get(0), this.getData().getCity().getGeo().get(1));
    }

    /**
     * @return Pollution level
     */
    public String getAqi() {
        return String.valueOf(this.getData().getAqi());
    }

    /**
     * @return Return properly color according to air quality
     * @see WaqiObject
     */
    public String getColorCode() {
        return WaqiObject.getColorCode(this.getData().getAqi());
    }
}
