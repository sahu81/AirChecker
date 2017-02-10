package com.example.dessusdi.myfirstapp.models;

import java.util.ArrayList;

/**
 * Created by dessusdi on 30/01/2017.
 * DESSUS Dimitri
 */
public class AqiObject {
    private String t;
    private ArrayList<Integer> v;

    public ArrayList<Integer> getV() {
        return v;
    }

    public void setV(ArrayList<Integer> v) {
        this.v = v;
    }

    public String getT() {
        return t;
    }

    public void setT(String t) {
        this.t = t;
    }

}
