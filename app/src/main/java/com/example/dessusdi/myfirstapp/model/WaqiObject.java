package com.example.dessusdi.myfirstapp.model;

import android.app.Activity;
import android.util.Log;

import com.example.dessusdi.myfirstapp.tools.AqcinRequestService;

/**
 * Created by dessusdi on 30/01/2017.
 * DESSUS Dimitri
 */
public class WaqiObject {
    private AqcinRequestService waqiService;
    private GlobalObject globalObject;

    public WaqiObject(AqcinRequestService waqiService) {
        this.waqiService = waqiService;
    }

    public void fetchData() {
        this.waqiService.sendRequestWithUrl("https://api.waqi.info/api/feed/@3069/obs.en.json",
        new AqcinRequestService.VolleyCallback() {
            @Override
            public void onSuccess(GlobalObject global) {
                Log.d("LOG", globalObject.getRxs().getObs().get(0).getMsg().getCity().getName());
                globalObject = global;
                //mTextView.setText(global.getRxs().getObs().get(0).getMsg().getCity().getName());
            }
        });
        // TODO: Completion block and assign to globalobject
    }
}
