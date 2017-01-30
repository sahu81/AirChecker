package com.example.dessusdi.myfirstapp.model;

import android.app.Activity;
import com.example.dessusdi.myfirstapp.tools.AqcinRequestService;

/**
 * Created by dessusdi on 30/01/2017.
 */
public class AqcinObject {

    private AqcinRequestService async;
    private String jsonStr;

    public AqcinObject(Activity context) {
        this.async = new AqcinRequestService(context);
    }

    public void getData() {
        this.async.sendRequestWithUrl("https://api.waqi.info/api/feed/@3069/obs.en.json");
    }
}
