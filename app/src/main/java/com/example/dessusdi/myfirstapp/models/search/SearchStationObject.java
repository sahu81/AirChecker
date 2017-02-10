package com.example.dessusdi.myfirstapp.models.search;

import java.util.ArrayList;

/**
 * Created by dessusdi on 10/02/2017.
 * DESSUS Dimitri
 */
public class SearchStationObject {
    private String name;
    private String url;
    private ArrayList<Float> geo;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ArrayList<Float> getGeo() {
        return geo;
    }

    public void setGeo(ArrayList<Float> geo) {
        this.geo = geo;
    }
}
