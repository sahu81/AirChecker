package com.example.dessusdi.myfirstapp.models.air_quality_position;

import java.util.ArrayList;

/**
 * Created by dessusdi on 27/03/2017.
 * DESSUS Dimitri
 */
public class PositionGlobalObject {
    private String status;
    private ArrayList<PositionStationObject> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<PositionStationObject> getData() {
        return data;
    }

    public void setData(ArrayList<PositionStationObject> data) {
        this.data = data;
    }
}
