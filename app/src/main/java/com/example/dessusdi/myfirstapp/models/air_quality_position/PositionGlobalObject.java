package com.example.dessusdi.myfirstapp.models.air_quality_position;

import com.example.dessusdi.myfirstapp.models.air_quality.WaqiObject;

/**
 * Created by dessusdi on 27/03/2017.
 * DESSUS Dimitri
 */
public class PositionGlobalObject {
    private String status;
    private PositionStationObject data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private PositionStationObject getData() {
        return data;
    }

    public void setData(PositionStationObject data) {
        this.data = data;
    }

    public String getName() {
        return this.getData().getCity().getName();
    }

    public String getGPSCoordinate() {
        return String.format("GPS : %.6f - %.6f", this.getData().getCity().getGeo().get(0), this.getData().getCity().getGeo().get(1));
    }

    public String getAqi() {
        return String.valueOf(this.getData().getAqi());
    }

    public String getColorCode() {
        return WaqiObject.getColorCode(this.getData().getAqi());
    }
}
