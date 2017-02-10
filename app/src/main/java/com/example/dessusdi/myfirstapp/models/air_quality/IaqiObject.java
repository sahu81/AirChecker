package com.example.dessusdi.myfirstapp.models.air_quality;

import java.util.ArrayList;

/**
 * Created by dessusdi on 30/01/2017.
 * DESSUS Dimitri
 */
public class IaqiObject {

    private String p;
    private ArrayList<Integer> v;
    private String i;

    public String getP() {
        return p;
    }

    public void setP(String p) {
        this.p = p;
    }

    public ArrayList<Integer> getV() {
        return v;
    }

    public void setV(ArrayList<Integer> v) {
        this.v = v;
    }

    public String getI() {
        return i;
    }

    public void setI(String i) {
        this.i = i;
    }
}
