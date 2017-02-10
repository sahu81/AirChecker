package com.example.dessusdi.myfirstapp.models.search;

import java.util.ArrayList;

/**
 * Created by dessusdi on 10/02/2017.
 * DESSUS Dimitri
 */
public class SearchLocationObject {
    private int uid;
    private SearchStationObject station;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public SearchStationObject getStation() {
        return station;
    }

    public void setStation(SearchStationObject station) {
        this.station = station;
    }
}
