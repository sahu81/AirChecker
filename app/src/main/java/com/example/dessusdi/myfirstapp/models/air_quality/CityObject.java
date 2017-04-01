package com.example.dessusdi.myfirstapp.models.air_quality;

import java.util.ArrayList;

/**
 * Created by dessusdi on 30/01/2017.
 * DESSUS Dimitri
 */
public class CityObject {

    private String name;
    private String url;
    private int idx;
    private String id;
    private ArrayList<Float> geo;

    public ArrayList<Float> getGeo() {
        return geo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
