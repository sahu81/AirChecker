package com.example.dessusdi.myfirstapp.model;

import android.app.Activity;
import com.example.dessusdi.myfirstapp.tools.AqcinRequestService;

import java.util.ArrayList;

/**
 * Created by dessusdi on 30/01/2017.
 */
public class AqcinObject {

    String p;
    ArrayList<Integer> v;
    String i;

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
